package com.android.download.http;

import com.android.download.file.FileStorageManager;
import com.android.download.utils.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/9.
 */
public class HttpManager {
  public static final int NETWORK_ERROR_CODE = 1;
  public static final int CONTENT_LENGTH_ERROR_CODE = 2;
  public static final int TASK_RUNNING_ERROR_CODE = 3;

  //private Context mContext;
  private OkHttpClient mClient;

  private HttpManager() {
    mClient = new OkHttpClient();
  }

  /**
   * 同步请求
   */
  public Response syncRequest(String url) {
    Request request = new Request.Builder().url(url).build();
    try {
      return mClient.newCall(request).execute();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  //public void init() {
  //this.mContext = context;
  //}

  /**
   * 同步请求
   */
  public Response syncRequestByRange(String url, long start, long end) {
    Request request =
        new Request.Builder().url(url).addHeader("Range", "bytes=" + start + "-" + end).build();
    try {
      return mClient.newCall(request).execute();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 异步请求
   */
  public void asyncRequest(final String url, Callback callback) {
    if (callback == null) {
      Logger.e("tag", "callback 异常");
      return;
    }
    Request request = new Request.Builder().url(url).build();
    mClient.newCall(request).enqueue(callback);
  }

  /**
   * 异步请求
   */
  public void asyncRequest(final String url, final DownloadCallback callback) {
    if (callback == null) {
      Logger.e("tag", "callback 异常");
      return;
    }
    Request request = new Request.Builder().url(url).build();
    mClient.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        callback.onFailure(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          callback.onFailure(NETWORK_ERROR_CODE, "请求失败");
        }
        File file = FileStorageManager.Holder.getInstance().getFileByName(url);
        byte[] buffer = new byte[1024 * 500];
        int len;
        FileOutputStream fos = new FileOutputStream(file);
        InputStream is = response.body().byteStream();
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
          fos.write(buffer, 0, len);
          fos.flush();
        }
        callback.onSuccess(file);
      }
    });
  }

  public static class Holder {
    private static final HttpManager sInstance = new HttpManager();

    public static HttpManager getInstance() {
      return sInstance;
    }
  }
}
