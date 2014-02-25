package cn.redinfo.chenzhi.Fantasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

public class DrDBHelper extends SQLiteOpenHelper {

    public static DrDBHelper mInstance = null;

    /**
     * 数据库名称 *
     */
    public static final String DATABASE_NAME = "dr_info.db";

    /**
     * 数据库版本号 *
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * DB对象 *
     */
    public SQLiteDatabase mDb = null;

    Context mContext = null;
    public final static String DRINFO_TABEL_NAME = "dr_data";
    public final static String UPDATA_TABEL_NAME = "up_data";

    /**
     * 数据库SQL语句 创建表 *
     */
    public static final String DRINFO_TABLE_CREATE = "create table dr_data("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "drName TEXT NOT NULL," + "drSex TEXT NOT NULL,"
            + "drIdNum TEXT NOT NULL);";
    public static final String UPDATA_TABLE_CREATE = "create table up_data("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "updata TEXT NOT NULL," + "name TEXT NOT NULL," + "cardnum TEXT NOT NULL," + "doctor TEXT NOT NULL,"
            + "type TEXT NOT NULL," + "createTime TEXT NOT NULL,"
            + "state TEXT NOT NULL);";

    /**
     * 单例模式 *
     */
    public static synchronized DrDBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DrDBHelper(context);
        }
        return mInstance;
    }

    public DrDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // 得到数据库对象
        mDb = getReadableDatabase();
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库
        db.execSQL(DRINFO_TABLE_CREATE);
        db.execSQL(UPDATA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert_DRINFO(String tablename, String drName, String drSex,
                              String drNum) {
        ContentValues values = new ContentValues();
        values.put("drName", drName);
        values.put("drSex", drSex);
        values.put("drIdNum", drNum);
        mDb.insert(tablename, null, values);
        values.clear();
    }

    public void delete_dr(String tablename, String drNum) {
        mDb.delete(tablename, "drIdNum='" + drNum + "';", null);
    }

    public void insert_UPINFO(String tablename, String updata, String name, String cardnum, String doctor, String type, String state) {
        ContentValues values = new ContentValues();
        values.put("updata", updata);
        values.put("name", name);
        values.put("state", state);
        values.put("doctor", doctor);
        values.put("cardnum", cardnum);
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd");
        String CodeDate = df.format(new java.util.Date());
        values.put("createTime", CodeDate);
        values.put("type", type);
        mDb.insert(tablename, null, values);
        values.clear();
    }

    public void delete_up(String tablename, String updata) {
        mDb.delete(tablename, "updata='" + updata + "';", null);
    }

    public void update_data(String tablename, String updata, String name, String cardnum, String doctor, String type, String state)

    {
        ContentValues values = new ContentValues();
        values.put("updata", updata);
        values.put("name", name);
        values.put("state", state);
        values.put("doctor", doctor);
        values.put("cardnum", cardnum);
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd");
        String CodeDate = df.format(new java.util.Date());
        values.put("createTime", CodeDate);
        values.put("type", type);
        mDb.update(tablename, values, "updata='" + updata + "';", null);
    }

    public void delete_table(String tablename) {
        mDb.delete(tablename, null, null);
    }

}
