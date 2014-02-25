package cn.redinfo.chenzhi.IDScanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.orinchen.utility.Tools;
import com.google.gson.Gson;

public class MainActivity extends Activity {
	private TextView txt_Name = null;
    private TextView txt_Gender = null;
    private TextView txt_Nation = null;
    private TextView txt_Birthday = null;
    private TextView txt_CardNumber = null;
    private TextView txt_Address = null;
    private TextView txt_IssuingAuthority = null;
    private TextView txt_ValidityDate = null;
    private ImageView img_Photo = null;

    private TextView txt_waring = null;

    private IdCardReader reader = null;

    private IDCard currentCard = null;
	public Dialog querydialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.txt_Birthday = (TextView) this.findViewById(R.id.text_Birthday);
		this.txt_CardNumber = (TextView) this
				.findViewById(R.id.text_CardNumber);
		this.txt_Gender = (TextView) this.findViewById(R.id.text_Gender);
		this.txt_Name = (TextView) this.findViewById(R.id.text_Name);
		this.txt_Nation = (TextView) this.findViewById(R.id.text_Nation);
		this.txt_Address = (TextView) this.findViewById(R.id.text_Address);
		this.txt_IssuingAuthority = (TextView) this
				.findViewById(R.id.text_IssuingAuthority);
		this.txt_ValidityDate = (TextView) this
				.findViewById(R.id.text_ValidityDate);
		this.txt_waring = (TextView) this.findViewById(R.id.txt_waring);
		txt_waring.setVisibility(View.INVISIBLE);
		this.txt_waring.setText("正在加载设备驱动请稍后...");
		this.img_Photo = (ImageView) this.findViewById(R.id.photoImage);
		querydialog = new Dialog(MainActivity.this, R.style.mmdialog);
		querydialog.setContentView(R.layout.query_dialog);
		this.reader = new IdCardReader();
		this.reader.setListener(new CardReaderListener() {

			@Override
			public void onReady() {
				// txt_waring.setText("准备就绪！");
				querydialog.dismiss();
				Toast.makeText(MainActivity.this, "准备就绪！", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onError() {
				// txt_waring.setText("加载设备驱动失败！");
				querydialog.dismiss();
				Toast.makeText(MainActivity.this, "加载设备驱动失败！",
						Toast.LENGTH_SHORT);
			}

			@Override
			public void onCardReaded(IDCard idCard) {
				currentCard = idCard;
				txt_Gender.setText(idCard.getGenderStr());
				txt_Name.setText(idCard.getName());
				txt_Nation.setText(idCard.getNationalityName());
				txt_CardNumber.setText(idCard.getCardNumber());
				txt_Birthday.setText(Tools.DateToString(idCard.getBirthday(),
						"yyyy-MM-dd"));
				txt_Address.setText(idCard.getAddress());
				txt_IssuingAuthority.setText(idCard.getIssuingAuthority());

				String vd = Tools.DateToString(idCard.getValidityDateFrom(),
						"yyyy-MM-dd")
						+ "至"
						+ Tools.DateToString(idCard.getValidityDateFrom(),
								"yyyy-MM-dd");

				txt_ValidityDate.setText(vd);
				img_Photo.setImageBitmap(BitmapFactory
						.decodeFile("/data/parsebmp/zp.bmp"));
			}
		});
		querydialog.show();
		this.reader.open();
	}

	public void onClick(View view) {
        Gson gson = new Gson();
        String jsonStr = "";
        if (currentCard != null) {
            jsonStr = gson.toJson(currentCard);
            Intent intent = new Intent();
            intent.putExtra("id", jsonStr);
            this.setResult(RESULT_OK, intent);
            this.finish();
        } else {
            this.finish();
        }
    }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.reader.close();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
