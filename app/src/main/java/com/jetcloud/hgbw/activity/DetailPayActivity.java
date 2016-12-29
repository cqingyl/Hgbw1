package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.PayTicketAdapter;
import com.jetcloud.hgbw.bean.ShopCarInfo;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.jetcloud.hgbw.activity.DetailsActivity.FOOD_OBJECT;

public class DetailPayActivity extends BaseActivity {

    private final static String TAG_LOG = HomePayActivity.class.getSimpleName();

    @ViewInject(R.id.lv_my_ticket)
    private ListView lv_my_ticket;
    @ViewInject(R.id.tv_gcb_num)
    private TextView tv_gcb_num;
    @ViewInject(R.id.tv_wei_num)
    private TextView tv_wei_num;
    @ViewInject(R.id.cb_weixin)
    private CheckBox cb_weixin;
    @ViewInject(R.id.cb_gcb)
    private CheckBox cb_gcb;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_machine_title)
    private TextView tv_machine_title;
    @ViewInject(R.id.tv_food_title)
    private TextView tv_food_title;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;


    private ArrayList<ShopCarInfo> listObj;
    private List<String> wayData;
    private float totalPrice;
    private ShopCarInfo shopCarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail_pay);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("结算");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
    }

    @Event(value = {R.id.cb_gcb, R.id.cb_weixin, R.id.tv_go_to_pay})
    private void onPayWayCheckedChanged(View view) {
        if (view.getId() == R.id.cb_gcb) {
            cb_gcb.setChecked(true);
            cb_weixin.setChecked(false);
            cb_gcb.setClickable(false);
            cb_weixin.setClickable(true);
        } else if (view.getId() == R.id.cb_weixin){
            cb_gcb.setChecked(false);
            cb_weixin.setChecked(true);
            cb_gcb.setClickable(true);
            cb_weixin.setClickable(false);
        } else if (view.getId() == R.id.tv_go_to_pay) {
            listObj.clear();
            listObj.add(shopCarInfo);
            Intent i = new Intent(DetailPayActivity.this, PayNextActivity.class);
            i.putExtra(FOOD_OBJECT, listObj);
            startActivity(i);
        }
    }
    @Override
    protected void loadData() {
        initListData();
    }

    private void initListData() {
        listObj = (ArrayList<ShopCarInfo>) getIntent().getSerializableExtra(FOOD_OBJECT);
        wayData = new ArrayList<>();
        wayData.add("20元卡券一张，可抵扣20元");
        wayData.add("40元卡券一张，可抵扣40元");
        lv_my_ticket.setAdapter(new PayTicketAdapter(this, wayData));
        Log.i(TAG_LOG, "initView: " + listObj.size());

        //只有一件商品
        shopCarInfo = listObj.get(0);
        int count = shopCarInfo.getP_local_number();
        totalPrice = shopCarInfo.getP_price() * count;
        tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
        tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
        tv_money.setText(String.valueOf(context.getString(R.string.rmb_display, totalPrice)));
        tv_num.setText(String.format(getString(R.string.take_food_num), count));
        tv_food_title.setText(shopCarInfo.getP_name());
        String machineName = shopCarInfo.getP_machine();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        tv_machine_title.setText(String.format(context.getString(R.string.machine_name),"成都",machineNum));

    }

}
