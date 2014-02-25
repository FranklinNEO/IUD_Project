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

public class BsOperator {

  private BsPerson person = null;

  private BsOperatorInfo optInfo = null;

  public BsPerson getPerson() {
    return person;
  }

  public void setPerson(BsPerson person) {
    this.person = person;
  }

  public BsOperatorInfo getOptInfo() {
    return optInfo;
  }

  public void setOptInfo(BsOperatorInfo optInfo) {
    this.optInfo = optInfo;
  }
}
