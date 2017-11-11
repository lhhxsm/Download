package com.android.download.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.android.download.utils.Md5Utils;
import java.io.File;
import java.io.IOException;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/8.
 */
public class FileStorageManager {
  private Context mContext;

  private FileStorageManager() {
  }

  public void init(Context context) {
    this.mContext = context;
  }

  public File getFileByName(String url) {
    if (TextUtils.isEmpty(url)) return null;
    File parent;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      parent = mContext.getExternalCacheDir();
    } else {
      parent = mContext.getCacheDir();
    }
    String fileName = Md5Utils.generateCode(url);
    if (TextUtils.isEmpty(fileName)) return null;
    File file = new File(parent, fileName);
    if (!file.exists()) {
      try {
        boolean newFile = file.createNewFile();
        if (!newFile) return null;
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    return file;
  }

  public static class Holder {
    private static FileStorageManager sInstance = new FileStorageManager();

    public static FileStorageManager getInstance() {
      return sInstance;
    }
  }
}
