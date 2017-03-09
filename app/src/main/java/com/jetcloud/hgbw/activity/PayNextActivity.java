package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.JumpUtils;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CusAlertDialog;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.PasswordDialog;
import com.jetcloud.hgbw.wxapi.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayNextActivity extends BaseActivity {
    private final static String TAG_LOG = PayNextActivity.class.getSimpleName();
    private List<ShopCarInfo> foodList;
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private String machineNum;
    private TextView tv_price;
    private CustomProgressDialog progress;
    private Button btn_pay;
    private LinearLayout activity_comfirm_pay;
    private double money;
    private String payWay;
    private String orderNum;
    private HgbwApplication app;
    public static IWXAPI api;
    private PayReq req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(com.jetcloud.hgbw.R.layout.activity_comfirm_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        app = (HgbwApplication) getApplication();
        topbar.setCenterText("支付");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_price = getView(R.id.tv_price);
        btn_pay = getViewWithClick(R.id.btn_pay);

        activity_comfirm_pay = getView(R.id.activity_comfirm_pay);
//        activity_comfirm_pay.setBackgroundResource(R.drawable.mine_bg);

//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
    }

    @Override
    protected void loadData() {
        machineNum = SharedPreferenceUtils.getMachineNum();
        children = app.getChildren();
        foodList = children.get(machineNum);
        payWay  = app.getType();
        if (payWay.equals(HgbwStaticString.PAY_WAY_VR9)) {
            money = app.getTotalGcb();
            if (children != null) {
                tv_price.setText(getString(R.string.gcb_display, money));
//                payWayNum = HgbwStaticString.PAY_WAY_VR9;
            }
        } else if (payWay.equals(HgbwStaticString.PAY_WAY_CNY)){
            money =  app.getTotalPrice();
            if (children != null) {
                tv_price.setText(getString(R.string.pay_money, money));
//                payWayNum = HgbwStaticString.PAY_WAY_CNY;
            }
        }
    }


    @Override
    public void onClick(View view) {
        final PasswordDialog passwordDialog;
        if (view.getId() == R.id.btn_pay){

            if (payWay.equals(HgbwStaticString.PAY_WAY_VR9)) {
                passwordDialog = new PasswordDialog(PayNextActivity.this, String.format(PayNextActivity.this.getString(R.string.gcb_display), money));
                passwordDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordDialog.dismiss();
                    }
                });
                passwordDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String paypwd = passwordDialog.getPasswordInputView().toString();
                        if (paypwd.length() == 6) {
                            Log.i(TAG_LOG, "cost_total: " + money);
                            Log.i(TAG_LOG, "cost_real: " + money);
                            Log.i(TAG_LOG, "pay_type: " + payWay);
                            Log.i(TAG_LOG, "mechine_number: " + machineNum);
                            Log.i(TAG_LOG, "pay_pwd: " + paypwd);
                            Log.i(TAG_LOG, "foods size: " + foodList.size());
                            passwordDialog.dismiss();
                            payRequest(String.valueOf(money), paypwd, foodList);
                        }
                    }
                });
                passwordDialog.show();
            } else if (payWay.equals(HgbwStaticString.PAY_WAY_CNY)){
                payRequest(String.valueOf(money), null, foodList);
            }

        }
    }

    /**
     * 处理json数据
     *  {"identity": "visitor", "status": "200", "message": "\u8ba2\u5355\u652f\u4ed8\u6210\u529f"}订单支付成功
     *  {"msg": "\u5269\u4f59\u51fa\u9910\u91cf\u4e0d\u591f", "status": "fail", "remain": 0, "food_id": "14"}
     */
    private void getVR9PayDataFromJson(String result) throws JSONException {
        JumpUtils.check405(PayNextActivity.this, result);
        final CusAlertDialog cusAlertDialog;
        final JSONObject jsonObject = new JSONObject(result);
        Log.i(TAG_LOG, "getPayDataFromJson: " + jsonObject.getString("message"));
        Out.Toast(PayNextActivity.this, jsonObject.getString("status"));
            cusAlertDialog = new CusAlertDialog(PayNextActivity.this);

        //成功
        if (jsonObject.getString("status").equals("200")) {
            cusAlertDialog.setTitle(jsonObject.getString("message"));
            cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    //如来自购物车购买
                    if (intent.hasExtra(HgbwStaticString.JUMP_RESOURCE)){
                        String jumpResource = getIntent().getStringExtra(HgbwStaticString.JUMP_RESOURCE);
                        if (jumpResource.equals(CarPayActivity.class.getSimpleName())){
                            for (int i = 0; i < foodList.size(); i ++) {
                                WhereBuilder whereBuilder = WhereBuilder.b("id", "=", foodList.get(i).getId());
                                try {
                                    app.db.delete(ShopCarInfo.class,whereBuilder);
                                        Log.i(TAG_LOG, "onClick ShopCarInfo size ----->>>>>" + app.db.selector(ShopCarInfo.class).findAll().size());
                                    if (app.db.selector(ShopCarInfo.class).findAll().size() == 0){
                                        app.db.delete(MachineInfo.class);
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            int oldNum = SharedPreferenceUtils.getShopCarNumber();
                            int newNum = oldNum - foodList.size();
                            if (newNum > 0) {
                                SharedPreferenceUtils.setShopCarNumber(newNum);
                            } else {
                                SharedPreferenceUtils.setShopCarNumber(0);
                            }
                        }
                    }
                    cusAlertDialog.dismiss();
                    Intent i = new Intent(PayNextActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(HgbwStaticString.JUMP_RESOURCE, PayNextActivity
                            .class.getSimpleName());
                    startActivity(i);
                }
            });
            //失败
        } else if (jsonObject.getString("status").equals("fail")) {
            cusAlertDialog.setTitle("支付失败");
            cusAlertDialog.setContent(jsonObject.getString("message"));
            cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cusAlertDialog.dismiss();
                    Intent intent = new Intent(PayNextActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } else if (jsonObject.has("code") && jsonObject.getString("code").equals("500")) {
            cusAlertDialog.setTitle("支付失败");
            cusAlertDialog.setContent(jsonObject.getString("message"));
            cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cusAlertDialog.dismiss();
                    Intent intent = new Intent(PayNextActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
        cusAlertDialog.show();

    }

    /**
     * 支付
     * /user/buyfood
     * pay_type =  ('10' VR9) or ('20' CNY)
     * foods = [{'id': 3, 'num': 2}, ....]
     * cost_total = 3
     * cost_real 真实价格
     * mechine_number = XXXX
     * */
    private void payRequest(String money, final String paypwd, List<ShopCarInfo> foodBeanList) {
        final RequestParams params = new RequestParams(HgbwUrl.PAY_URL);
        JSONArray foodArray = null;
        try {
            foodArray = new JSONArray();
            for (int i = 0; i < foodBeanList.size(); i++) {
                JSONObject foodObj = new JSONObject();
                ShopCarInfo shopCarInfo = foodBeanList.get(i);
                foodObj.put("id", String.valueOf(shopCarInfo.getId()));
                foodObj.put("num", String.valueOf(shopCarInfo.getP_local_number()));
                foodObj.put("food_name", String.valueOf(shopCarInfo.getName()));
                foodObj.put("food_pay_way", payWay);
                foodObj.put("food_pic", String.valueOf(shopCarInfo.getPic()));
                foodObj.put("food_price_vr9", String.valueOf(shopCarInfo.getPrice_vr9()));
                foodObj.put("food_price_rmb", String.valueOf(shopCarInfo.getPrice_cny()));
                foodObj.put("food_des", String.valueOf(shopCarInfo.getDescription()));
                Log.i(TAG_LOG, "food_id: " + String.valueOf(shopCarInfo.getId()));
                Log.i(TAG_LOG, " food_num: " + String.valueOf(shopCarInfo.getP_local_number()));
                Log.i(TAG_LOG, "food_name: " + String.valueOf(shopCarInfo.getName()));
                Log.i(TAG_LOG, "food_price: " + money);
                Log.i(TAG_LOG, "food_pay_way: " + payWay);
                Log.i(TAG_LOG, "food_price_vr9: " + String.valueOf(shopCarInfo.getPrice_vr9()));
                Log.i(TAG_LOG, "food_price_rmb: " + String.valueOf(shopCarInfo.getPrice_cny()));
                Log.i(TAG_LOG, "food_des: " + String.valueOf(shopCarInfo.getDescription()));
                foodArray.put(i, foodObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> stringArray = new ArrayList<String>();
        for (int i = 0, count = foodArray.length(); i < count; i++) {
            try {
                JSONObject jsonObject = foodArray.getJSONObject(i);
                stringArray.add(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG_LOG, "payRequest: \npayWay: " + payWay + "\nmoney: " + money + "\npaypwd: " + paypwd + "\nmachineNum: " + machineNum + "\nmachineName: "+ SharedPreferenceUtils.getMachineNickName());
            params.addBodyParameter("pay_type", payWay);
            params.addBodyParameter("cost_total", money);
            params.addBodyParameter("cost_real", money);
            params.addBodyParameter("pay_pwd", paypwd);
            params.addBodyParameter("mechine_number", machineNum);
            params.addBodyParameter("mechine_name", SharedPreferenceUtils.getMachineNickName());
            params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
            params.addBodyParameter("foods", String.valueOf(stringArray));

            Log.i(TAG_LOG, "getNetData jsonObject: " + stringArray.toString());
//            Log.i(TAG_LOG, "getNetData params: " + params.toJSONString());
            params.setCacheMaxAge(1000 * 60);

            x.task().run(new Runnable() {
                @Override
                public void run() {
                    x.http().post(params, new Callback.CacheCallback<String>() {

                        private boolean hasError = false;
                        private String result = null;


                        @Override
                        public boolean onCache(String result) {
                            this.result = result;
                            return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                        }

                        @Override
                        public void onSuccess(String result) {
                            // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                            if (result != null) {
                                this.result = result;
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            hasError = true;
                            Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e(TAG_LOG, "payRequest onError: " + ex.getMessage());
                            if (ex instanceof HttpException) { // 网络错误
                                HttpException httpEx = (HttpException) ex;
                                int responseCode = httpEx.getCode();
                                String responseMsg = httpEx.getMessage();
                                String errorResult = httpEx.getResult();
                                Log.e(TAG_LOG, "payRequest onError " + " code: " + responseCode + " message: " + responseMsg);
                            } else { // 其他错误
                            }
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinished() {
                            progress.dismiss();
                            if (!hasError && result != null) {
                                Log.i(TAG_LOG, "payRequest onFinished: " + result);
                                try {
                                    JSONObject json = new JSONObject(result);
                                    if(json.getString("status").equals("fail")) {
                                        final CusAlertDialog cusAlertDialog = new CusAlertDialog(PayNextActivity.this);
                                        cusAlertDialog.setTitle("支付失败");
                                        cusAlertDialog.setContent(json.getString("message"));
                                        cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                cusAlertDialog.dismiss();
                                                Intent intent = new Intent(PayNextActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                                        cusAlertDialog.show();
                                    } else {
                                        if (payWay != null && payWay.equals(HgbwStaticString.PAY_WAY_VR9)){
                                            getVR9PayDataFromJson(result);
                                        } else if (payWay != null && payWay.equals(HgbwStaticString.PAY_WAY_CNY)) {
                                            requestWxPay(result);
    //                                        startActivity(new Intent(PayNextActivity.this, WXPayEntryActivity.class));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e(TAG_LOG, " json error: " + e.getMessage());
                                }
                            }
                        }

                    });
                    x.task().post(new Runnable() {
                        @Override
                        public void run() {
                            progress = new CustomProgressDialog(PayNextActivity.this, "请稍后", R.drawable.fram2);
                            progress.show();
                        }
                    });
                }
            });

        }


/**
     * 微信支付
     * */
    public void requestWxPay(final String result) throws JSONException {
        final CusAlertDialog cusAlertDialog = new CusAlertDialog(PayNextActivity.this);
        Button payBtn = (Button) findViewById(R.id.btn_pay);
        payBtn.setEnabled(false);
        JSONObject json = new JSONObject(result);
        if(json.getString("status").equals("fail")) {
            cusAlertDialog.setTitle("支付失败");
            cusAlertDialog.setContent(json.getString("message"));
            cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cusAlertDialog.dismiss();
                    Intent intent = new Intent(PayNextActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            cusAlertDialog.show();
        } else {

    //        x.task().run(new Runnable() {
    //            @Override
    //            public void run() {
                    try{
                        if (api != null) {
                            if (api.isWXAppInstalled()) {
                                if(!json.has("retcode") ){
                                    PayReq req = new PayReq();
                                    //{   }
                                    req.appId			= json.getString("appid");
                                    req.partnerId		= json.getString("partnerid");
                                    req.prepayId		= json.getString("prepayid");
                                    req.nonceStr		= json.getString("noncestr");
                                    req.timeStamp		= json.getString("timestamp");
                                    req.packageValue	= json.getString("package");
                                    req.sign			= json.getString("sgin");
                                    String s = MD5.md5("appid="+req.appId+
                                            "&noncestr="+req.nonceStr+
                                            "&package=Sign=WXPay"+
                                            "&partnerid="+req.partnerId+
                                            "&prepayid="+req.prepayId+
                                            "&timestamp="+req.timeStamp+"&key=f6dc74a523185690019a43fdeaf12596").toUpperCase();
                                    Log.i(TAG_LOG, "requestWxPay: " + s);
                                    // 将该app注册到微信
                                    api.registerApp(Constants.WX_APP_ID);
                                    Toast.makeText(PayNextActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    Log.i(TAG_LOG, "requestWxPay: " + api.sendReq(req));
    //                                if (!api.sendReq(req)) {
    //                                    x.task().post(new Runnable() {
    //                                        @Override
    //                                        public void run() {
    //                                            Out.Toast(PayNextActivity.this, "微信未响应");
    //                                        }
    //                                    });
    //                                }

                                }else{
                                    Log.i(TAG_LOG, "返回错误: "+json.getString("retmsg"));
                                    Toast.makeText(PayNextActivity.this, "返回错误 : " + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }catch(Exception e){
                        Log.e(TAG_LOG, "异常: " + e.getMessage());
                        Toast.makeText(PayNextActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
    //            }
    //        });
        }
            payBtn.setEnabled(true);
    }
}
