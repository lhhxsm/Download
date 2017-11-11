package com.android.download.test;

import android.app.Application;
import com.android.download.DownloadConfig;
import com.android.download.DownloadManager;
import com.android.download.db.DownloadHelper;
import com.android.download.file.FileStorageManager;
import com.facebook.stetho.Stetho;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/8.
 */
public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    FileStorageManager.Holder.getInstance().init(this);
    //HttpManager.Holder.getInstance().init(this);
    DownloadHelper.Holder.getInstance().init(this);
    DownloadConfig config = new DownloadConfig.Builder().setCoreThreadSize(2)
        .setMaxThreadSize(4)
        .setLocalProgressThreadSize(1)
        .build();
    DownloadManager.Holder.getInstance().init(config);
    Stetho.initializeWithDefaults(this);
  }
}
