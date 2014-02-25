package cn.redinfo.chenzhi.Fantasy.DataModle;

public class AreaInfo {
  private String Name;
  private String Code;

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getCode() {
    return Code;
  }

  public void setCode(String code) {
    Code = code;
  }

  @Override
  public String toString() {
    return this.Name;
  }
}
