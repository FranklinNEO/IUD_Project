package cn.redinfo.chenzhi.Fantasy.DataModle;

public class Terminal {

  private String uuid;

  // 所在站点名称
  private String siteName;

  // 所在站点的行政区划名称
  private String siteAreaName;

  // 设备安放地址
  private String address;

  // 设备日常维护人
  private String charge;

  // 地理经度
  private String lng;

  // 地理纬度
  private String lat;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = (siteName != null ? siteName.trim() : siteName);
  }

  public String getSiteAreaName() {
    return siteAreaName;
  }

  public void setSiteAreaName(String siteAreaName) {
    this.siteAreaName = siteAreaName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCharge() {
    return charge;
  }

  public void setCharge(String charge) {
    this.charge = charge;
  }

  public String getLng() {
    return lng;
  }

  public void setLng(String lng) {
    this.lng = lng;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

}
