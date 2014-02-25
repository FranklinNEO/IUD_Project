package cn.redinfo.chenzhi.IDScanner;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Orin Chen
 * Date: 12-12-12
 * Time: 下午4:21
 */
public class IDCard {

  private static String[] nationNames = new String[]{"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布衣", "朝鲜", "满", "侗", "瑶", "白",
    "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇",
    "柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒",
    "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺"};

  private String name;
  private Gender gender;
  private int nationality;
  private Date birthday;
  private String cardNumber;
  private String address;
  private String issuingAuthority;
  private Date validityDateFrom;
  private Date validityDateEnd;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Gender getGender() {
    return gender;
  }

  public String getGenderStr() {
    return this.getGender() == Gender.Male ? "男" : "女";
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getNationalityName() {
    if (this.nationality < 1 || this.nationality > nationNames.length)
      return null;
    return nationNames[this.nationality - 1];
  }

  public int getNationality() {
    return this.nationality;
  }

  public void setNationality(int nationality) {
    this.nationality = nationality;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  public Date getValidityDateFrom() {
    return validityDateFrom;
  }

  public void setValidityDateFrom(Date startDate) {
    this.validityDateFrom = startDate;
  }

  public Date getValidityDateEnd() {
    return validityDateEnd;
  }

  public void setValidityDateEnd(Date endDate) {
    this.validityDateEnd = endDate;
  }

  public enum Gender {Male, Female}

  ;
}
