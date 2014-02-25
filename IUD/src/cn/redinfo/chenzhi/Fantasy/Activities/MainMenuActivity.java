package cn.redinfo.chenzhi.Fantasy.Activities;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.redinfo.chenzhi.Fantasy.FantasyApplication;
import cn.redinfo.chenzhi.Fantasy.NetHelper;
import cn.redinfo.chenzhi.Fantasy.R;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 13-1-8 Time: 上午10:22
 */
public class MainMenuActivity extends Activity implements View.OnClickListener {
	private String svno;
	private TextView InfoTxt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_layout);
		// getActionBar().setBackgroundDrawable(
		// this.getBaseContext().getResources()
		// .getDrawable(R.drawable.BackBar));
		// getActionBar().show();
		// Dashboard News feed button
		Button btn_newsfeed = (Button) findViewById(R.id.dsh_btn_news_feed);
		btn_newsfeed.setOnClickListener(this);

		// Dashboard Friends button
		Button btn_friends = (Button) findViewById(R.id.dsh_btn_friends);
		btn_friends.setOnClickListener(this);

		// Dashboard Messages button
		Button btn_messages = (Button) findViewById(R.id.dsh_btn_messages);
		btn_messages.setOnClickListener(this);

		// Dashboard Places button
		Button btn_places = (Button) findViewById(R.id.dsh_btn_places);
		btn_places.setOnClickListener(this);

		// Dashboard Events button
		Button btn_events = (Button) findViewById(R.id.dsh_btn_events);
		btn_events.setOnClickListener(this);

		// Dashboard Photos button
		Button btn_photos = (Button) findViewById(R.id.dsh_btn_photos);
		btn_photos.setOnClickListener(this);

		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			svno = (String) (get.invoke(c, "ro.serialno", "unknown"));
			Log.e("serialno", svno);
		} catch (Exception ignored) {
		}
		InfoTxt = (TextView) findViewById(R.id.infoTxt);
		InfoTxt.setText("设备号:" + svno);
		((FantasyApplication) this.getApplication()).setDeviceId(svno);
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.dsh_btn_news_feed:
			//StationSettingsActivity.NavToThis(this);
            System.NavToThis(this);
			break;

		case R.id.dsh_btn_messages:
            if(NetHelper.IsHaveInternet(MainMenuActivity.this)){
                ReportActivity.NavToThis(this);
            }else{
                Toast.makeText(MainMenuActivity.this, "网络连接失败，请稍后再试试",
                        Toast.LENGTH_LONG).show();
            }
            break;

		case R.id.dsh_btn_friends:
			DoctorsActivity.NavToThis(this);
			break;

		case R.id.dsh_btn_events:
			//SearchAcivity.NavToThis(this);
            HistoryActivity.NavToThis(this);
			break;
		}
	}
}
