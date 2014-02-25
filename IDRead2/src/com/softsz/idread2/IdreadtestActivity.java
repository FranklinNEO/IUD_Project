package com.softsz.idread2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import GuoTeng.GtTermb;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fri.idcread.Idcread;
import com.softsz.gongdian.GongdianEngine;

public class IdreadtestActivity extends Activity {
	/** Called when the activity is first created. */

	private TextView xingming;
	private TextView xingbie;
	private TextView minzu;
	private TextView csrq;
	private TextView zhuzhi;
	private TextView sfzh;
	private TextView qfjg;
	private TextView yxqx;
	private TextView tishi;
	private ImageView image;
	private Button readBtn;
	private Idcread reader = new Idcread();
//	ReadThread rt;
	ReadThread rt = new ReadThread();
	private int mobileType = 1;//1为盛本机型PE43，2为盛本PA710，3为西姆通机型
	private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
	private boolean isSuccessed = false;
//	ExecutorService pool = Executors.newFixedThreadPool(2);
	private boolean needExit = false;
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;
//	private GongdianEngine powerControl = new GongdianEngine();
	public int fd = 0;
	public int fd2 = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if(mBTAdapter.getState() != BluetoothAdapter.STATE_OFF) {//此判断代表蓝牙当前为开，反之为关
            mBTAdapter.disable();//关闭蓝牙
        }
		xingming = (TextView) findViewById(R.id.xingming);
		xingbie = (TextView) findViewById(R.id.xingbie);
		minzu = (TextView) findViewById(R.id.minzu);
		csrq = (TextView) findViewById(R.id.csrq);
		zhuzhi = (TextView) findViewById(R.id.zhuzhi);
		sfzh = (TextView) findViewById(R.id.sfzh);
		qfjg = (TextView) findViewById(R.id.qfjg);
		yxqx = (TextView) findViewById(R.id.yxqx);
		image = (ImageView) findViewById(R.id.zp);
		tishi = (TextView) findViewById(R.id.tishi);
//		readBtn = (Button) findViewById(R.id.readCard);
		
		/*  适应国腾的读卡器部分   开始*/
//		fd = powerControl.OpenPort("/dev/ttyHSL0");
//		Toast.makeText(this, "打开设备: " + fd, Toast.LENGTH_LONG).show();
//		int iError = GtTermb.InitComm(fd);
//    	powerControl.SetAttr(fd);
//    	fd2 = powerControl.OpenPort("/sys/devices/platform/msm_serial_hsl.0/uart_switch");
//    	powerControl.Switch(fd2, '0');
    	/*  适应国腾的读卡器部分   结束*/
    	
		if (reader.InitComm("/dev/uart_switch", 1, "/dev/ttyHS0") < 0) {
			if (reader.InitComm("/dev/isl4260e", 0, "/dev/ttyHSL2") < 0) {
				if (reader.InitComm2("/dev/ttyHSL0", '0', "/sys/devices/platform/msm_serial_hsl.0/uart_switch") < 0) {
					finish();
				} else {
					mobileType = 3;
				}
			} else {
				mobileType = 2;
			}
		}
//		if (reader.TestConn() != 1) {
//			finish();
//		} else {
			rt = new ReadThread();
//			pool.execute(rt);
//			rt.setPriority(Thread.MAX_PRIORITY);
			rt.start();
//			wt.start();
//		}
		
//		readBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				readidcard();
//			}
//		});
			
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "goodev");
		wakeLock.acquire();
	}

//	public void readidcard() {
//		if (reader.Authenticate() == 1) {/* 身份证认证 */
//			if (reader.ReadContent() == 1) {/* 读取身份证数据 */
//				String content = getContent();
//				xingming.setText(content.substring(0, 15));
//				String xb = content.substring(15, 16);
//				if (xb.equals("1"))
//					xingbie.setText("男");
//				else
//					xingbie.setText("女");
//				String mz = content.substring(16, 18);
//				minzu.setText(getMZ(mz));
//				csrq.setText(content.substring(18, 26));
//				zhuzhi.setText(content.substring(26, 61));
//				sfzh.setText(content.substring(61, 79));
//				qfjg.setText(content.substring(79, 94));
//				yxqx.setText(content.substring(94, 110));
//				image.setImageBitmap(BitmapFactory.decodeFile("/data/parsebmp/zp.bmp"));
//			}
//		}
//	}
	
	private Handler hd = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tishi.setText("准备就绪");
				break;
			case 2:
				tishi.setVisibility(4);
				String content = getContent();
				xingming.setText(content.substring(0, 15));
				String xb = content.substring(15, 16);
				if (xb.equals("1"))
					xingbie.setText("男");
				else
					xingbie.setText("女");
				String mz = content.substring(16, 18);
				minzu.setText(getMZ(mz));
				csrq.setText(content.substring(18, 26));
				zhuzhi.setText(content.substring(26, 61));
				sfzh.setText(content.substring(61, 79));
				qfjg.setText(content.substring(79, 94));
				yxqx.setText(content.substring(94, 110));
				image.setImageBitmap(BitmapFactory.decodeFile("/data/parsebmp/zp.bmp"));
				if (content.substring(61, 79) != null && content.substring(61, 79).length() > 0)
					isSuccessed = true;
				break;
			}
		};
	};
	
	class ReadThread extends Thread {
		@Override
		public synchronized void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (needExit == false) {
				if (reader.Authenticate() == 1) {/* 身份证认证 */
					if (reader.ReadContent() == 1) {/* 读取身份证数据 */
						ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
						tone.startTone(ToneGenerator.TONE_PROP_BEEP,500);
						tone.release();
						Message m2 = new Message();
						m2.what = 2;
						hd.sendMessage(m2);
					}
				}
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//内容转码
	public String getContent() {
		StringBuilder sb = new StringBuilder();
		InputStream instream=null;
		try {
			instream=new FileInputStream("/data/parsebmp/wzuni.txt");
			if (instream.available()>0)	{
				byte[] buffer=new byte[instream.available()];
				instream.read(buffer);
				sb.append(new String(com.fri.idcread.UnicodeToUTF.UNICODE_TO_UTF8(buffer), "utf-8"));
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
	
	public void setData() {
		Bundle b = new Bundle();
		Intent intent = new Intent();
		b.putInt("test_result",isSuccessed ? 1 : 0);
		intent.putExtras(b);
		setResult(RESULT_OK,intent);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		needExit = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rt.interrupt();
//		wt.interrupt();
//		pool.shutdown();
		if (mobileType == 3) reader.CloseComm2();
		else reader.CloseComm();
//		powerControl.ClosePort(fd2);
//		powerControl.ClosePort(fd);
//		GtTermb.CloseComm();
		wakeLock.release();
		setData();
		this.finish();
//		android.os.Process.killProcess(android.os.Process.myPid());
		super.onBackPressed();
	}

	public String getMZ(String code) {
		if (code.equals("01")) return "汉";
		if (code.equals("02")) return "蒙古";
		if (code.equals("03")) return "回";
		if (code.equals("04")) return "藏";
		if (code.equals("05")) return "维吾尔";
		if (code.equals("06")) return "苗";
		if (code.equals("07")) return "彝";
		if (code.equals("08")) return "壮";
		if (code.equals("09")) return "布依";
		if (code.equals("10")) return "朝鲜";
		
		if (code.equals("11")) return "满";
		if (code.equals("12")) return "侗";
		if (code.equals("13")) return "瑶";
		if (code.equals("14")) return "白";
		if (code.equals("15")) return "土家";
		if (code.equals("16")) return "哈尼";
		if (code.equals("17")) return "哈萨克";
		if (code.equals("18")) return "傣";
		if (code.equals("19")) return "黎";
		if (code.equals("20")) return "傈僳";
		
		if (code.equals("21")) return "佤";
		if (code.equals("22")) return "畲";
		if (code.equals("23")) return "高山";
		if (code.equals("24")) return "拉祜";
		if (code.equals("25")) return "水";
		if (code.equals("26")) return "东乡";
		if (code.equals("27")) return "纳西";
		if (code.equals("28")) return "景颇";
		if (code.equals("29")) return "柯尔克孜";
		if (code.equals("30")) return "土";
		
		if (code.equals("31")) return "达斡尔";
		if (code.equals("32")) return "仫佬";
		if (code.equals("33")) return "羌";
		if (code.equals("34")) return "布朗";
		if (code.equals("35")) return "撒拉";
		if (code.equals("36")) return "毛难";
		if (code.equals("37")) return "仡佬";
		if (code.equals("38")) return "锡伯";
		if (code.equals("39")) return "阿昌";
		if (code.equals("40")) return "普米";
		
		if (code.equals("41")) return "塔吉克";
		if (code.equals("42")) return "怒";
		if (code.equals("43")) return "乌孜别克";
		if (code.equals("44")) return "俄罗斯";
		if (code.equals("45")) return "鄂温克";
		if (code.equals("46")) return "崩龙";
		if (code.equals("47")) return "保安";
		if (code.equals("48")) return "裕固";
		if (code.equals("49")) return "京";
		if (code.equals("50")) return "塔塔尔";
		
		if (code.equals("51")) return "独龙";
		if (code.equals("52")) return "鄂伦春";
		if (code.equals("53")) return "赫哲";
		if (code.equals("54")) return "门巴";
		if (code.equals("55")) return "珞巴";
		if (code.equals("56")) return "基诺";
		
		if (code.equals("97")) return "其他";
		if (code.equals("98")) return "外国血统中国籍";
		
		return "汉";
	}
}