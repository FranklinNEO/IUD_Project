package com.gmail.orinchen.utility;

import android.content.Context;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-28
 * Time: 上午10:12
 */
public abstract class AdbHelper {
  private String dbPathStr = null;
  private String dbFullNameStr = null;

  protected String getDbFullNameStr() {
    return dbFullNameStr;
  }

  public AdbHelper(Context context, String dbName) {
    String packageName = context.getPackageName();
    dbPathStr = String.format("data/data/%1$s/database", packageName);
    dbFullNameStr = String.format("%1$s/%2$s", dbPathStr, dbName);

    try {
      File dbPath = new File(dbPathStr);
      File dbFullName = new File(dbFullNameStr);
      if (dbFullName.exists())
        return;
      if (!dbPath.exists()) {
        dbPath.mkdir();
      }
      dbFullName.createNewFile();

      InputStream assetsDB = context.getAssets().open(dbName);
      OutputStream dbOut = new FileOutputStream(dbFullNameStr);

      byte[] buffer = new byte[1024];
      int length;
      while ((length = assetsDB.read(buffer)) > 0) {
        dbOut.write(buffer, 0, length);
      }
      dbOut.flush();
      dbOut.close();
      assetsDB.close();
    } catch (IOException ex) {

    }
  }
}
