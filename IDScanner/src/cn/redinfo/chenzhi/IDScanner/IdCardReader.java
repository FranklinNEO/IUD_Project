package cn.redinfo.chenzhi.IDScanner;

import android.bluetooth.BluetoothAdapter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Message;
import com.fri.idcread.Idcread;
import com.gmail.orinchen.utility.Tools;
import com.gmail.orinchen.utility.UnicodeToUTF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 12-12-17 Time: 上午10:16
 */
public class IdCardReader {
	private Idcread reader = new Idcread();

	ReadThread rt = null;
	WaitThread wt = null;

	private int mobileType = 1;// 1为盛本机型，2为西姆通机型
	private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();

	private CardReaderListener listener = null;

	private void onError() {
		if (this.listener != null)
			this.listener.onError();
	}

	private void onReady() {
		if (this.listener != null)
			this.listener.onReady();
	}

	private void onCardReaded(IDCard card) {
		if (this.listener != null)
			this.listener.onCardReaded(card);
	}

	public void setListener(CardReaderListener listener) {
		this.listener = listener;
	}

	public void open() {

		if (mBTAdapter.getState() != BluetoothAdapter.STATE_OFF) {// 此判断代表蓝牙当前为开，反之为关
			mBTAdapter.disable();// 关闭蓝牙
		}

		if (this.reader.TestConn() != 1) {
			if (reader.InitComm("/dev/uart_switch", 1, "/dev/ttyHS0") == 0) {
				reader.InitComm("/dev/exar1170", 0, "/dev/ttyHSL0");
				mobileType = 2;
			}
		}

		if (reader.TestConn() != 1) {
			this.onError();
		} else {
			if (this.rt == null)
				this.rt = new ReadThread();

			if (this.wt == null)
				this.wt = new WaitThread();

			this.rt.start();
			this.wt.start();
		}
	}

	private Handler hd = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				IdCardReader.this.onReady();
				break;
			case 2:
				String content = getContent();
				IDCard card = new IDCard();
				card.setName(content.substring(0, 15));
				String xb = content.substring(15, 16);
				card.setGender(xb.equals("1") ? IDCard.Gender.Male
						: IDCard.Gender.Female);
				String mz = content.substring(16, 18);
				card.setNationality(Integer.parseInt(mz));

				card.setBirthday(Tools.DateFromString(
						content.substring(18, 26), "yyyyMMdd"));

				card.setAddress(content.substring(26, 61));
				card.setCardNumber(content.substring(61, 79));
				card.setIssuingAuthority(content.substring(79, 94));
				String yxq = content.substring(94, 110);

				card.setValidityDateFrom(Tools.DateFromString(
						yxq.substring(0, 8), "yyyyMMdd"));
				card.setValidityDateEnd(Tools.DateFromString(yxq.substring(8),
						"yyyyMMdd"));
				IdCardReader.this.onCardReaded(card);
				break;
			}
		}

		;
	};

	class ReadThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (reader.Authenticate() == 1) {/* 身份证认证 */
					if (reader.ReadContent() == 1) {/* 读取身份证数据 */
						ToneGenerator tone = new ToneGenerator(
								AudioManager.STREAM_MUSIC,
								ToneGenerator.MAX_VOLUME);
						tone.startTone(ToneGenerator.TONE_PROP_BEEP, 500);
						Message msg = new Message();
						msg.what = 2;
						hd.sendMessage(msg);
					}
				}
			}
		}
	}

	private class WaitThread extends Thread {
		@Override
		public void run() {
			try {
				if (mobileType == 1)
					sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = 1;
			hd.sendMessage(msg);
		}
	}

	// 内容转码
	public String getContent() {
		StringBuilder sb = new StringBuilder();
		InputStream instream = null;
		try {
			instream = new FileInputStream("/data/parsebmp/wzuni.txt");
			if (instream.available() > 0) {
				byte[] buffer = new byte[instream.available()];
				instream.read(buffer);
				sb.append(new String(UnicodeToUTF.UNICODE_TO_UTF8(buffer),
						"utf-8"));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void close() {
		if (this.rt != null) {
			this.rt.interrupt();
		}

		if (this.wt != null) {
			this.wt.interrupt();
		}

		if (this.reader != null) {
			this.reader.CloseComm();
		}
		this.rt = null;
		this.wt = null;
	}

}
