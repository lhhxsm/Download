package com.android.download;

import android.support.annotation.NonNull;
import com.android.download.db.DownloadEntity;
import com.android.download.db.DownloadHelper;
import com.android.download.file.FileStorageManager;
import com.android.download.http.DownloadCallback;
import com.android.download.http.HttpManager;
import com.android.download.utils.Logger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/10.
 */
public class DownloadManager {
  private static ThreadPoolExecutor sThreadPool;
  private static ExecutorService sLocalProgressPool;
  private HashSet<DownloadTask> mHashSet = new HashSet<>();
  private List<DownloadEntity> mCache;
  private long mLength;
  private int mMaxThread;

  private DownloadManager() {
  }

  public void init(DownloadConfig config) {
    mMaxThread = config.getMaxThreadSize();
    sThreadPool =
        new ThreadPoolExecutor(config.getCoreThreadSize(), mMaxThread, 60, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
          private AtomicInteger mInteger = new AtomicInteger(1);

          @Override public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "download thread # " + mInteger.getAndIncrement());
          }
        });
    sLocalProgressPool = Executors.newFixedThreadPool(config.getLocalProgressThreadSize());
  }

  private void finish(DownloadTask task) {
    mHashSet.remove(task);
  }

  public void download(final String url, final DownloadCallback callback) {
    if (callback == null) {
      Logger.e("tag", "callback 异常");
      return;
    }
    final DownloadTask task = new DownloadTask(url, callback);
    if (mHashSet.contains(task)) {
      callback.onFailure(HttpManager.TASK_RUNNING_ERROR_CODE, "任务已经执行");
      return;
    }
    mHashSet.add(task);
    mCache = DownloadHelper.Holder.getInstance().queryAll(url);
    if (mCache == null || mCache.size() <= 0) {//没有数据
      HttpManager.Holder.getInstance().asyncRequest(url, new Callback() {
        @Override public void onFailure(Call call, IOException e) {
          callback.onFailure(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
          finish(task);
        }

        @Override public void onResponse(Call call, Response response) throws IOException {
          if (!response.isSuccessful()) {
            callback.onFailure(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
            return;
          }
          mLength = response.body().contentLength();
          if (mLength == -1) {
            callback.onFailure(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content length -1");
            return;
          }
          processDownload(url, mLength, callback, mCache);
          finish(task);
        }
      });
    } else {//处理已经下载过的数据
      for (int i = 0; i < mCache.size(); i++) {
        DownloadEntity entity = mCache.get(i);
        if (i == mCache.size() - 1) {
          mLength = entity.getEndPosition() + 1;
        }
        long startSize = entity.getStartPosition() + entity.getProgressPosition();
        long endSize = entity.getEndPosition();
        sThreadPool.execute(new DownloadRunnable(url, startSize, endSize, callback, entity));
      }
    }
    sLocalProgressPool.execute(new Runnable() {
      @Override public void run() {
        while (true) {
          try {
            Thread.sleep(500);
            File file = FileStorageManager.Holder.getInstance().getFileByName(url);
            long fileSize = file.length();
            int progress = (int) (fileSize * 100.0 / mLength);
            if (progress >= 100) {
              callback.onProgress(progress);
              return;
            }
            callback.onProgress(progress);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  private void processDownload(String url, long length, DownloadCallback callback,
      List<DownloadEntity> cache) {
    long threadDownloadSize = 1 + length / mMaxThread;
    if (cache == null) {
      mCache = new ArrayList<>();
    }
    for (int i = 0; i < mMaxThread; i++) {
      long startSize = i * threadDownloadSize;
      long endSize = 0;
      if (i == mMaxThread - 1) {
        endSize = length - 1;
      } else {
        endSize = (i + 1) * threadDownloadSize - 1;
      }
      DownloadEntity entity = new DownloadEntity();
      entity.setDownloadUrl(url);
      entity.setStartPosition(startSize);
      entity.setEndPosition(endSize);
      entity.setThreadId(i + 1);
      sThreadPool.execute(new DownloadRunnable(url, startSize, endSize, callback, entity));
    }
  }

  public static class Holder {
    private static DownloadManager sInstance = new DownloadManager();

    public static DownloadManager getInstance() {
      return sInstance;
    }
  }
}
