/**
 * @CopyRight (c) 2012-2013
 * @Company <���������Ϣ�������޹�˾>
 * @Project <redts>
 * @JDK_Version_Used:<JDK1.6>
 * @Description:<ҵ����Ա��Ϣ>
 * @Title: BsPerson.java
 * @Author: <�ο�>
 * @Author_of_Contact <E:azj1001@sina.com T:18651831939>
 * @Date 2013-1-9 ����11:22:48
 * @Modified_By: <�޸���>
 * @Modified_of_Contact <�޸�����ϵ��ʽ>
 * @Modified_Date <�޸�����ʱ��>
 * @Why & What is modified <�޸�ԭ������>
 * @Version v1.00
 */
package cn.redinfo.chenzhi.Fantasy.DataModle;

public class People{

  private String idCard;

  private String peopleName;

  private String parea;

  private String carea;

  private String xarea;

  private String areaId;

  private String phone;

  private String iudstatus;

  private String lastTime;

  public People(String idCard,
                String peopleName,
                String parea,
                String carea,
                String xarea,
                String areaId,
                String phone,
                String iudstatus,
                String lastTime) {
    this.idCard = idCard;
    this.peopleName = peopleName;
    this.parea = parea;
    this.carea = carea;
    this.xarea = xarea;
    this.areaId = areaId;
    this.phone = phone;
    this.iudstatus = iudstatus;
    this.lastTime = lastTime;
  }
}
