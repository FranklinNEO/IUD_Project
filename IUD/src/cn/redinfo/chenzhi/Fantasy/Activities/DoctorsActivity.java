package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.redinfo.chenzhi.Fantasy.Adapters.DoctorsAdapter;
import cn.redinfo.chenzhi.Fantasy.DataModle.BarcodeInfo;
import cn.redinfo.chenzhi.Fantasy.DataModle.IDCard;
import cn.redinfo.chenzhi.Fantasy.CustomDialog;
import cn.redinfo.chenzhi.Fantasy.DBHelper;
import cn.redinfo.chenzhi.Fantasy.DrDBHelper;
import cn.redinfo.chenzhi.Fantasy.EditDialog;
import cn.redinfo.chenzhi.Fantasy.OnEditorFinishedListener;
import cn.redinfo.chenzhi.Fantasy.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.gmail.orinchen.utility.Tools;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 13-1-8 Time: 下午1:14
 */
public class DoctorsActivity extends Activity implements OnItemClickListener,
        OnItemLongClickListener {
    public final static String URL = "/data/data/cn.redinfo.chenzhi.Fantasy/databases";
    private static int IDSCAN_REQ_CODE = AddDoctors.class.hashCode();
    SQLiteDatabase db = null;
    public DrDBHelper m_db = null;
    private IDCard idCard = null;
    private ImageButton addButton = null;
    private EditDialog inputDialog = null; // 弹出输入框
    private ArrayList<HashMap<String, String>> doctor = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> doctors = null;
    private CustomAdapter adapter;
    private TextView title;
    private ListView list = null;
    private int pos;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.doctors_layout);
        m_db = DrDBHelper.getInstance(DoctorsActivity.this);
        this.title = (TextView) findViewById(R.id.action_bar_title);
        this.title.setText("站点人员管理");
        this.inputDialog = new EditDialog(this);
        this.addButton = (ImageButton) this
                .findViewById(R.id.action_bar_button);
        this.addButton.setVisibility(View.VISIBLE);
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDoctor();
            }
        });
        list = (ListView) findViewById(R.id.doctor_list);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);

        this.initData();
    }

    private void addDoctor() {
        Intent intent = new Intent();
        intent.setClass(DoctorsActivity.this, AddDoctors.class);
        DoctorsActivity.this.startActivityForResult(intent, IDSCAN_REQ_CODE);
    }

    private void initData() {

        new AsyncTask<Integer, Integer, String[]>() {

            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            protected String[] doInBackground(Integer... params) {
                File file = new File(URL, DrDBHelper.DATABASE_NAME);
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                String sql = "SELECT * FROM dr_data;";
                Cursor cur = db.rawQuery(sql, null);
                doctor = new ArrayList<HashMap<String, String>>();
                if (cur != null && cur.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name",
                                cur.getString(cur.getColumnIndex("drName")));
                        map.put("sex",
                                cur.getString(cur.getColumnIndex("drSex")));
                        map.put("id",
                                cur.getString(cur.getColumnIndex("drIdNum")));

                        doctor.add(map);
                    } while ((cur.moveToNext()));
                    cur.close();
                    db.close();
                } else {
                    cur.close();
                    db.close();
                }
                Collections.reverse(doctor);
                return null;
            }

            protected void onPostExecute(String[] result) {
                super.onPostExecute(result);
                adapter = new CustomAdapter(DoctorsActivity.this);
                list.setAdapter(adapter);
            }
        }.execute(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "OK", Toast.LENGTH_LONG);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == IDSCAN_REQ_CODE) {
            String idStr = data.getStringExtra("id");
            Toast.makeText(this, idStr, Toast.LENGTH_LONG);
            Gson gson = new Gson();
            try {
                idCard = gson.fromJson(idStr, IDCard.class);
            } catch (JsonSyntaxException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
            }
            if (idCard != null) {
                String cardnum = idCard.getCardNumber();
                String gender = idCard.getGenderStr();
                String name = idCard.getName();
                if (check(cardnum)) {
                    m_db.insert_DRINFO(DrDBHelper.DRINFO_TABEL_NAME, name, gender,
                            cardnum);
                }
                initData();
            }
        }
    }

    private boolean check(String contents) {
        if (this.doctor.size() != 0) {
            for (int i = 0; i < this.doctor.size(); i++) {
                if ((this.doctor.get(i).get("id")).equalsIgnoreCase(contents)) {
                    return false;
                }
            }
        }
        return true;

    }

    public final class ViewHolder {
        public TextView nameTv;
        public TextView sexTv;
        public TextView idCodeTv;
    }

    private class CustomAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public CustomAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return doctor.size();
        }

        public Object getItem(int position) {
            return doctor.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.doctor_item, null);
                holder = new ViewHolder();
                holder.nameTv = (TextView) convertView
                        .findViewById(R.id.nameTv);
                holder.sexTv = (TextView) convertView.findViewById(R.id.sexTv);
                holder.idCodeTv = (TextView) convertView
                        .findViewById(R.id.IdTv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.nameTv.setText(doctor.get(position).get("name").trim());
            holder.sexTv.setText("性別:"+doctor.get(position).get("sex"));
            holder.idCodeTv.setText("身份证号:" + doctor.get(position).get("id"));

            return convertView;
        }
    }

    public static void NavToThis(Activity activity) {
        Intent intent = new Intent(activity, DoctorsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        // TODO Auto-generated method stub
        pos = arg2;
        Dialog dialog = null;
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(
                DoctorsActivity.this);
        customBuilder.setMessage("是否要刪除此条医生信息？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        m_db.delete_dr(DrDBHelper.DRINFO_TABEL_NAME, doctor.get(pos).get("id"));
                        initData();
                    }
                });
        dialog = customBuilder.create();
        dialog.show();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }
}