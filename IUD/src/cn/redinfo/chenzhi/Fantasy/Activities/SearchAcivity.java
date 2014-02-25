package cn.redinfo.chenzhi.Fantasy.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.redinfo.chenzhi.Fantasy.Adapters.SearchAdapter;
import cn.redinfo.chenzhi.Fantasy.DataModle.BsOperatorInfo;
import cn.redinfo.chenzhi.Fantasy.DataModle.PagedData;
import cn.redinfo.chenzhi.Fantasy.R;
import cn.redinfo.chenzhi.Fantasy.WebHelper;
import cn.redinfo.chenzhi.Fantasy.WebHelperListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-9
 * Time: 上午9:34
 */
public class SearchAcivity extends ListActivity
        implements View.OnClickListener {
    private ImageButton searchButton = null; // 搜索按钮
    private ImageButton pageUpButton = null; // 上一页按钮
    private ImageButton pageDownButton = null; // 下一页按钮
    private TextView pageInfoText = null; // 页码信息

    private ProgressDialog busyDialog = null;

    private int page = 1;
    private int pageCount = 0;
    private ArrayList<BsOperatorInfo> datas = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.search_layout);
        this.initToolbar();
    }

    // 初始化底部工具条
    private void initToolbar() {
        this.searchButton = (ImageButton) this.findViewById(R.id.search_button);
        this.searchButton.setOnClickListener(this);
        this.pageUpButton = (ImageButton) this.findViewById(R.id.pageup_button);
        this.pageUpButton.setOnClickListener(this);
        this.pageUpButton.setEnabled(false);
        this.pageDownButton = (ImageButton) this.findViewById(R.id.pagedown_button);
        this.pageDownButton.setOnClickListener(this);
        this.pageDownButton.setEnabled(false);
        this.pageInfoText = (TextView) this.findViewById(R.id.page_info_text);
        this.pageInfoText.setText("");
    }

    public static void NavToThis(Activity activity) {
        Intent intent = new Intent(activity, SearchAcivity.class);
        activity.startActivity(intent);
    }

    // 设置控件忙碌状态
    private void isBusy(boolean busy) {
        busy = !busy;
        this.pageDownButton.setEnabled(busy);
        this.pageUpButton.setEnabled(busy);
        this.searchButton.setEnabled(busy);

        if (this.busyDialog == null) {
            this.busyDialog = new ProgressDialog(this);
            this.busyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            //this.busyDialog.setTitle("提示");//设置标题
            //this.busyDialog.setIcon(R.drawable.icon);//设置图标
            this.busyDialog.setMessage("正在查询记录，请耐心等候...");
            this.busyDialog.setIndeterminate(false);//设置进度条是否为不明确
            //this.busyDialog.setCancelable(true);//设置进度条是否可以按退回键取消
        }

        if (!busy)
            this.busyDialog.show();
        else
            this.busyDialog.hide();

        this.setPageButton();
    }

    private void setPageButton() {
        if (this.datas == null) {
            this.pageUpButton.setEnabled(false);
            this.pageDownButton.setEnabled(false);
            return;
        }

        int page = this.page;
        int pageCount = this.pageCount;
        if (page > 1)
            pageUpButton.setEnabled(true);
        else
            pageUpButton.setEnabled(false);

        if (pageCount > page)
            pageDownButton.setEnabled(true);
        else
            pageDownButton.setEnabled(false);
    }

    // 处理按钮事件 包括搜索 上一页 下一页
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                search();
                break;
            case R.id.pagedown_button:
                //this.markerManager.pageDown();
                break;
            case R.id.pageup_button:
                //this.markerManager.pageUp();
                break;
        }
    }

    private void initData(PagedData pd) {
        this.datas = pd.getRows();
        this.pageCount = pd.getTotal();
        this.page = 1;
        if (datas == null)
            this.datas = new ArrayList<BsOperatorInfo>();
        SearchAdapter adapter = new SearchAdapter(this, datas);
        //参考ArrayAdapter的构造函数
        this.setListAdapter(adapter);
    }

    private void search() {
        this.isBusy(true);
        WebHelper.search(new WebHelperListener() {
            @Override
            public void onFinished(String msg, Throwable err) {
                isBusy(false);
                if (err != null) {
                    Toast.makeText(SearchAcivity.this, "发生网络错误，请稍后再试。", Toast.LENGTH_LONG).show();
                    return;
                }
                Gson gson = new Gson();
                PagedData pagedData =
                        gson.fromJson(msg, new TypeToken<PagedData>() {
                        }.getType());
                initData(pagedData);
            }
        });
    }
}