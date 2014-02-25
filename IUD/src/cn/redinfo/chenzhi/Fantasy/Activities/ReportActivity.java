package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.redinfo.chenzhi.Fantasy.*;
import cn.redinfo.chenzhi.Fantasy.DataModle.*;
import com.fri.idcread.Idcread;
import com.gmail.orinchen.utility.Tools;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 13-1-4 Time: 下午2:40
 */
public class ReportActivity extends Activity implements View.OnClickListener {
    public final static String URL = "/data/data/cn.redinfo.chenzhi.Fantasy/databases";
    public static String report_url = "http://219.232.243.20:1832/wah/iudrecords.ajax";
    public static String SEARCH_RESULT = "http://219.232.243.20:1832/wah/takeof.ajax";
    //private static String IDSCAN = "cn.redinfo.chenzhi.IDSCANT";
    //private static String BCSCAN = "cn.redinfo.chenzhi.BCSCAN";
    //private static int IDSCAN_REQ_CODE = IDSCAN.hashCode();
    //private static int BCSCAN_REQ_CODE = BCSCAN.hashCode();
    private Button btn_barcodeScann = null;
    private Button btn_ok = null;
    private Button autoBtn = null;
    private Button typeBtn = null;
    private ImageButton menuBtn = null;

    private TextView txt_Name = null;
    private TextView txt_Gender = null;
    private TextView txt_Nation = null;
    private TextView txt_Birthday = null;
    private TextView txt_CardNumber = null;
    private TextView txt_PhoneNumber = null;
    private TextView txt_barcode = null;
    private TextView txt_prodName = null;
    private TextView txt_producers = null;
    private TextView txt_operationDate = null;
    private TextView title = null;

    private EditText et_cardNum = null;
    private EditText et_cardName = null;

    private Spinner sp_operator = null;

    private Spinner sp_take = null;
    private ImageView img_Photo = null;

    private DatePickerDialog opDatePicker = null;

    private ProgressDialog busyDialog = null;
    private TableLayout scanTable = null;
    private LinearLayout imagetLayout = null;
    private TableRow takeRow = null;
    private TableRow birthdayTb = null;
    private TableRow cardNameTb = null;

    SQLiteDatabase db = null;
    public DrDBHelper m_db = null;
    private IDCard idCard = null;
    private BarcodeInfo barcodeInfo = null;

    private String[] defaultarray = {"请选择"};
    private String[] doctorarray = {""};
    private String[] doctorIdarray = {""};
    private String[] doctorSexArray = {""};
    private String doctorName = null;
    private String doctorId = null;
    private String doctorSex = null;
    private String[] takearray = {"请选择", "生育二孩", "不良反应"};
    private String[] takeIdarray = {""};
    private String takereason = null;
    private String data = null;
    private String name = null;
    private String idcode = null;

    public Dialog querydialog = null;
    private Idcread reader = new Idcread();
    ReadThread rt = new ReadThread();
    private int mobileType = 1;//1为盛本机型PE43，2为盛本PA710，3为西姆通机型
    private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isSuccessed = false;
    private boolean type = false;//手术类型选择 false 放入 true 取出
    private boolean inputType = false;//身份证输入类型 扫描 false 手输 true
    //	ExecutorService pool = Executors.newFixedThreadPool(2);
    private boolean needExit = false;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    private static final int MENU_HISTORY = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);
        if (mBTAdapter.getState() != BluetoothAdapter.STATE_OFF) {//此判断代表蓝牙当前为开，反之为关
            mBTAdapter.disable();//关闭蓝牙
        }
        m_db = DrDBHelper.getInstance(ReportActivity.this);
        initCompant();
        this.btn_barcodeScann = (Button) this.findViewById(R.id.btn_barcodeScan);
        this.btn_barcodeScann.setOnClickListener(this);
        this.autoBtn = (Button) findViewById(R.id.autoBtn);
        this.autoBtn.setOnClickListener(this);
        this.typeBtn = (Button) findViewById(R.id.typeBtn);
        this.typeBtn.setOnClickListener(this);
        this.btn_ok = (Button) this.findViewById(R.id.btn_ok);
        this.menuBtn = (ImageButton) this.findViewById(R.id.action_bar_button);
        this.menuBtn.setVisibility(View.VISIBLE);
        this.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = null;
                CustomDialog.Builder customBuilder = new CustomDialog.Builder(
                        ReportActivity.this);
                customBuilder
                        .setTitle("请选择施术类型")
                        .setNegativeButton("放入",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        type = false;
                                        if (type) {
                                            initTakeSpinner();
                                            barcodeInfo = new BarcodeInfo("",
                                                    "", "", "", "", "");
                                            btn_barcodeScann
                                                    .setVisibility(View.GONE);
                                            takeRow.setVisibility(View.VISIBLE);
                                            scanTable.setVisibility(View.GONE);
                                        } else {
                                            takereason = "";
                                            btn_barcodeScann
                                                    .setVisibility(View.VISIBLE);
                                            takeRow.setVisibility(View.GONE);
                                            scanTable
                                                    .setVisibility(View.VISIBLE);
                                        }
                                    }
                                })
                        .setPositiveButton("取出",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        type = true;
                                        if (type) {
                                            initTakeSpinner();
                                            barcodeInfo = new BarcodeInfo("",
                                                    "", "", "", "", "");
                                            btn_barcodeScann
                                                    .setVisibility(View.GONE);
                                            takeRow.setVisibility(View.VISIBLE);
                                            scanTable.setVisibility(View.GONE);
                                        } else {
                                            takereason = "";
                                            btn_barcodeScann
                                                    .setVisibility(View.VISIBLE);
                                            takeRow.setVisibility(View.GONE);
                                            scanTable
                                                    .setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                dialog = customBuilder.create();
                dialog.show();
            }
        });

        if (type) {
            this.btn_barcodeScann.setVisibility(View.GONE);
        } else {
            this.btn_barcodeScann.setVisibility(View.VISIBLE);
        }


        this.sp_operator = (Spinner) this.findViewById(R.id.sp_operator);
        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_item, defaultarray);
        this.sp_operator.setAdapter(doctorAdapter);
        this.sp_operator.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                sp_operator
                        .setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {
                                int index = arg0.getSelectedItemPosition();
                                if (index != 0 && (doctorarray.length > 1)) {
                                    doctorName = doctorarray[index];
                                    doctorId = doctorIdarray[index];
                                    doctorSex = doctorSexArray[index];
                                }
                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                return false;
            }

        });

        this.sp_take = (Spinner) this.findViewById(R.id.sp_take);
        ArrayAdapter<String> takeAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_item, takearray);
        this.sp_take.setAdapter(takeAdapter);
        this.sp_take.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                sp_take.setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int index = arg0.getSelectedItemPosition();
                        if (index > 0) {
                            takereason = takeIdarray[index];
                        } else {
                            takereason = null;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                return false;
            }

        });
        this.img_Photo = (ImageView) this.findViewById(R.id.photoImage);

       /* this.btn_idScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IDSCAN);
                ReportActivity.this.startActivityForResult(intent,
                        IDSCAN_REQ_CODE);
            }
        });*/


        this.scanTable = (TableLayout) findViewById(R.id.scanTable);
        if (type) {
            this.scanTable.setVisibility(View.GONE);
        } else {
            this.scanTable.setVisibility(View.VISIBLE);
        }

        this.takeRow = (TableRow) findViewById(R.id.takeTable);
        if (type) {
            this.takeRow.setVisibility(View.VISIBLE);
        } else {
            this.takeRow.setVisibility(View.GONE);
        }

        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        this.opDatePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i2,
                                          int i3) {
                        Calendar cl = Calendar.getInstance();
                        cl.set(i, i2, i3);
                        String dateStr = Tools.DateToString(cl.getTime(), "yyyy-MM-dd");
                        txt_operationDate.setText(dateStr);
                    }
                }, year, month, day);

        this.initDoctorSpinner();
        this.initTakeSpinner();
        this.txt_operationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opDatePicker.show();
            }
        });

        this.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputType) {
                    if (!type) {
                        if (txt_PhoneNumber.getText().toString().trim().equals("")
                                || txt_PhoneNumber.getText().toString().trim() == null
                                || doctorId == null
                                || txt_operationDate.getText().toString().trim() == null
                                || txt_operationDate.getText().toString().trim().equals("")
                                || barcodeInfo == null || idCard == null) {
                            Toast.makeText(ReportActivity.this, "请完善填报信息",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                report();
                            } catch (NullPointerException e) {
                                Toast.makeText(ReportActivity.this, "请完善填报信息",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (txt_PhoneNumber.getText().toString().trim().equals("")
                                || txt_PhoneNumber.getText().toString().trim() == null
                                || takereason == null
                                || doctorId == null
                                || txt_operationDate.getText().toString().trim() == null
                                || txt_operationDate.getText().toString().trim().equals("")
                                || idCard == null || takereason == null || takereason.equals("")) {
                            Toast.makeText(ReportActivity.this, "请完善填报信息",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                report();
                            } catch (NullPointerException e) {
                                Toast.makeText(ReportActivity.this, "请完善填报信息",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (!type) {
                        if (txt_PhoneNumber.getText().toString().trim().equals("")
                                || txt_PhoneNumber.getText().toString().trim() == null
                                || doctorId == null
                                || txt_operationDate.getText().toString().trim() == null
                                || txt_operationDate.getText().toString().trim().equals("")
                                || barcodeInfo == null) {
                            Toast.makeText(ReportActivity.this, "请完善填报信息",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                report();
                            } catch (NullPointerException e) {
                                Toast.makeText(ReportActivity.this, "请完善填报信息",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (txt_PhoneNumber.getText().toString().trim().equals("")
                                || txt_PhoneNumber.getText().toString().trim() == null
                                || takereason == null
                                || doctorId == null
                                || txt_operationDate.getText().toString().trim() == null
                                || txt_operationDate.getText().toString().trim().equals("")
                                || takereason == null || takereason.equals("")) {
                            Toast.makeText(ReportActivity.this, "请完善填报信息",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                report();
                            } catch (NullPointerException e) {
                                Toast.makeText(ReportActivity.this, "请完善填报信息",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }


            }
        });
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

    private void initCompant() {
        querydialog = new Dialog(ReportActivity.this, R.style.mmdialog);
        querydialog.setContentView(R.layout.query_dialog);
        this.title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("放置信息登记");
        this.imagetLayout = (LinearLayout) findViewById(R.id.image_ll);
        this.birthdayTb = (TableRow) findViewById(R.id.birthday_tb);
        this.cardNameTb = (TableRow) findViewById(R.id.cardName_tb);
        this.txt_Birthday = (TextView) this.findViewById(R.id.text_Birthday);
        this.txt_CardNumber = (TextView) this.findViewById(R.id.text_CardNumber);
        this.txt_PhoneNumber = (TextView) this.findViewById(R.id.text_PhoneNumber);
        this.txt_Gender = (TextView) this.findViewById(R.id.text_Gender);
        this.txt_Name = (TextView) this.findViewById(R.id.text_Name);
        this.txt_Nation = (TextView) this.findViewById(R.id.text_Nation);

        this.txt_barcode = (TextView) this.findViewById(R.id.txt_barcode);
        this.txt_prodName = (TextView) this.findViewById(R.id.txt_prodName);
        this.txt_producers = (TextView) this.findViewById(R.id.txt_producers);

        this.txt_operationDate = (TextView) this
                .findViewById(R.id.txt_operationDate);
        this.et_cardNum = (EditText) this.findViewById(R.id.et_CardNumber);
        this.et_cardName = (EditText) this.findViewById(R.id.et_CardName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, MENU_HISTORY, 0, "未上传记录");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case MENU_HISTORY:
                Intent intent = new Intent();
                intent.setClass(ReportActivity.this, HistoryActivity.class);
                startActivity(intent);
                Toast.makeText(ReportActivity.this, "正在显示未上传记录,请稍后……", Toast.LENGTH_SHORT).show();
                needExit = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                rt.interrupt();
                //wt.interrupt();
                //pool.shutdown();
                if (mobileType == 3) reader.CloseComm2();
                else reader.CloseComm();
                //powerControl.ClosePort(fd2);
                //powerControl.ClosePort(fd);
                //GtTermb.CloseComm();
                // wakeLock.release();
                setData();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Handler hd = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(ReportActivity.this, "准备就绪", Toast.LENGTH_SHORT).show();
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
                    if (xb.equals("1")) {
                        //  txt_Gender.setText("男");
                        Toast.makeText(ReportActivity.this, "非合法身份证",
                                Toast.LENGTH_SHORT).show();
                        idCard = null;
                    } else {
                        idCard = card;
                        txt_Name.setText(content.substring(0, 15));
                        txt_Gender.setText("女");
                        txt_Nation.setText(getMZ(mz));
                        txt_Birthday.setText(content.substring(18, 22) + "年" + content.substring(22, 24) + "月" + content.substring(24, 26) + "日");
                        txt_CardNumber.setText(content.substring(61, 79));
                        img_Photo.setImageBitmap(BitmapFactory.decodeFile("/data/parsebmp/zp.bmp"));
                        if (content.substring(61, 79) != null && content.substring(61, 79).length() > 0)
                            isSuccessed = true;
                    }

                    break;
            }
        }

        ;
    };

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter("com.android.server.scannerservice.broadcast");
        registerReceiver(receiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_barcodeScan:
                Intent intent = new Intent("android.intent.action.SCANNER_BUTTON_DOWN", null);
                this.sendOrderedBroadcast(intent, null);
                break;
            case R.id.autoBtn:
                inputType = false;
                this.birthdayTb.setVisibility(View.VISIBLE);
                this.imagetLayout.setVisibility(View.VISIBLE);
                this.et_cardNum.setVisibility(View.GONE);
                this.txt_CardNumber.setVisibility(View.VISIBLE);
                this.autoBtn.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
                this.typeBtn.setBackgroundResource(R.drawable.abc_ab_solid_light_holo);
                this.autoBtn.setTextColor(getResources().getColor(R.color.mm_hyper_text));
                this.typeBtn.setTextColor(getResources().getColor(R.color.black_40_transparent));
                this.cardNameTb.setVisibility(View.GONE);
                break;
            case R.id.typeBtn:
                inputType = true;
                this.birthdayTb.setVisibility(View.GONE);
                this.imagetLayout.setVisibility(View.GONE);
                this.et_cardNum.setVisibility(View.VISIBLE);
                this.txt_CardNumber.setVisibility(View.GONE);
                this.autoBtn.setBackgroundResource(R.drawable.abc_ab_solid_light_holo);
                this.typeBtn.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
                this.autoBtn.setTextColor(getResources().getColor(R.color.black_40_transparent));
                this.typeBtn.setTextColor(getResources().getColor(R.color.mm_hyper_text));
                this.cardNameTb.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.server.scannerservice.broadcast")) {
                String barcode;
                barcode = intent.getExtras().getString("scannerdata");
                //Toast.makeText(ReportActivity.this,barcode,Toast.LENGTH_SHORT).show();
                //TODO:…
                searchProduct(barcode);
            }
        }
    };

    public void searchProduct(final String barcode) {
        String SEARCH_RESULT = "http://www.youkang800.net/r/share/get_barcode_data.html";
        RequestParams params = new RequestParams();
        params.put("barcode", barcode);
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
                    txt_barcode.setText(barcode);
                    txt_prodName.setText(dp.getVariety_name());
                    txt_producers.setText(dp.getEnterprise_name());
                    barcodeInfo = new BarcodeInfo(txt_barcode.getText().toString().trim(),
                            dp.getVariety_name(), dp.getVariety_id(), dp.getEnterprise_name(),
                            dp.getEnterprise_id(), dp.getBatch_number());
                   /* barcodeInfo.setBarcode(txt_barcode.getText().toString());
                    barcodeInfo.setProdName(dp.getVariety_name());
                    barcodeInfo.setProducers(dp.getEnterprise_name());
                    barcodeInfo.setProdId(dp.getVariety_id());
                    barcodeInfo.setProducersId(dp.getEnterprise_id());
                    barcodeInfo.setBatchcode(dp.getBatch_number());*/
                } catch (Exception e) {
                    Toast.makeText(ReportActivity.this, "查找失败",
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
                Toast.makeText(ReportActivity.this, "发生网络错误，请稍后再试试",
                        Toast.LENGTH_LONG).show();
            }

        });

    }

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
        Toast.makeText(ReportActivity.this, "正在退出,请稍后……", Toast.LENGTH_SHORT).show();
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
        // wakeLock.release();
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

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "OK", Toast.LENGTH_LONG);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == IDSCAN_REQ_CODE) {
            String idStr = data.getStringExtra("id");
            Toast.makeText(this, idStr, Toast.LENGTH_LONG);
            Gson gson = new Gson();
            try {
                idCard = gson.fromJson(idStr, IDCard.class);
            } catch (JsonSyntaxException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
            }
            if (idCard != null) {
                if (idCard.getGenderStr().equals("男")) {
                    Toast.makeText(ReportActivity.this, "非合法身份证",
                            Toast.LENGTH_SHORT).show();
                } else if (idCard.getGenderStr().equals("女")) {
                    txt_Gender.setText(idCard.getGenderStr());
                    txt_Name.setText(idCard.getName());
                    txt_Nation.setText(idCard.getNationalityName());
                    txt_CardNumber.setText(idCard.getCardNumber());
                    txt_Birthday.setText(Tools.DateToString(
                            idCard.getBirthday(), "yyyy-MM-dd"));
                    img_Photo.setImageBitmap(BitmapFactory
                            .decodeFile("/data/parsebmp/zp.bmp"));
                }

            }
        } else if (requestCode == BCSCAN_REQ_CODE) {

            String bcStr = data.getStringExtra("bc");
            Gson gson = new Gson();
            try {
                barcodeInfo = gson.fromJson(bcStr, BarcodeInfo.class);
            } catch (JsonSyntaxException ex) {
            }
            if (barcodeInfo != null) {
                this.txt_barcode.setText(barcodeInfo.getBarcode().trim());
                this.txt_prodName.setText(barcodeInfo.getProdName());
                this.txt_producers.setText(barcodeInfo.getProducers());
            }
        }
    }
    */

    private void initDoctorSpinner() {
        File file = new File(URL, DrDBHelper.DATABASE_NAME);
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
        String sql = "SELECT * FROM dr_data;";
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<String> doctornamearry = new ArrayList<String>();
        ArrayList<String> doctoridarray = new ArrayList<String>();
        ArrayList<String> doctorSexArray = new ArrayList<String>();
        doctornamearry.add("请选择");
        doctoridarray.add("请选择");
        doctorSexArray.add("请选择");
        if (cur != null && cur.moveToFirst()) {
            do {
                doctornamearry.add(cur.getString(cur.getColumnIndex("drName")));
                doctoridarray.add(cur.getString(cur.getColumnIndex("drIdNum")));
                doctorSexArray.add(cur.getString(cur.getColumnIndex("drSex")));
            } while ((cur.moveToNext()));
            cur.close();
            db.close();
        } else {
            cur.close();
            db.close();
        }
        doctorarray = (String[]) doctornamearry
                .toArray(new String[doctornamearry.size()]);
        doctorIdarray = (String[]) doctoridarray
                .toArray(new String[doctoridarray.size()]);
        this.doctorSexArray = (String[]) doctorSexArray
                .toArray(new String[doctorSexArray.size()]);
        ArrayAdapter<String> drAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_item, doctorarray);
        this.sp_operator.setAdapter(drAdapter);
    }

    private void initTakeSpinner() {
        // TODO Auto-generated method stub

        RequestParams params = new RequestParams();
        params = null;
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

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                super.onFailure(arg0, arg1, arg2, arg3);
                Toast.makeText(ReportActivity.this, "连接失败,请检查网络设置",
                        Toast.LENGTH_LONG).show();
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(String content) {
                // TODO Auto-generated method stub
                super.onSuccess(content);
                try {
                    JSONObject jstr = new JSONObject(content);
                    String state = jstr.getString("code");
                    JSONArray takeList;
                    ArrayList<String> takeArray = new ArrayList<String>();
                    ArrayList<String> takeIdArray = new ArrayList<String>();
                    takeArray.add("请选择");
                    takeIdArray.add("请选择");
                    takeList = jstr.getJSONArray("data");

                    for (int i = 0; i < takeList.length(); i++) {
                        takeArray.add((takeList.getJSONObject(i)
                                .getString("text")));
                        takeIdArray.add(takeList.getJSONObject(i).getString(
                                "code"));
                    }
                    takearray = (String[]) takeArray
                            .toArray(new String[takeArray.size()]);
                    takeIdarray = (String[]) takeIdArray
                            .toArray(new String[takeArray.size()]);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                initSp();

            }

        });

    }

    protected void initSp() {
        // TODO Auto-generated method stub
        ArrayAdapter<String> takeAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_item, takearray);
        sp_take.setAdapter(takeAdapter);
    }

    private void report() {
        this.querydialog.show();

        String svno = ((FantasyApplication) this.getApplication())
                .getDeviceId();

        Doctor doctor = new Doctor(this.doctorName.trim(),
                this.doctorSex.equalsIgnoreCase("男") ? "1" : "2",
                this.doctorId);
        String cardNumber;
        String cardName;
        if (!inputType) {
            cardNumber = this.idCard.getCardNumber();
            cardName = this.idCard.getName();
        } else {
            if (!(IdUtils.CheckIDCard18(this.et_cardNum.getText().toString().trim())
                    || IdUtils.CheckIDCard15(this.et_cardNum.getText().toString().trim()))) {
                Toast.makeText(ReportActivity.this, "身份证格式不正确!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if ((this.et_cardName.getText().toString().trim()).equals("") || this.et_cardName.getText().toString().trim() == null) {
                    Toast.makeText(ReportActivity.this, "请输入姓名!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    cardName = this.et_cardName.getText().toString().trim();
                }
                cardNumber = this.et_cardNum.getText().toString().trim();
                if (Integer.parseInt(cardNumber.substring(16, 17)) % 2 == 1) {
                    Toast.makeText(ReportActivity.this, "非合法身份证",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        String parea = cardNumber.substring(0, 2);
        String carea = cardNumber.substring(2, 4);
        String xarea = cardNumber.substring(4, 6);
        String area = cardNumber.substring(0, 6);
        People people = new People(cardNumber,
                cardName,
                parea,
                carea,
                xarea,
                area,
                this.txt_PhoneNumber.getText().toString().trim(),
                this.type ? "2" : "1",
                this.txt_operationDate.getText().toString().trim());

        String yy1 = cardNumber.substring(6, 10); // 出生的年份
        String mm1 = cardNumber.substring(10, 12); // 出生的月份
        String dd1 = cardNumber.substring(12, 14); // 出生的日期
        String birthday = yy1.concat("-").concat(mm1).concat("-").concat(dd1);

        int age = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date today = new Date();
            Date birth = sdf.parse(birthday);
            age = today.getYear() - birth.getYear();
        } catch (Exception e) {
        }
        Operation operation = new Operation(this.txt_operationDate.getText().toString().trim(),
                this.type ? "2" : "1",
                cardNumber,
                cardName,
                age,
                this.txt_PhoneNumber.getText().toString().trim(),
                this.doctorName.trim(),
                this.barcodeInfo.getBatchcode(),
                this.barcodeInfo.getProdName(),
                this.barcodeInfo.getProdId(),
                this.barcodeInfo.getProducers(),
                this.takereason,
                svno);

        OperationInfo operationInfo = new OperationInfo(svno, doctor, people,
                operation);

        Gson gson = new Gson();
        data = gson.toJson(operationInfo);
        name = cardName;
        idcode = cardNumber;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", "100");
        params.add("data", data);

        client.post(report_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                String responseString = new String(responseBody, Charset.defaultCharset()); //String.valueOf(responseBody);
                saveData(data, name, idcode, "1");
                Toast.makeText(ReportActivity.this, "数据上传成功！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                ReportActivity.this.querydialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                saveData(data, name, idcode, "0");
                Toast.makeText(ReportActivity.this, "数据上传失败！请稍后再试！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveData(String data, String name, String idcode, String state) {
        m_db.insert_UPINFO(DrDBHelper.UPDATA_TABEL_NAME, data, name, idcode, this.doctorName.trim(), type ? "1" : "0", state);
    }

    /*private void isBusy(boolean busy) {
        this.btn_ok.setEnabled(!busy);
        this.btn_cancel.setEnabled(!busy);
        this.btn_barcodeScann.setEnabled(!busy);
        this.btn_idScann.setEnabled(!busy);

        if (this.busyDialog == null) {
            this.busyDialog = new ProgressDialog(this);
            this.busyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
            // this.busyDialog.setTitle("提示");//设置标题
            // this.busyDialog.setIcon(R.drawable.icon);//设置图标
            this.busyDialog.setMessage("正在上传记录，请耐心等候...");
            this.busyDialog.setIndeterminate(false);// 设置进度条是否为不明确
            // this.busyDialog.setCancelable(true);//设置进度条是否可以按退回键取消
        }

        if (!busy)
            this.busyDialog.show();
        else
            this.busyDialog.hide();

    }*/

    public static void NavToThis(Activity activity) {
        Intent intent = new Intent(activity, ReportActivity.class);
        activity.startActivity(intent);
    }
}
