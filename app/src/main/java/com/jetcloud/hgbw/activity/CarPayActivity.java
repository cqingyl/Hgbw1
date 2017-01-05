package com.jetcloud.hgbw.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.PayBuyingAdapter;
import com.jetcloud.hgbw.adapter.PayTicketAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.MyExpandableListView;
import com.jetcloud.hgbw.view.MyListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarPayActivity extends BaseActivity {
    private final static String TAG_LOG = CarPayActivity.class.getSimpleName();
    private PayBuyingAdapter buyingAdapter;
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private TextView tv_wei_num;
    private TextView tv_gcb_num;
    private CheckBox cb_weixin;
    private CheckBox cb_gcb;
    private TextView tv_total_price;
    private MyExpandableListView elv_buying;
    private MyListView lv_my_ticket;
    private int totalNum;
    private TextView tv_go_to_pay;
    private double totalPrice;
    private double totalGcb;
    private List<String> wayData;
    private HgbwApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_car_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        app = (HgbwApplication) getApplication();
        topbar.setCenterText("结算");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_wei_num = getView(R.id.tv_wei_num);
        tv_gcb_num = getView(R.id.tv_gcb_num);
        elv_buying = getView(R.id.elv_buying);
        lv_my_ticket = getView(R.id.lv_my_ticket);
        tv_total_price = getView(R.id.tv_total_price);
        tv_go_to_pay = getViewWithClick(R.id.tv_go_to_pay);
        cb_gcb = getViewWithClick(R.id.cb_gcb);
        cb_weixin = getViewWithClick(R.id.cb_weixin);

    }

    @Override
    protected void loadData() {
        groups = app.getGroups();
        children = app.getChildren();
        totalPrice = app.getTotalPrice();
        totalGcb = app.getTotalGcb();
        Log.i(TAG_LOG, "loadData: " + totalGcb);
//        for (int i = 0; i < groups.size(); i ++) {
//            List<ShopCarInfo> scList = children.get(groups.get(i).getId());
//            for (int j = 0; j < scList.size(); j ++){
//                ShopCarInfo shopCarInfo = scList.get(i);
//                totalPrice += totalNum * shopCarInfo.getP_price();
//                totalNum += shopCarInfo.getP_local_number();
//                Log.i(TAG_LOG, "loadData: " +  totalPrice+ " "  + totalNum);
//            }
//        }

        wayData = new ArrayList<>();
        wayData.add("20元卡券一张，可抵扣20元");
        wayData.add("40元卡券一张，可抵扣40元");
        lv_my_ticket.setAdapter(new PayTicketAdapter(this, wayData));

        buyingAdapter = new PayBuyingAdapter(CarPayActivity.this, groups, children);
        elv_buying.setAdapter(buyingAdapter);
        elv_buying.setGroupIndicator(null);
        for (int i = 0; i < buyingAdapter.getGroupCount(); i++) {
            elv_buying.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }

//        tv_gcb_num.setText();
        //总价格： 默认显示微信价格
        tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
        tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
        BigDecimal db = BigDecimal.valueOf(totalGcb);
//        Log.i(TAG_LOG, "initListData: " + db);
        tv_gcb_num.setText(context.getString(R.string.gcb_display, db));
    }

    @Override
    public void onClick(View view) {
        AlertDialog alert;
        if (view.getId() == R.id.cb_gcb) {
            cb_gcb.setChecked(true);
            cb_weixin.setChecked(false);
            cb_gcb.setClickable(false);
            cb_weixin.setClickable(true);
            tv_total_price.setText(context.getString(R.string.take_food_gcb_total, totalGcb));
        } else if (view.getId() == R.id.cb_weixin) {
            cb_gcb.setChecked(false);
            cb_weixin.setChecked(true);
            cb_gcb.setClickable(true);
            cb_weixin.setClickable(false);
            tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
        } else if(view.getId() == R.id.tv_go_to_pay)  {
                    if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)) {
                            alert = new AlertDialog.Builder(context).create();
                            alert.setTitle("操作提示");
                            alert.setMessage("您还未绑定交易宝账号");
                            alert.getWindow().setBackgroundDrawableResource(R.color.white);
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, "去绑定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(CarPayActivity.this, BindingActivity.class);
                                            intent.putExtra(CarPayActivity.this.getString(R.string.jump_resource), CarPayActivity.class.getSimpleName());
                                            startActivity(intent);
                                        }
                                    });
                            alert.show();
                    } else {
                        Intent i = new Intent(CarPayActivity.this, PayNextActivity.class);
                        if (cb_gcb.isChecked()){
                            app.setType(CarPayActivity.this.getString(R.string.pay_way_gcb));
                        } else {
                            app.setType(CarPayActivity.this.getString(R.string.pay_way_weixin));
                        }
                        startActivity(i);
                    }
                }
    }
}
