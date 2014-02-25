package cn.redinfo.chenzhi.BarcodeScanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BarcodeActivity extends Activity implements OnClickListener {

	private BarcodeReader barcodeReader = new BarcodeReader();
	private Button btn_cancel = null;
	private Button btn_ok = null;
	private Button btn_scan = null;
	private TextView txt_barcode = null;
	private TextView txt_batchNumber = null;
	private TextView txt_varietyId = null;
	private TextView txt_varietyName = null;
	private TextView txt_enterpriseId = null;
	private TextView txt_enterpriseName = null;
	private DrugProduct drugInfo = null;
	public Dialog querydialog = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.txt_barcode = (TextView) this.findViewById(R.id.txt_barcode);
		this.txt_batchNumber = (TextView) this
				.findViewById(R.id.txt_batchNumber);
		this.txt_enterpriseId = (TextView) this
				.findViewById(R.id.txt_enterpriseId);
		this.txt_enterpriseName = (TextView) this
				.findViewById(R.id.txt_enterpriseName);
		this.txt_varietyId = (TextView) this.findViewById(R.id.txt_varietyId);
		this.txt_varietyName = (TextView) this
				.findViewById(R.id.txt_varietyName);
		this.btn_ok = (Button) this.findViewById(R.id.btn_ok);
		this.btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		this.btn_scan = (Button) this.findViewById(R.id.btn_scan);
		this.btn_ok.setOnClickListener(this);
		this.btn_cancel.setOnClickListener(this);
		this.btn_scan.setOnClickListener(this);
		querydialog = new Dialog(BarcodeActivity.this, R.style.mmdialog);
		querydialog.setContentView(R.layout.query_dialog);
		this.barcodeReader.setListener(new BarcodeReaderListener() {
			@Override
			public void onBarcodeReaded(String barcode) {
				txt_barcode.setText(barcode);
				searchProduct(barcode);
			}
		});

		this.barcodeReader.open();
	}

	public void searchProduct(final String barcod) {
		String SEARCH_RESULT = "http://www.youkang800.net/r/share/get_barcode_data.html";
		RequestParams params = new RequestParams();
		params.put("barcode", barcod);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(SEARCH_RESULT, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				querydialog.show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				querydialog.dismiss();
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					Gson gson = new Gson();
					DrugProduct dp = gson.fromJson(content, DrugProduct.class);
					drugInfo = dp;
					txt_barcode.setText(barcod);
					txt_varietyName.setText(dp.getVariety_name());
					txt_varietyId.setText(dp.getVariety_id());
					txt_enterpriseId.setText(dp.getEnterprise_id());
					txt_enterpriseName.setText(dp.getEnterprise_name());
					txt_batchNumber.setText(dp.getBatch_number());
				} catch (Exception e) {
					Toast.makeText(BarcodeActivity.this, "查找失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				super.onFailure(error);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(BarcodeActivity.this, "发生网络错误，请稍后再试试",
						Toast.LENGTH_LONG).show();
			}

		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.barcodeReader.close();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.barcodeReader.close();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.barcodeReader.close();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.barcodeReader.close();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_cancel:
			BarcodeActivity.this.finish();
			break;
		case R.id.btn_ok:
			Intent intent = new Intent();
			// 设置要返回的属性值
			Gson gson = new Gson();
			BarcodeInfo info = new BarcodeInfo();
			if ((txt_barcode.getText().toString().trim().equals(""))
					|| (txt_barcode.getText().toString().trim().equals(null))
					|| drugInfo == null) {
				Toast.makeText(BarcodeActivity.this, "请完善信息",
						Toast.LENGTH_SHORT).show();
			} else {
				info.setBarcode(txt_barcode.getText().toString());
				info.setProdName(drugInfo.getVariety_name());
				info.setProducers(drugInfo.getEnterprise_name());
				info.setProdId(drugInfo.getVariety_id());
				info.setProducersId(drugInfo.getEnterprise_id());
				info.setBatchcode(drugInfo.getBatch_number());
				String result = gson.toJson(info);
				intent.putExtra("bc", result);
				// 设置返回码和Intent对象
				BarcodeActivity.this.setResult(RESULT_OK, intent);
				// 关闭Activity
				BarcodeActivity.this.finish();
			}

			break;
		case R.id.btn_scan:
			barcodeReader.scan();
			break;
		default:
			break;
		}
	}
}