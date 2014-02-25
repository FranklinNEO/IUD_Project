package cn.redinfo.chenzhi.Fantasy.Activities;

import java.lang.reflect.Method;
import java.security.interfaces.RSAPublicKey;

import com.gmail.orinchen.utility.RSAUtils;

import cn.redinfo.chenzhi.Fantasy.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PermissionActivity extends Activity implements OnClickListener {
	private TextView tv = null;
	private EditText et = null;
	private Button btn = null;
	private String imei = null;
	private String SerialNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.permission_dialog);
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		// imei = tm.getDeviceId();
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			imei = (String) (get.invoke(c, "ro.serialno", "unknown"));
			Log.e("serialno", imei);
		} catch (Exception ignored) {
		}
		tv = (TextView) findViewById(R.id.imei_tv);
		tv.setText("设备号:\n" + imei);
		et = (EditText) findViewById(R.id.permission_et);
		btn = (Button) findViewById(R.id.permission_btn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.permission_btn:
			String ming = et.getText().toString().trim();
			String modulus = "90594178989655967097569383394790175138748497919049392137005659395147344637272492474854372610837957872953443115970456674334225319610045188749679277077180303609401285751239185591369591824400041552134620906865815584767711067033533313951045347371893098334962377244511317329251606380250512249489707989601869323383";
			String public_exponent = "65537";
			RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus,
					public_exponent);
			try {
				String mi = RSAUtils.encryptByPublicKey(imei, pubKey)
						.substring(0, 11);
				if (mi.equals(ming)) {
					Toast.makeText(PermissionActivity.this, "已授权",
							Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					this.finish();
				} else {
					Toast.makeText(PermissionActivity.this, "序列码错误",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}
}
