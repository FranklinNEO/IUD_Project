package cn.redinfo.chenzhi.Fantasy.DataModle;

public class Sites {

  // 站点名称
  private String sitesName;

  // 站点电话
  private String telephone;

  // 所在地区
  private String area;

  // 所在地区完整名称
  private String areaName;

  // 站点的详细地址
  private String address;

  // 站点总负责人
  private String charge;


  public String getSitesName() {
    return sitesName;
  }

  public void setSitesName(String sitesName) {
    this.sitesName = (sitesName != null ? sitesName.trim() : sitesName);
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
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

}
