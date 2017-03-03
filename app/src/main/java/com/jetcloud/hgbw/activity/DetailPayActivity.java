package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CusAlertDialogWithTwoBtn;

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
    @ViewInject(R.id.activity_details_pay)
    private RelativeLayout activity_details_pay;

    private List<ShopCarInfo> listObj;
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private List<MachineInfo> groups = new ArrayList<>();
    private String machineNum;
    private List<String> wayData;
    private double totalPrice;
    private double totalGcb;
    private ShopCarInfo shopCarInfo;
    private HgbwApplication app;
    private CusAlertDialogWithTwoBtn alert;

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

        activity_details_pay = getView(R.id.activity_details_pay);
//        activity_details_pay.setBackgroundResource(R.drawable.mine_bg);
    }

    @Event(value = {R.id.cb_gcb, R.id.cb_weixin, R.id.tv_go_to_pay})
    private void onPayWayCheckedChanged(View view) {

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
                if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)) {
                    alert = new CusAlertDialogWithTwoBtn(context);
                    alert.setTitle("操作提示");
                    alert.setContent("您还未绑定交易宝账号");
                    alert.setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            }
                    );
                    alert.setPositiveButton("去绑定",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    Intent intent = new Intent(DetailPayActivity.this, BindingActivity.class);
                                    intent.putExtra(HgbwStaticString.JUMP_RESOURCE, CarPayActivity
                                            .class.getSimpleName());
                                    startActivity(intent);
                                }
                            });
                    alert.show();
                } else {
                    Intent i = new Intent(DetailPayActivity.this, PayNextActivity.class);
                    startActivity(i);
                }
            } else {
                app.setType(HgbwStaticString.PAY_WAY_CNY);
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
        machineNum = SharedPreferenceUtils.getMachineNum();
        listObj = app.getChildren().get(machineNum);
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
        String machineLocation = SharedPreferenceUtils.getMachineNickName();
        /**String machineNum = machineName.substring(machineName.length() - 3, machineName.length());*/
        tv_machine_title.setText(machineLocation);


        ImageOptions imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.ic_launcher)
                .build();
        String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + shopCarInfo.getPic());
        x.image().bind(img_food, imgPath, imageOptions);


    }

}
