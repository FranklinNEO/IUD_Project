package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.redinfo.chenzhi.Fantasy.CustomDialog;
import cn.redinfo.chenzhi.Fantasy.DrDBHelper;
import cn.redinfo.chenzhi.Fantasy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by NEO on 13-12-25.
 */
public class HistoryActivity extends ListActivity implements AdapterView.OnItemClickListener {
    public final static String URL = "/data/data/cn.redinfo.chenzhi.Fantasy/databases";
    SQLiteDatabase db = null;
    public DrDBHelper m_db = null;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private TextView title = null;
    private ListView list = null;
    private CustomAdapter adapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histoty_layout);
        m_db = DrDBHelper.getInstance(HistoryActivity.this);
        list = getListView();
        title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("放置信息记录");
        list.setOnItemClickListener(this);
        this.initData();
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
                String sql = "SELECT * FROM up_data;";
                Cursor cur = db.rawQuery(sql, null);
                data = new ArrayList<HashMap<String, String>>();
                if (cur != null && cur.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("updata",
                                cur.getString(cur.getColumnIndex("updata")));
                        map.put("state",
                                cur.getString(cur.getColumnIndex("state")));
                        map.put("doctor", cur.getString(cur.getColumnIndex("doctor")));
                        map.put("name", cur.getString(cur.getColumnIndex("name")));
                        map.put("type", cur.getString(cur.getColumnIndex("type")));
                        map.put("createTime", cur.getString(cur.getColumnIndex("createTime")));
                        map.put("cardnum", cur.getString(cur.getColumnIndex("cardnum")));
                        data.add(map);
                    } while ((cur.moveToNext()));
                    cur.close();
                    db.close();
                } else {
                    cur.close();
                    db.close();
                }
                Collections.reverse(data);
                return null;
            }

            protected void onPostExecute(String[] result) {
                super.onPostExecute(result);
                adapter = new CustomAdapter(HistoryActivity.this);
                list.setAdapter(adapter);
            }
        }.execute(0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Dialog dialog = null;
        pos = i;
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(
                HistoryActivity.this);
        customBuilder
                .setTitle("是否需要重新上传数据？")
                .setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (data.get(pos).get("state").equals("1")) {
                                    Toast.makeText(HistoryActivity.this, "文件已上传！", Toast.LENGTH_SHORT).show();
                                } else {
                                    submmit(data.get(pos).get("updata"), pos);
                                }
                                dialog.dismiss();
                            }
                        })
                .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_db.delete_up(DrDBHelper.UPDATA_TABEL_NAME, data.get(pos).get("updata"));
                        initData();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
        dialog = customBuilder.create();
        dialog.show();
    }

    private void submmit(final String updata, final int pos) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", "100");
        params.add("data", updata);

        client.post(ReportActivity.report_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                String responseString = new String(responseBody, Charset.defaultCharset()); //String.valueOf(responseBody);
                Toast.makeText(HistoryActivity.this, "数据上传成功！", Toast.LENGTH_LONG).show();
                update(pos);
            }

            @Override
            public void onFinish() {
                initData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Toast.makeText(HistoryActivity.this, "数据上传失败！请稍后再试！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void update(int pos) {
        m_db.update_data(DrDBHelper.UPDATA_TABEL_NAME,
                data.get(pos).get("updata"), data.get(pos).get("name"),
                data.get(pos).get("cardnum"),
                data.get(pos).get("doctor"), data.get(pos).get("type"), "1");
//        m_db.delete_up(DrDBHelper.UPDATA_TABEL_NAME, data.get(pos).get("updata"));
    }


    public final class ViewHolder {
        public TextView typeTv;
        public TextView dateTv;
        public TextView stateTv;
        public TextView drNameTv;
        public TextView idCodeTv;
    }

    private class CustomAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public CustomAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return data.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.history_item, null);
                holder = new ViewHolder();
                holder.typeTv = (TextView) convertView
                        .findViewById(R.id.typeTv);
                holder.idCodeTv = (TextView) convertView.findViewById(R.id.IdCardTv);
                holder.drNameTv = (TextView) convertView.findViewById(R.id.DoctorInfoTv);
                holder.dateTv = (TextView) convertView.findViewById(R.id.createTimeTv);
                holder.stateTv = (TextView) convertView.findViewById(R.id.dataState);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (data.get(position).get("type").equals("1")) {
                holder.typeTv.setText("取出");
            } else if (data.get(position).get("type").equals("0")) {
                holder.typeTv.setText("放入");
            }
            if (data.get(position).get("state").equals("1")) {
                holder.stateTv.setText("已上传");
            } else if (data.get(position).get("state").equals("0")) {
                holder.stateTv.setText("未上传");
            }
            holder.drNameTv.setText(data.get(position).get("cardnum"));
            holder.dateTv.setText(data.get(position).get("createTime"));
            holder.idCodeTv.setText(data.get(position).get("name"));
            return convertView;
        }
    }

    public static void NavToThis(Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        activity.startActivity(intent);
    }
}
