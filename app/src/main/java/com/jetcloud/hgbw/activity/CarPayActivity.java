package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.PayBuyingAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CusAlertDialogWithTwoBtn;
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
    private RelativeLayout activity_car_pay;
    private MyExpandableListView elv_buying;
    private MyListView lv_my_ticket;
    private int totalNum;
    private TextView tv_go_to_pay;
    private double totalPrice;
    private double totalGcb;
    private List<String> wayData;
    private HgbwApplication app;
    private CusAlertDialogWithTwoBtn alert;

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
//        activity_car_pay = getView(R.id.activity_car_pay);
//        activity_car_pay.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {

        groups = app.getGroups();
        children = app.getChildren();
        totalPrice = app.getTotalPrice();
        totalGcb = app.getTotalGcb();

//        for (int i = 0; i < groups.size(); i ++) {
//            List<ShopCarInfo> scList = children.get(groups.get(i).getId());
//            for (int j = 0; j < scList.size(); j ++){
//                ShopCarInfo shopCarInfo = scList.get(i);
//                totalPrice += totalNum * shopCarInfo.getP_price();
//                totalNum += shopCarInfo.getP_local_number();
//                Log.i(TAG_LOG, "loadData: " +  totalPrice+ " "  + totalNum);
//            }
//        }
/**
 wayData = new ArrayList<>();
 wayData.add("20元卡券一张，可抵扣20元");
 wayData.add("40元卡券一张，可抵扣40元");
 lv_my_ticket.setAdapter(new PayTicketAdapter(this, wayData));
 */
        buyingAdapter = new PayBuyingAdapter(CarPayActivity.this, groups, children);
        elv_buying.setAdapter(buyingAdapter);
        elv_buying.setGroupIndicator(null);
        for (int i = 0; i < buyingAdapter.getGroupCount(); i++) {
            elv_buying.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }

//        tv_gcb_num.setText();
        //总价格： 默认显示GCB价格
        tv_total_price.setText(context.getString(R.string.take_food_gcb_total, totalGcb));
        tv_wei_num.setText(context.getString(R.string.rmb_display, totalPrice));
        BigDecimal db = BigDecimal.valueOf(totalGcb);
//        Log.i(TAG_LOG, "initListData: " + db);
        tv_gcb_num.setText(context.getString(R.string.gcb_display, db));
    }

    @Override
    public void onClick(View view) {
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
        } else if (view.getId() == R.id.tv_go_to_pay) {
            if (cb_gcb.isChecked()) {
                app.setType(HgbwStaticString.PAY_WAY_VR9);
                if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)) {
                    alert = new CusAlertDialogWithTwoBtn(context);
                    alert.setTitle("操作提示");
                    alert.setContent("您还未绑定交易宝账号");
                    alert.setNegativeButton("取消",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            });
                    alert.setPositiveButton("去绑定",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    Intent intent = new Intent(CarPayActivity.this, BindingActivity.class);
                                    intent.putExtra(HgbwStaticString.JUMP_RESOURCE, CarPayActivity
                                            .class.getSimpleName());
                                    startActivity(intent);
                                }
                            });
                    alert.show();
                } else {
                    Intent i = new Intent(CarPayActivity.this, PayNextActivity.class);
                    i.putExtra(HgbwStaticString.JUMP_RESOURCE, CarPayActivity.class.getSimpleName());
                    startActivity(i);
                }
            } else {
                app.setType(HgbwStaticString.PAY_WAY_CNY);
                SharedPreferenceUtils.setCarpay(SharedPreferenceUtils.WITH_CARPAY);
                Intent i = new Intent(CarPayActivity.this, PayNextActivity.class);
                i.putExtra(HgbwStaticString.JUMP_RESOURCE, CarPayActivity.class.getSimpleName());
                startActivity(i);

            }
        }
    }

    /****
     * 获取商品数量
     public void getFoodNumRequest(int foodId) {

     final RequestParams params = new RequestParams(HgbwUrl.FOOD_DETAIL_URL);
     //缓存时间
     params.addQueryStringParameter("food_id", String.valueOf(foodId));
     params.addQueryStringParameter("mechine_number", groups.get(0).getNumber());
     params.setCacheMaxAge(1000 * 60);
     x.task().run(new Runnable() {
    @Override public void run() {
    x.http().get(params, new Callback.CacheCallback<String>() {

    private boolean hasError = false;
    private String result = null;

    @Override public boolean onCache(String result) {
    this.result = result;
    return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
    }

    @Override public void onSuccess(String result) {
    // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
    if (result != null) {
    this.result = result;
    }
    }

    @Override public void onError(Throwable ex, boolean isOnCallback) {
    hasError = true;
    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
    Log.e(TAG_LOG, "getFoodNumRequest onError: " + ex.getMessage());
    if (ex instanceof HttpException) { // 网络错误
    HttpException httpEx = (HttpException) ex;
    int responseCode = httpEx.getCode();
    String responseMsg = httpEx.getMessage();
    String errorResult = httpEx.getResult();
    Log.e(TAG_LOG, "getFoodNumRequest onError " + " code: " + responseCode + " message: " + responseMsg);
    } else { // 其他错误

    }
    }

    @Override public void onCancelled(CancelledException cex) {
    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
    }

    @Override public void onFinished() {
    if (!hasError && result != null) {
    Log.i(TAG_LOG, "getFoodNumRequest onFinished: " + result);
    getDataFromJson(result);
    }
    }
    });
    }
    });
     }
     */
    /**
     * 获取商品数量
     * */
    /***
     public void getDataFromJson(String result) {
     Gson gson = new Gson();
     final CusAlertDialog cusAlertDialog;
     List<ShopCarInfo> nullNumList = null;
     FoodDetailBean foodDetailBean = gson.fromJson(result, FoodDetailBean.class);
     FoodDetailBean.InfoBean infoBean = foodDetailBean.getInfo();
     if (foodDetailBean.getStatus().equals("success")) {
     String machineNum = groups.get(0).getNumber();
     List<ShopCarInfo> shopCarInfoList = children.get(machineNum);
     for (int i = 0; i < shopCarInfoList.size(); i ++) {
     ShopCarInfo info = shopCarInfoList.get(i);
     Log.i(TAG_LOG, "local_food_num: " + info.getP_local_number());
     Log.i(TAG_LOG, "remote_food_num: " + infoBean.getNum());
     if (info.getP_local_number() <= infoBean.getNum()) {
     nullNumList = new ArrayList<>();
     nullNumList.add(info);
     }
     }

     StringBuffer title;
     cusAlertDialog = new CusAlertDialog(CarPayActivity.this);
     if (nullNumList != null) {
     title = new StringBuffer("结算失败\n");

     for (int i = 0; i < nullNumList.size() - 1; i++) {
     title.append(nullNumList.get(i).getName() + "、");
     }
     title.append(nullNumList.get(nullNumList.size() - 1).getName() + "已经下架");
     cusAlertDialog.setTitle(title.toString());
     cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
    @Override public void onClick(View view) {
    cusAlertDialog.dismiss();
    }
    });
     cusAlertDialog.show();
     } else {

     }
     }
     }*/
}
