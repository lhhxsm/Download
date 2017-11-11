package com.android.download;

import com.android.download.db.DownloadEntity;
import com.android.download.db.DownloadHelper;
import com.android.download.file.FileStorageManager;
import com.android.download.http.DownloadCallback;
import com.android.download.http.HttpManager;
import com.android.download.utils.Logger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import okhttp3.Response;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/10.
 */
public class DownloadRunnable implements Runnable {
  private String mUrl;
  private long mStart;
  private long mEnd;
  private DownloadCallback mCallback;
  private DownloadEntity mEntity;

  public DownloadRunnable(String url, long start, long end, DownloadCallback callback,
      DownloadEntity entity) {
    mUrl = url;
    mStart = start;
    mEnd = end;
    mCallback = callback;
    mEntity = entity;
  }

  @Override public void run() {
    if (mCallback == null) {
      Logger.e("tag", "callback 异常");
      return;
    }
    //设置线程的优先级
    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    Response response = HttpManager.Holder.getInstance().syncRequestByRange(mUrl, mStart, mEnd);
    if (response == null) {
      mCallback.onFailure(HttpManager.NETWORK_ERROR_CODE, "网络出现问题了");
      return;
    }
    File file = FileStorageManager.Holder.getInstance().getFileByName(mUrl);
    try {
      RandomAccessFile raf = new RandomAccessFile(file, "rwd");
      //long finishProgress =
      //    mEntity.getProgressPosition() == null ? 0 : mEntity.getProgressPosition();
      long progress = 0;
      raf.seek(mStart);//偏移位置
      byte[] buffer = new byte[1024 * 500];
      int len;
      InputStream is = response.body().byteStream();
      while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        raf.write(buffer, 0, len);
        progress += len;
        mEntity.setProgressPosition(progress);
        //Logger.e("tag", "progress ---> " + progress);
      }
      //mEntity.setProgressPosition(mEntity.getProgressPosition()+finishProgress);
      raf.close();
      mCallback.onSuccess(file);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      DownloadHelper.Holder.getInstance().insert(mEntity);
    }
  }
}
