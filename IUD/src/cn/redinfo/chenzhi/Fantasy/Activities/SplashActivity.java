package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.redinfo.chenzhi.Fantasy.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);

		new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainMenuActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}

		}.start();
		// PermissionCheck();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x969 && resultCode == RESULT_OK) {
			saveUserPermission();
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, MainMenuActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	private void PermissionCheck() {
		// TODO Auto-generated method stub
		FileInputStream inStream = null;
		ByteArrayOutputStream outStream = null;
		try {

			inStream = this.openFileInput("permission.txt");
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}
			String content = outStream.toString();

			outStream.close();
			inStream.close();
			if (content.equals("pass")) {
				Log.e("permission", "pass");
				Toast.makeText(SplashActivity.this, "已授权", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainMenuActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				Log.e("permission", "fail");
				Toast.makeText(SplashActivity.this, "未注册", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, PermissionActivity.class);
				startActivityForResult(intent, 0x969);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("permission", "fail");
			Toast.makeText(SplashActivity.this, "未注册", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, PermissionActivity.class);
			startActivityForResult(intent, 0x969);
		}
	}

	public void saveUserPermission() {
		try {
			FileOutputStream outStream = this.openFileOutput("permission.txt",
					Context.MODE_PRIVATE);
			String content = "pass";
			outStream.write(content.getBytes());
			outStream.close();
		} catch (IOException ex) {

		}
	}

}
