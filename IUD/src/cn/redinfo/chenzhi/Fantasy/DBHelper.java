package cn.redinfo.chenzhi.Fantasy;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.redinfo.chenzhi.Fantasy.DataModle.AreaInfo;
import cn.redinfo.chenzhi.Fantasy.DataModle.Sites;
import cn.redinfo.chenzhi.Fantasy.DataModle.Station;
import cn.redinfo.chenzhi.Fantasy.DataModle.Terminal;
import com.gmail.orinchen.utility.AdbHelper;

import java.util.ArrayList;

public final class DBHelper extends AdbHelper {

  String dbFullNameStr = "";

  public DBHelper(Context context) {
    super(context, "fantasy_db.db");
    dbFullNameStr = this.getDbFullNameStr();
  }

  // 获得所有省信息
  public ArrayList<AreaInfo> getProvince() {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return null;
    }

    Cursor reader = db.rawQuery("select * from `areas` where length(`areas`.`code`)=2",
      null);
    if (reader == null) {
      return null;
    }

    ArrayList<AreaInfo> result = null;
    while (reader.moveToNext()) {

      if (result == null) {
        result = new ArrayList<AreaInfo>();
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setName("请选择...");
        areaInfo.setCode("000000");
        result.add(areaInfo);
      }

      AreaInfo areaInfo = new AreaInfo();
      areaInfo.setCode(reader.getString(reader.getColumnIndex("code")));
      areaInfo.setName(reader.getString(reader.getColumnIndex("name")));
      result.add(areaInfo);
    }

    reader.close();
    db.close();
    return result;
  }

  // 获得所有市信息
  public ArrayList<AreaInfo> getCityByProvinceCode(String code) {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return null;
    }

    String selectStr = "select * from `areas` where length(`areas`.`code`)=4" +
      " and substr(`areas`.`code`,1,2)=?";

    Cursor reader = db.rawQuery(selectStr, new String[]{code});
    if (reader == null) {
      return null;
    }

    ArrayList<AreaInfo> result = null;
    while (reader.moveToNext()) {
      if (result == null) {
        result = new ArrayList<AreaInfo>();
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setName("请选择...");
        areaInfo.setCode("000000");
        result.add(areaInfo);
      }

      AreaInfo areaInfo = new AreaInfo();
      areaInfo.setCode(reader.getString(reader.getColumnIndex("code")));
      areaInfo.setName(reader.getString(reader.getColumnIndex("name")));
      result.add(areaInfo);
    }

    reader.close();
    db.close();
    return result;
  }

  // 获得所有县信息
  public ArrayList<AreaInfo> getCountyByCityCode(String code) {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return null;
    }

    String selectStr = "select * from `areas` where length(`areas`.`code`)=6 " +
      " and substr(`areas`.`code`,1,4)=?";

    Cursor reader = db.rawQuery(selectStr, new String[]{code});
    if (reader == null) {
      return null;
    }

    ArrayList<AreaInfo> result = null;
    while (reader.moveToNext()) {
      if (result == null) {
        result = new ArrayList<AreaInfo>();
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setName("请选择...");
        areaInfo.setCode("000000");
        result.add(areaInfo);
      }

      AreaInfo areaInfo = new AreaInfo();
      areaInfo.setCode(reader.getString(reader.getColumnIndex("code")));
      areaInfo.setName(reader.getString(reader.getColumnIndex("name")));
      result.add(areaInfo);
    }

    reader.close();
    db.close();
    return result;
  }

  public boolean putStation(Station station) {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return false;
    }

    String insertStr = "REPLACE INTO `station` VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    Sites st = station.getSite();
    Terminal tr = station.getTerminal();

    try {

      db.execSQL(insertStr, new String[]{
        st.getSitesName(),
        st.getAddress(),
        st.getCharge(),
        st.getTelephone(),
        st.getArea(),
        st.getAreaName(),
        tr.getUuid(),
        tr.getLat(),
        tr.getLng()
      });
    } catch (SQLException ex) {
      return false;
    }
    return true;
  }

  public Station getStation() {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return null;
    }

    String selectStr = "select * from `station` where `id`='1'";

    Cursor reader = db.rawQuery(selectStr, null);
    if (reader == null || !reader.moveToNext()) {
      return null;
    }

    Sites site = new Sites();

    site.setAddress(reader.getString(reader.getColumnIndex("site_address")));
    site.setArea(reader.getString(reader.getColumnIndex("site_area")));
    site.setCharge(reader.getString(reader.getColumnIndex("site_charge")));
    site.setSitesName(reader.getString(reader.getColumnIndex("site_name")));
    site.setTelephone(reader.getString(reader.getColumnIndex("site_phone")));
    site.setAreaName(reader.getString(reader.getColumnIndex("site_area_name")));

    Terminal terminal = new Terminal();
    terminal.setAddress(site.getAddress());
    terminal.setCharge(site.getCharge());
    terminal.setLat(reader.getString(reader.getColumnIndex("ter_lat")));
    terminal.setLng(reader.getString(reader.getColumnIndex("ter_lng")));
    terminal.setUuid(reader.getString(reader.getColumnIndex("ter_uuid")));
    terminal.setSiteName(site.getSitesName());

    reader.close();
    db.close();

    Station is = new Station();
    is.setSite(site);
    is.setTerminal(terminal);

    return is;
  }

  public ArrayList<String> getDoctors() {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return null;
    }

    String selectStr = "select * from `doctors`";

    Cursor reader = db.rawQuery(selectStr, null);
    if (reader == null) {
      return null;
    }

    ArrayList<String> result = null;
    while (reader.moveToNext()) {
      if (result == null) {
        result = new ArrayList<String>();
      }

      String name = reader.getString(reader.getColumnIndex("name"));
      result.add(name);
    }

    reader.close();
    db.close();
    return result;
  }

  public boolean putDoctor(String name) {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return false;
    }

    String insertStr = "INSERT INTO `doctors` VALUES (?)";

    try {
      db.execSQL(insertStr, new String[]{name});
    } catch (SQLException ex) {
      return false;
    }
    return true;
  }

  public boolean delDoctor(String name) {
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFullNameStr, null);

    if (db == null) {
      return false;
    }

    String insertStr = "DELETE FROM `doctors` Where `name` = ?";

    try {
      db.execSQL(insertStr, new String[]{name});
    } catch (SQLException ex) {
      return false;
    }
    return true;
  }
}