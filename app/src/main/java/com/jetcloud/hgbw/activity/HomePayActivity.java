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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.PayTicketAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomePayActivity extends BaseActivity {
    private final static String TAG_LOG = HomePayActivity.class.getSimpleName();

    @ViewInject(R.id.img_food)
    private ImageView img_food;
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
    @ViewInject(R.id.tv_btn_dec)
    private TextView tv_btn_dec;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;
    @ViewInject(R.id.tv_btn_add)
    private TextView tv_btn_add;

    private List<ShopCarInfo> listObj;
    private List<String> wayData;
    private float totalPrice;
    private double totalGcb;
    private BigDecimal gcbBigDecimal;
    private int count = 1;
    private ShopCarInfo shopCarInfo;
    private HgbwApplication app;
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_homepay);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        app = (HgbwApplication) getApplication();
        topbar.setCenterText("结算");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
    }

        @Event(value = {R.id.cb_gcb, R.id.cb_weixin, R.id.tv_go_to_pay})
    private void onPayWayCheckedChanged(View view) {
            AlertDialog alert;
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
            if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)) {
                if (cb_gcb.isChecked()){
                    alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("操作提示");
                    alert.setMessage("您还未绑定交易宝账号");
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

                                    Intent intent = new Intent(HomePayActivity.this, BindingActivity.class);
                                    intent.putExtra(HomePayActivity.this.getString(R.string.jump_resource), CarPayActivity.class.getSimpleName());
                                    startActivity(intent);
                                }
                            });
                    alert.show();
                }
            } else {
                //只有一种产品
                app.setTotalGcb(totalGcb);
                app.setTotalPrice(totalPrice);
                if (cb_gcb.isChecked()){
                    app.setType(HomePayActivity.this.getString(R.string.pay_way_gcb));
                } else {
                    app.setType(HomePayActivity.this.getString(R.string.pay_way_weixin));
                }
                Intent i = new Intent(HomePayActivity.this, PayNextActivity.class);
                startActivity(i);
            }
        }
    }
    @Override
    protected void loadData() {
        initListData();
    }

    private void initListData() {

        wayData = new ArrayList<>();
        wayData.add("20元卡券一张，可抵扣20元");
        wayData.add("40元卡券一张，可抵扣40元");
        lv_my_ticket.setAdapter(new PayTicketAdapter(this, wayData));

        //只有一件商品
        groups = app.getGroups();
        listObj = app.getChildren().get(groups.get(0).getId());
        Log.i(TAG_LOG, "initView: " + listObj.size());
        shopCarInfo = listObj.get(0);
        totalPrice = shopCarInfo.getP_price() * count;
        totalGcb = shopCarInfo.getP_vr9() * count;
        gcbBigDecimal = BigDecimal.valueOf(totalGcb);
        shopCarInfo.setP_local_number(count);
        tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
        tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
        tv_gcb_num.setText(context.getString(R.string.gcb_display, gcbBigDecimal));
        tv_money.setText(String.valueOf(context.getString(R.string.rmb_display, totalPrice)));
        tv_num.setText(String.valueOf(count));
        tv_food_title.setText(shopCarInfo.getP_name());
        String machineName = shopCarInfo.getP_machine();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        tv_machine_title.setText(String.format(context.getString(R.string.machine_name),"成都",machineNum));

        ImageOptions imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.ic_launcher)
                .build();
        String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.BASE_URL + shopCarInfo.getP_picture());
        x.image().bind(img_food, imgPath, imageOptions);

        tv_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                tv_num.setText(String.valueOf(count));
                shopCarInfo.setP_local_number(count);
                totalPrice = shopCarInfo.getP_price() * count;
                tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
                tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
                tv_money.setText(String.valueOf(context.getString(R.string.rmb_display, totalPrice)));
                totalGcb = shopCarInfo.getP_vr9() * count;
                gcbBigDecimal = BigDecimal.valueOf(totalGcb);
                tv_gcb_num.setText(context.getString(R.string.gcb_display, gcbBigDecimal));
            }
        });
        tv_btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 1) {
                    count--;
                    tv_num.setText(String.valueOf(count));
                    shopCarInfo.setP_local_number(count);
                    totalPrice = shopCarInfo.getP_price() * count;
                    tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
                    tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
                    tv_money.setText(String.valueOf(context.getString(R.string.rmb_display, totalPrice)));
                    totalGcb = shopCarInfo.getP_vr9() * count;
                    gcbBigDecimal = BigDecimal.valueOf(totalGcb);
                    tv_gcb_num.setText(context.getString(R.string.gcb_display, gcbBigDecimal));
                }
            }
        });
    }

}
