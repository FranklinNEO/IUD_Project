package cn.redinfo.chenzhi.Fantasy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.redinfo.chenzhi.Fantasy.DataModle.BsOperatorInfo;
import cn.redinfo.chenzhi.Fantasy.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-9
 * Time: 上午9:50
 */
public class SearchAdapter extends BaseAdapter {
  private ArrayList<BsOperatorInfo> datas = null;
  private LayoutInflater inflater = null;

  public SearchAdapter(Context context, ArrayList<BsOperatorInfo> datas) {
    this.inflater = LayoutInflater.from(context);
    this.datas = datas;
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int i) {
    return datas.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View convertView, ViewGroup viewGroup) {

    ViewHolder holder = null;

    if (convertView == null) {
      holder = new ViewHolder();
      convertView = this.inflater.inflate(R.layout.sr_item, null);
      // 设置行背景
      if (datas.size() == 1) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_single);
      } else if (i == 0) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_top);
      } else if (i == getCount() - 1) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_bottom);
      } else {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_middle);
      }

      LinearLayout tempLayout = (LinearLayout) convertView;
      // 创建标题
      TextView txt_opDate = (TextView) convertView.findViewById(R.id.txt_operationDate);
      holder.txt_opDate = txt_opDate;

      TextView txt_opr = (TextView) convertView.findViewById(R.id.txt_operator);

      holder.txt_opr = txt_opr;

      TextView txt_prod = (TextView) convertView.findViewById(R.id.txt_prodName);
      holder.txt_prod = txt_prod;
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    BsOperatorInfo bo = datas.get(i);
    if (bo != null) {

      holder.txt_opDate.setText(bo.getOpDateStr());
      holder.txt_opr.setText(bo.getDoctors());
      holder.txt_prod.setText(bo.getDrugName());
    }

    return convertView;
  }

  private class ViewHolder {
    public TextView txt_opDate = null;
    public TextView txt_opr = null;
    public TextView txt_prod = null;
  }
}
