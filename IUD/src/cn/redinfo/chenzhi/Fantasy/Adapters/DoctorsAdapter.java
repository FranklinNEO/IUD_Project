package cn.redinfo.chenzhi.Fantasy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.redinfo.chenzhi.Fantasy.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-8
 * Time: 下午1:30
 */
public class DoctorsAdapter extends BaseAdapter {
  private ArrayList<String> doctors = null;
  private LayoutInflater inflater = null;

  public DoctorsAdapter(Context context, ArrayList<String> doctors) {
    this.inflater = LayoutInflater.from(context);
    this.doctors = doctors;
  }

  @Override
  public int getCount() {
    return doctors.size();
  }

  @Override
  public Object getItem(int i) {
    return doctors.get(i);
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
      convertView = (LinearLayout) this.inflater.inflate(R.layout.simple_clickable_item, null);
      // 设置行背景
      if (doctors.size() == 1) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_single);
      } else if (i == 0) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_top);
      } else if (i == getCount() - 1) {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_bottom);
      } else {
        convertView.setBackgroundResource(R.drawable.list_item_bkg_middle);
      }

      // 创建标题
      TextView contentTextView = (TextView) ((LinearLayout) convertView).getChildAt(0);
      holder.setContentTextView(contentTextView);
      contentTextView.setText(this.doctors.get(i).toString());
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
      holder.getContentTextView().setText(this.doctors.get(i).toString());
    }
    return convertView;
  }

  private class ViewHolder {
    private TextView contentTextView = null;

    public TextView getContentTextView() {
      return contentTextView;
    }

    public void setContentTextView(TextView contentTextView) {
      this.contentTextView = contentTextView;
    }
  }
}
