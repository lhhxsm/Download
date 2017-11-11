package com.android.download.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.android.download.DownloadManager;
import com.android.download.R;
import com.android.download.http.DownloadCallback;
import com.android.download.utils.Logger;
import java.io.File;

public class MainActivity extends AppCompatActivity {
  private ImageView mIvPhoto;
  private int count = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mIvPhoto = (ImageView) findViewById(R.id.iv_photo);
    DownloadManager.Holder.getInstance()
        .download("http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk",
            new DownloadCallback() {
              @Override public void onSuccess(File file) {
                count++;
                Logger.e("tag", "success " + count);
              }

              @Override public void onFailure(int errorCode, String errorMessage) {
                Logger.e("tag", "failure " + errorCode + "  " + errorMessage);
              }

              @Override public void onProgress(int progress) {
                Logger.e("tag", System.currentTimeMillis() + " progress = " + progress);
              }
            });
  }

  private void installApk(File file) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(Uri.parse("file://" + file.getAbsoluteFile().toString()),
        "application/vnd.android.package-archive");
    MainActivity.this.startActivity(intent);
  }
}
