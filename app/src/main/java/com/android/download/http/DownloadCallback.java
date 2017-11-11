package com.android.download.http;

import java.io.File;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/9.
 */
public interface DownloadCallback {
  void onSuccess(File file);

  void onFailure(int errorCode, String errorMessage);

  void onProgress(int progress);
}
