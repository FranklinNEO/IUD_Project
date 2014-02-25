package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import cn.redinfo.chenzhi.Fantasy.R;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NEO on 13-12-30.
 */
public class System extends Activity {
    private String verson;
    private String svno;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("设备信息");
        String pkName = this.getPackageName();
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            svno = (String) (get.invoke(c, "ro.serialno", "unknown"));
            Log.e("serialno", svno);
        } catch (Exception ignored) {
        }
        try {
            verson = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TextView sn = (TextView) findViewById(R.id.textView2);
        sn.setText("设备号:" + svno);
        TextView link = (TextView) findViewById(R.id.linkTv);
        link.setText(Html
                .fromHtml("<a href=\"http://www.redinfo.com\">http://www.redinfo.cn</a>"));
        link.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tvVerson = (TextView) findViewById(R.id.vesion);
        tvVerson.setText("V" + verson.substring(0, 3));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(new Date()));
        TextView copyTv = (TextView) findViewById(R.id.copyright);
        copyTv.setText("Copyright © " + (year - 1) + "-" + year + " REDINFO.");

    }

    public static void NavToThis(Activity activity) {
        Intent intent = new Intent(activity, System.class);
        activity.startActivity(intent);
    }
}
