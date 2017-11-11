package com.android.download.utils;

import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/8.
 */
public class Md5Utils {

  public static String generateCode(String url) {
    if (TextUtils.isEmpty(url)) return null;
    try {
      MessageDigest digest = MessageDigest.getInstance("md5");
      digest.update(url.getBytes());
      byte[] cipher = digest.digest();
      StringBuilder sb = new StringBuilder();
      for (byte b : cipher) {
        String hexStr = Integer.toHexString(b & 0xff);
        sb.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }
}
