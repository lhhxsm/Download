package com.android.download.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/11.
 */
@Entity(nameInDb = "DownloadDb") public class DownloadEntity {
  @Id(autoincrement = true) @Property(nameInDb = "_Id") private Long _Id;
  @Property(nameInDb = "startPosition") private long startPosition;
  @Property(nameInDb = "endPosition") private long endPosition;
  @Property(nameInDb = "progressPosition") private long progressPosition;
  @Property(nameInDb = "downloadUrl") private String downloadUrl;
  @Property(nameInDb = "threadId") private int threadId;

  @Generated(hash = 558679168)
  public DownloadEntity(Long _Id, long startPosition, long endPosition, long progressPosition,
      String downloadUrl, int threadId) {
    this._Id = _Id;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.progressPosition = progressPosition;
    this.downloadUrl = downloadUrl;
    this.threadId = threadId;
  }

  @Generated(hash = 1671715506) public DownloadEntity() {
  }

  public Long get_Id() {
    return this._Id;
  }

  public void set_Id(Long _Id) {
    this._Id = _Id;
  }

  public long getStartPosition() {
    return this.startPosition;
  }

  public void setStartPosition(long startPosition) {
    this.startPosition = startPosition;
  }

  public long getEndPosition() {
    return this.endPosition;
  }

  public void setEndPosition(long endPosition) {
    this.endPosition = endPosition;
  }

  public long getProgressPosition() {
    return this.progressPosition;
  }

  public void setProgressPosition(long progressPosition) {
    this.progressPosition = progressPosition;
  }

  public String getDownloadUrl() {
    return this.downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public int getThreadId() {
    return this.threadId;
  }

  public void setThreadId(int threadId) {
    this.threadId = threadId;
  }
}
