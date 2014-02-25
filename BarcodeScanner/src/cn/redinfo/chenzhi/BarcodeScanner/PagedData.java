package cn.redinfo.chenzhi.BarcodeScanner;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-9
 * Time: 下午2:47
 */
public class PagedData {
  private int total = 1;
  private ArrayList<DrugProduct> rows = null;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public ArrayList<DrugProduct> getRows() {
    return rows;
  }

  public void setRows(ArrayList<DrugProduct> rows) {
    this.rows = rows;
  }
}
