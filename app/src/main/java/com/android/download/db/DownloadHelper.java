package com.android.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.android.download.db.dao.DaoMaster;
import com.android.download.db.dao.DownloadEntityDao;
import java.util.List;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/11/11.
 */
public class DownloadHelper {
  //private DaoMaster mDaoMaster;
  //private DaoSession mDaoSession;
  private DownloadEntityDao mDao;

  private DownloadHelper() {
  }

  public void init(Context context) {
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "download.db", null);
    SQLiteDatabase database = helper.getWritableDatabase();
    //mDaoMaster = new DaoMaster(database);
    //mDaoSession = mDaoMaster.newSession();
    mDao = new DaoMaster(database).newSession().getDownloadEntityDao();
  }

  public long insert(DownloadEntity entity) {
    return mDao.insertOrReplace(entity);
  }

  public List<DownloadEntity> queryAll(String url) {
    return mDao.queryBuilder().where(DownloadEntityDao.Properties.DownloadUrl.eq(url))//条件查询
        .orderAsc(DownloadEntityDao.Properties.ThreadId)//排序(升序)
        .list();
  }

  public static class Holder {
    private static DownloadHelper sInstance = new DownloadHelper();

    public static DownloadHelper getInstance() {
      return sInstance;
    }
  }
}
