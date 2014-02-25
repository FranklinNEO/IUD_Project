package cn.redinfo.chenzhi.Fantasy.DataModle;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-9
 * Time: 下午3:48
 */
public class PagedData {
  private int total = 0;
  private ArrayList<BsOperatorInfo> rows = null;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public ArrayList<BsOperatorInfo> getRows() {
    return rows;
  }

  public void setRows(ArrayList<BsOperatorInfo> rows) {
    this.rows = rows;
  }
}
