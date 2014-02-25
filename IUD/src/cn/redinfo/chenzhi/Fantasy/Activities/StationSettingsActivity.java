package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cn.redinfo.chenzhi.Fantasy.DBHelper;
import cn.redinfo.chenzhi.Fantasy.DataModle.AreaInfo;
import cn.redinfo.chenzhi.Fantasy.DataModle.Sites;
import cn.redinfo.chenzhi.Fantasy.DataModle.Station;
import cn.redinfo.chenzhi.Fantasy.DataModle.Terminal;
import cn.redinfo.chenzhi.Fantasy.R;
import cn.redinfo.chenzhi.Fantasy.WebHelper;
import cn.redinfo.chenzhi.Fantasy.WebHelperListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gmail.orinchen.utility.Tools;

import java.util.ArrayList;

public class StationSettingsActivity extends Activity {

  private Spinner sp_province = null;
  private Spinner sp_city = null;
  private Spinner sp_county = null;

  private EditText edt_SiteName = null;
  private EditText edt_SiteCharge = null;
  private EditText edt_SitePhone = null;
  private EditText edt_SiteAddress = null;

  private Button btn_post = null;

  private DBHelper db = null;

  private double lng = 0.0;
  private double lat = 0.0;

  public LocationClient locationClient = null;

  private ProgressDialog busyDialog = null;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.station_settings_layout);

    this.db = new DBHelper(this);

    this.sp_province = (Spinner) this.findViewById(R.id.province_spinner);
    this.sp_city = (Spinner) this.findViewById(R.id.city_spinner);
    this.sp_county = (Spinner) this.findViewById(R.id.county_spinner);

    this.edt_SiteAddress = (EditText) this.findViewById(R.id.edt_SiteAddress);
    this.edt_SiteCharge = (EditText) this.findViewById(R.id.edt_SiteCharge);
    this.edt_SiteName = (EditText) this.findViewById(R.id.edt_SiteName);
    this.edt_SitePhone = (EditText) this.findViewById(R.id.edt_SitePhone);

    this.btn_post = (Button) this.findViewById(R.id.btn_post);
    this.btn_post.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PostData(view);
      }
    });

    locationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
    locationClient.registerLocationListener(new BDLocationListener() {
      @Override
      public void onReceiveLocation(BDLocation bdLocation) {
        lat = bdLocation.getLatitude();
        lng = bdLocation.getLongitude();
      }

      @Override
      public void onReceivePoi(BDLocation bdLocation) {

      }
    });    //注册监听函数

    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true);
    option.setAddrType("all");//返回的定位结果包含地址信息
    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
    option.disableCache(true);//禁止启用缓存定位
    option.setPoiNumber(5);  //最多返回POI个数
    option.setPoiDistance(1000); //poi查询距离
    option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
    locationClient.setLocOption(option);

    locationClient.start();
    if (locationClient != null && locationClient.isStarted())
      locationClient.requestLocation();

    this.initStation();

  }

  private void initProvinceSpinner(final String tagCode) {
    ArrayList<AreaInfo> areas = db.getProvince();
    ArrayAdapter<AreaInfo> adapter = new ArrayAdapter<AreaInfo>(this,
      android.R.layout.simple_spinner_item, areas);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    this.sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        AreaInfo area = (AreaInfo) spinner.getSelectedItem();
        if (area.getCode().equalsIgnoreCase("000000")) {
          sp_city.setSelection(0, true);
          sp_city.setEnabled(false);
          sp_county.setSelection(0, true);
          sp_county.setEnabled(false);
        } else {
          sp_city.setEnabled(true);
          AreaInfo cArea = (AreaInfo) sp_city.getSelectedItem();
          if (cArea == null || !cArea.getCode().startsWith(area.getCode())) {
            initCitySpinner(area.getCode(), null);
          }
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    this.sp_province.setAdapter(adapter);
    if (tagCode != null) {
      int i = -1;
      for (AreaInfo area : areas) {
        i++;
        if (area.getCode().equalsIgnoreCase(tagCode)) {
          this.sp_province.setSelection(i);
          break;
        }
      }
    }
  }

  private void initCitySpinner(String code, final String tagCode) {
    ArrayList<AreaInfo> areas = db.getCityByProvinceCode(code);
    ArrayAdapter<AreaInfo> adapter = new ArrayAdapter<AreaInfo>(this,
      android.R.layout.simple_spinner_item, areas);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    this.sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        AreaInfo area = (AreaInfo) spinner.getSelectedItem();
        if (area.getCode().equalsIgnoreCase("000000")) {
          sp_county.setSelection(0, true);
          sp_county.setEnabled(false);
        } else {
          sp_county.setEnabled(true);

          AreaInfo cArea = (AreaInfo) sp_county.getSelectedItem();
          if (cArea == null || !cArea.getCode().startsWith(area.getCode())) {
            initCountySpinner(area.getCode(), null);
          }
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    this.sp_city.setAdapter(adapter);

    if (tagCode != null) {
      int i = -1;
      for (AreaInfo area : areas) {
        i++;
        if (area.getCode().equalsIgnoreCase(tagCode)) {
          this.sp_city.setSelection(i);
          break;
        }
      }
    }
  }

  private void initCountySpinner(String code, String tagCode) {
    ArrayList<AreaInfo> areas = db.getCountyByCityCode(code);
    ArrayAdapter<AreaInfo> adapter = new ArrayAdapter<AreaInfo>(this,
      android.R.layout.simple_spinner_item, areas);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    this.sp_county.setAdapter(adapter);
    if (tagCode != null) {
      int i = -1;
      for (AreaInfo area : areas) {
        i++;
        if (area.getCode().equalsIgnoreCase(tagCode)) {
          this.sp_county.setSelection(i);
          break;
        }
      }
    }
  }

  private AreaInfo getAreaInfo() {

    AreaInfo countyInfo = (AreaInfo) sp_county.getSelectedItem();
    AreaInfo cityInfo = (AreaInfo) sp_city.getSelectedItem();
    AreaInfo provinceInfo = (AreaInfo) sp_province.getSelectedItem();
    AreaInfo result = new AreaInfo();
    if (provinceInfo != null && !provinceInfo.getCode().equalsIgnoreCase("000000")) {
      if (cityInfo != null && !cityInfo.getCode().equalsIgnoreCase("000000")) {
        if (countyInfo != null && !countyInfo.getCode().equalsIgnoreCase("000000")) {
          result.setCode(countyInfo.getCode());
          result.setName(provinceInfo.getName() + cityInfo.getName() + countyInfo.getName());
        } else {
          result.setCode(countyInfo.getCode());
          result.setName(provinceInfo.getName() + cityInfo.getName());
        }
      } else {
        result.setCode(provinceInfo.getCode());
        result.setName(provinceInfo.getName());
      }
    } else {
      result = null;
    }
    return result;
  }

  private Station buileStation() {
    String siteAddress = this.edt_SiteAddress.getText().toString().trim();
    String siteCharge = this.edt_SiteCharge.getText().toString().trim();
    String siteName = this.edt_SiteName.getText().toString().trim();
    String sitePhone = this.edt_SitePhone.getText().toString().trim();

    AreaInfo area = this.getAreaInfo();

    if (area == null ||
      siteAddress.isEmpty() ||
      siteCharge.isEmpty() ||
      siteName.isEmpty() ||
      sitePhone.isEmpty())
      return null;


    Sites site = new Sites();
    site.setAddress(siteAddress);
    site.setArea(area.getCode());
    site.setAreaName(area.getName());
    site.setCharge(siteCharge);
    site.setSitesName(siteName);
    site.setTelephone(sitePhone);

    Terminal terminal = new Terminal();
    terminal.setAddress(siteAddress);
    terminal.setCharge(siteCharge);
    terminal.setLat(String.valueOf(lat));
    terminal.setLng(String.valueOf(lng));
    terminal.setUuid("000000001");
    terminal.setSiteName(siteName);
    terminal.setSiteAreaName(area.getName());

    Station is = new Station();
    is.setSite(site);
    is.setTerminal(terminal);
    return is;
  }

  private void initStation() {
    Station st = db.getStation();
    if (st == null) {
      initProvinceSpinner(null);
      return;
    }
    this.edt_SiteAddress.setText(st.getSite().getAddress());
    this.edt_SiteCharge.setText(st.getSite().getCharge());
    this.edt_SiteName.setText(st.getSite().getSitesName());
    this.edt_SitePhone.setText(st.getSite().getTelephone());

    String areaCode = st.getSite().getArea();
    areaCode = Tools.padRight(areaCode, 6, '0');

    String pCode = areaCode.substring(0, 2);
    String cCode = areaCode.substring(0, 4);
    this.initProvinceSpinner(pCode);
    if (!cCode.endsWith("00")) {
      this.initCitySpinner(pCode, cCode);
      if (!areaCode.endsWith("00")) {
        this.initCountySpinner(cCode, areaCode);
      }
    }
  }

  public void PostData(View view) {
    Station is = this.buileStation();

    if (is == null) {
      Toast.makeText(this, "请将信息填写完整。", Toast.LENGTH_LONG);
    }

    this.isBusy(true);

    db.putStation(is);

    WebHelper.postSeitSettings(is, new WebHelperListener() {
      @Override
      public void onFinished(String msg, Throwable err) {
        isBusy(false);
        String result = msg;
        if (err != null)
          result = err.getMessage();
        Toast.makeText(StationSettingsActivity.this, result, Toast.LENGTH_LONG);
      }
    });
  }

  private void isBusy(boolean busy) {
    btn_post.setEnabled(!busy);

    if (this.busyDialog == null) {
      this.busyDialog = new ProgressDialog(this);
      this.busyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
      //this.busyDialog.setTitle("提示");//设置标题
      //this.busyDialog.setIcon(R.drawable.icon);//设置图标
      this.busyDialog.setMessage("正在保存记录，请耐心等候...");
      this.busyDialog.setIndeterminate(false);//设置进度条是否为不明确
      //this.busyDialog.setCancelable(true);//设置进度条是否可以按退回键取消
    }

    if (!busy)
      this.busyDialog.show();
    else
      this.busyDialog.hide();

  }

  public static void NavToThis(Activity activity) {
    Intent intent = new Intent(activity, StationSettingsActivity.class);
    activity.startActivity(intent);
  }
}