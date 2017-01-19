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
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPayActivity extends BaseActivity {

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
    @ViewInject(R.id.tv_price_cny)
    private TextView tv_price_cny;
    @ViewInject(R.id.tv_price_vr9)
    private TextView tv_price_vr9;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;

    private List<ShopCarInfo> listObj;
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private List<MachineInfo> groups = new ArrayList<>();
    private MachineInfo machineInfo;
    private List<String> wayData;
    private double totalPrice;
    private double totalGcb;
    private ShopCarInfo shopCarInfo;
    private HgbwApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail_pay);
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
            tv_total_price.setText(context.getString(R.string.take_food_gcb_total, totalGcb));
        } else if (view.getId() == R.id.cb_weixin){
            cb_gcb.setChecked(false);
            cb_weixin.setChecked(true);
            cb_gcb.setClickable(true);
            cb_weixin.setClickable(false);
            tv_total_price.setText(context.getString(R.string.take_food_total, totalPrice));
        } else if (view.getId() == R.id.tv_go_to_pay) {
            app.setTotalPrice(totalPrice);
            app.setTotalGcb(totalGcb);
            if (cb_gcb.isChecked()) {
                app.setType(HgbwStaticString.PAY_WAY_VR9);
            } else {
                app.setType(HgbwStaticString.PAY_WAY_WEIXIN);
                Out.Toast(DetailPayActivity.this, "暂未开通，敬请期待");
                return;
            }
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
                                Intent intent = new Intent(DetailPayActivity.this, BindingActivity.class);
                                intent.putExtra(HgbwStaticString.JUMP_RESOURCE, CarPayActivity
                                        .class.getSimpleName());
                                startActivity(intent);
                            }
                        });
                alert.show();
            } else {
//                        for (int i = 0; i < children.get(groups.get(0).getNumber()).size(); i ++) {
//                            ShopCarInfo shopCarInfo = children.get(groups.get(0).getNumber()).get(i);
//                            shopCarInfo.getId();
////                            Log.i(TAG_LOG, "onClick: " + shopCarInfo.getId());
////                            getFoodNumRequest(shopCarInfo.getId());
//
//                        }
                Intent i = new Intent(DetailPayActivity.this, PayNextActivity.class);
                startActivity(i);
            }

        }
    }
    @Override
    protected void loadData() {
        initListData();
    }

    private void initListData() {
        /***
        wayData = new ArrayList<>();
        wayData.add("20元卡券一张，可抵扣20元");
        wayData.add("40元卡券一张，可抵扣40元");
        lv_my_ticket.setAdapter(new PayTicketAdapter(this, wayData));*/

        //只有一种商品
        groups = app.getGroups();
        machineInfo = groups.get(0);
        listObj = app.getChildren().get(machineInfo.getNumber());
        Log.i(TAG_LOG, "initView: " + listObj.size());
        shopCarInfo = listObj.get(0);
        int count = shopCarInfo.getP_local_number();
        totalPrice = shopCarInfo.getPrice_cny() * count;
        totalGcb = shopCarInfo.getPrice_vr9() * count;
        //默认显示GCB
        tv_total_price.setText(context.getString(R.string.take_food_gcb_total, totalGcb));
        tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
        totalGcb = shopCarInfo.getPrice_vr9() * count;
//        Log.i(TAG_LOG, "initListData: " + totalGcb);
        tv_gcb_num.setText(context.getString(R.string.gcb_display, totalGcb));
        tv_price_cny.setText(String.valueOf(context.getString(R.string.rmb_display, totalPrice)));
        tv_price_vr9.setText(String.valueOf(context.getString(R.string.gcb_display, totalGcb)));
        tv_num.setText(String.format(getString(R.string.take_food_num), count));
        tv_food_title.setText(shopCarInfo.getName());
        String machineName = machineInfo.getNickname();
        String machineLocation = machineInfo.getCity();
        /**String machineNum = machineName.substring(machineName.length() - 3, machineName.length());*/
        tv_machine_title.setText(String.format(context.getString(R.string.machine_name), machineLocation, machineName));


        ImageOptions imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.ic_launcher)
                .build();
        String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + shopCarInfo.getPic());
        x.image().bind(img_food, imgPath, imageOptions);


    }

}
