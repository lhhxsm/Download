package com.android.download;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/11.
 */
public class DownloadConfig {
  private static final int MAX_THREAD = 2;//最大线程数
  private static final int LOCAL_PROGRESS_SIZE = 1;
  private int mCoreThreadSize;
  private int mMaxThreadSize;
  private int mLocalProgressThreadSize;

  private DownloadConfig(Builder builder) {
    this.mCoreThreadSize = builder.mCoreThreadSize == 0 ? MAX_THREAD : builder.mCoreThreadSize;
    this.mMaxThreadSize = builder.mMaxThreadSize == 0 ? MAX_THREAD : builder.mMaxThreadSize;
    this.mLocalProgressThreadSize = builder.mLocalProgressThreadSize == 0 ? LOCAL_PROGRESS_SIZE
        : builder.mLocalProgressThreadSize;
  }

  public int getCoreThreadSize() {
    return mCoreThreadSize;
  }

  public int getMaxThreadSize() {
    return mMaxThreadSize;
  }

  public int getLocalProgressThreadSize() {
    return mLocalProgressThreadSize;
  }

  public static class Builder {
    private int mCoreThreadSize;
    private int mMaxThreadSize;
    private int mLocalProgressThreadSize;

    public Builder setCoreThreadSize(int coreThreadSize) {
      this.mCoreThreadSize = coreThreadSize;
      return this;
    }

    public Builder setMaxThreadSize(int maxThreadSize) {
      this.mMaxThreadSize = maxThreadSize;
      return this;
    }

    public Builder setLocalProgressThreadSize(int localProgressThreadSize) {
      this.mLocalProgressThreadSize = localProgressThreadSize;
      return this;
    }

    public DownloadConfig build() {
      return new DownloadConfig(this);
    }
  }
}
