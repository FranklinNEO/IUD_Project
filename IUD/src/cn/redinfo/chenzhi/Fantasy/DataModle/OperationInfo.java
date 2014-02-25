package cn.redinfo.chenzhi.Fantasy.DataModle;

/**
 * Created by orinchen on 13-12-11.
 */
public class OperationInfo {
  private String factoryCode;
  private Doctor json_doctor;
  private People json_people;
  private Operation json_operator;

  public OperationInfo(String factoryCode,
                       Doctor json_doctor,
                       People json_people,
                       Operation json_operator)
  {
    this.factoryCode = factoryCode;
    this.json_doctor = json_doctor;
    this.json_people = json_people;
    this.json_operator = json_operator;
  }
}
