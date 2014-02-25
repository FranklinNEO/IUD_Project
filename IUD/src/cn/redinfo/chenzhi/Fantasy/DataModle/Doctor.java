/**
 * @CopyRight (c) 2012-2013
 * @Company <���������Ϣ�������޹�˾>
 * @Project <redts>
 * @JDK_Version_Used:<JDK1.6>
 * @Description:TODO<������;>
 * @Title: BsOperator.java
 * @Author: <�ο�>
 * @Author_of_Contact <E:azj1001@sina.com T:18651831939>
 * @Date 2013-1-9 ����12:34:33
 * @Modified_By: <�޸���>
 * @Modified_of_Contact <�޸�����ϵ��ʽ>
 * @Modified_Date <�޸�����ʱ��>
 * @Why & What is modified <�޸�ԭ������>
 * @Version v1.00
 */
package cn.redinfo.chenzhi.Fantasy.DataModle;

public class Doctor {
  private String name;
  private String sex;
  private String idCard;

  public Doctor(String name, String sex, String idCard) {
    this.name = name;
    this.sex = sex;
    this.idCard = idCard;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }
}
