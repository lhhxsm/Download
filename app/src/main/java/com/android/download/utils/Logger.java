package com.android.download.utils;

import android.util.Log;
import java.util.Locale;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/8.
 */
public class Logger {
  public static final boolean DEBUG = true;

  public static void d(String tag, String message) {
    if (DEBUG) {
      Log.d(tag, message);
    }
  }

  public static void d(String tag, String message, Object... args) {
    if (DEBUG) {
      Log.d(tag, String.format(Locale.getDefault(), message, args));
    }
  }

  public static void e(String tag, String message) {
    if (DEBUG) {
      Log.e(tag, message);
    }
  }

  public static void i(String tag, String message) {
    if (DEBUG) {
      Log.i(tag, message);
    }
  }

  public static void w(String tag, String message) {
    if (DEBUG) {
      Log.w(tag, message);
    }
  }
}
