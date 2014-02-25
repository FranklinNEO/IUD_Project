package cn.redinfo.chenzhi.Fantasy.Activities;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.redinfo.chenzhi.Fantasy.DataModle.IDCard;
import cn.redinfo.chenzhi.Fantasy.R;
import com.fri.idcread.Idcread;
import com.gmail.orinchen.utility.Tools;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NEO on 13-12-21.
 */
public class AddDoctors extends Activity {
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
    private Idcread reader = new Idcread();
    //	ReadThread rt;
    private IDCard currentCard;
    ReadThread rt = new ReadThread();
    private int mobileType = 1;//1为盛本机型PE43，2为盛本PA710，3为西姆通机型
    private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isSuccessed = false;
    //	ExecutorService pool = Executors.newFixedThreadPool(2);
    private boolean needExit = false;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_add_doctors);
        if (mBTAdapter.getState() != BluetoothAdapter.STATE_OFF) {//此判断代表蓝牙当前为开，反之为关
            mBTAdapter.disable();//关闭蓝牙
        }
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
        rt = new ReadThread();
        rt.start();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "goodev");
        wakeLock.acquire();
    }

    private Handler hd = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    txt_waring.setText("准备就绪");
                    Toast.makeText(AddDoctors.this, "准备就绪", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //get idcard
                    String content = getContent();
                    //new idcard object
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
                    currentCard = card;
                    //set view
                    txt_Name.setText(content.substring(0, 15));
                    if (xb.equals("1"))
                        txt_Gender.setText("男");
                    else
                        txt_Gender.setText("女");
                    txt_Nation.setText(getMZ(mz));
                    txt_Birthday.setText(content.substring(18, 22) + "年" + content.substring(22, 24) + "月" + content.substring(24, 26) + "日");
                    txt_Address.setText(content.substring(26, 61));
                    txt_CardNumber.setText(content.substring(61, 79));
                    txt_IssuingAuthority.setText(content.substring(79, 94));
                    txt_ValidityDate.setText(content.substring(94, 110));
                    img_Photo.setImageBitmap(BitmapFactory.decodeFile("/data/parsebmp/zp.bmp"));
                    if (content.substring(61, 79) != null && content.substring(61, 79).length() > 0)
                        isSuccessed = true;
                    break;
            }
        }

        ;
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
                        tone.startTone(ToneGenerator.TONE_PROP_BEEP, 500);
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

    public void onClick(View view) {
        Gson gson = new Gson();
        String jsonStr = "";
        if (currentCard != null) {
            jsonStr = gson.toJson(currentCard);
            Intent intent = new Intent();
            intent.putExtra("id", jsonStr);
            this.setResult(RESULT_OK, intent);
            Toast.makeText(AddDoctors.this, "正在保存信息,请稍后……", Toast.LENGTH_SHORT).show();
            needExit = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rt.interrupt();

            if (mobileType == 3) reader.CloseComm2();
            else reader.CloseComm();

            wakeLock.release();

            this.finish();
        } else {
            Toast.makeText(AddDoctors.this, "正在返回,请稍后……", Toast.LENGTH_SHORT).show();
            needExit = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rt.interrupt();

            if (mobileType == 3) reader.CloseComm2();
            else reader.CloseComm();

            wakeLock.release();

            this.finish();
        }
    }

    //内容转码
    public String getContent() {
        StringBuilder sb = new StringBuilder();
        InputStream instream = null;
        try {
            instream = new FileInputStream("/data/parsebmp/wzuni.txt");
            if (instream.available() > 0) {
                byte[] buffer = new byte[instream.available()];
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
        b.putInt("test_result", isSuccessed ? 1 : 0);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Toast.makeText(AddDoctors.this, "请稍后……", Toast.LENGTH_SHORT).show();
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
     //   wakeLock.release();
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
