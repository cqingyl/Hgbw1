package com.jetcloud.hgbw.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MachineListAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.wx.wheelview.widget.WheelView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class MachineListActivity extends BaseActivity {

    private final static String TAG_LOG = MachineListActivity.class.getSimpleName();
    @ViewInject(R.id.ll_btn_layout)
    LinearLayout ll_btn_layout;
    @ViewInject(R.id.lv_machine)
    ListView lv_machine;
    @ViewInject(R.id.activity_machine_list)
    LinearLayout activity_machine_list;
    @ViewInject(R.id.btn_shenghui)
    Button btn_shenghui;
    private CustomProgressDialog progress;
    private MachineListActivity context;
    private HgbwApplication app;
    private Button[] btns;
    private MachineListAdapter adapter;
    private WheelView wheelView;
    private PopupWindow window;
    private int inwhere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_machine_list);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void initView() {
        context = MachineListActivity.this;
        app = (HgbwApplication) getApplication();
        topbar.setCenterText("机器列表");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        btn_shenghui.setOnClickListener(this);

    }

    @Override
    protected void loadData() {
    }

}
