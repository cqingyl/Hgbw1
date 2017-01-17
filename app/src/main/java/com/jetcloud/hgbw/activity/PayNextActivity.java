package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CusAlertDialog;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.PasswordDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private MachineInfo machineInfo;
    private TextView tv_price;
    private CustomProgressDialog progress;
    private Button btn_pay;
    private double money;
    private String payWay;
    private int payWayNum;
    private final static int  PAY_WAY_VR9 = 10;
    private final static int  PAY_WAY_CNY = 20;
    private String orderNum;
    private String type;
    private HgbwApplication app;
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
    }

    @Override
    protected void loadData() {
        groups = app.getGroups();
        machineInfo = groups.get(0);
        children = app.getChildren();
        foodList = children.get(machineInfo.getNumber());
        payWay  = app.getType();
        if (payWay.equals(PayNextActivity.this.getString(R.string.pay_way_gcb))) {
            money = app.getTotalGcb();
            if (children != null) {
                tv_price.setText(getString(R.string.gcb_display, money));
                payWayNum = PAY_WAY_VR9;
            }
        } else {
            money =  app.getTotalPrice();
            if (children != null) {
                tv_price.setText(getString(R.string.pay_money, money));
                payWayNum = PAY_WAY_CNY;
            }
        }
    }


    @Override
    public void onClick(View view) {
        final PasswordDialog passwordDialog;
        if (view.getId() == R.id.btn_pay){

            //如果是vr9
            if (payWay.equals(PayNextActivity.this.getString(R.string.pay_way_gcb))) {

                passwordDialog = new PasswordDialog(PayNextActivity.this, String.format(PayNextActivity.this.getString(R.string.gcb_display),money));
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
                            Log.i(TAG_LOG, "pay_type: " + payWayNum);
                            Log.i(TAG_LOG, "mechine_number: " + app.getGroups().get(0).getNumber());
                            Log.i(TAG_LOG, "pay_pwd: " + paypwd);
                            Log.i(TAG_LOG, "foods size: " + foodList.size());
                            passwordDialog.dismiss();
                            getNetData(String.valueOf(money), paypwd, foodList);
                        }
                    }
                });
                passwordDialog.show();

            }
        }
    }

    /**
     * 处理json数据
     *  {"identity": "visitor", "status": "200", "message": "\u8ba2\u5355\u652f\u4ed8\u6210\u529f"}订单支付成功
     *  {"msg": "\u5269\u4f59\u51fa\u9910\u91cf\u4e0d\u591f", "status": "fail", "remain": 0, "food_id": "14"}
     */
    private void getDataFromJson(String result) throws JSONException {
        final CusAlertDialog cusAlertDialog;
        final JSONObject jsonObject = new JSONObject(result);
        Log.i(TAG_LOG, "getDataFromJson: " + jsonObject.getString("message"));
        Out.Toast(PayNextActivity.this, jsonObject.getString("status"));
            cusAlertDialog = new CusAlertDialog(PayNextActivity.this);
        //成功
        if (jsonObject.getString("status").equals("200")) {
            cusAlertDialog.setTitle(jsonObject.getString("message"));
            cusAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cusAlertDialog.dismiss();
//                    startActivityForResult(new Intent(PayNextActivity.this, MainActivity.class), MainActivity.PAY_SUCCESS_REQUEST);
                    finish();
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
                    app.removeAll();
                    Intent intent = new Intent(PayNextActivity.this, MainActivity.class);
                    intent.putExtra(PayNextActivity.this.getString(R.string.jump_resource), PayNextActivity
                            .class.getSimpleName());
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
    private void getNetData(String money, String paypwd, List<ShopCarInfo> foodBeanList) {
        final RequestParams params = new RequestParams(HgbwUrl.PAY_URL);
//        JSONObject jsonObject = null;
        JSONArray foodArray = null;
        try {
            foodArray = new JSONArray();
            for (int i = 0; i < foodBeanList.size(); i++) {
                JSONObject foodObj = new JSONObject();
                ShopCarInfo shopCarInfo = foodBeanList.get(i);
                foodObj.put("id", String.valueOf(shopCarInfo.getId()));
                foodObj.put("num", String.valueOf(shopCarInfo.getP_local_number()));
                Log.i(TAG_LOG, "id: " + String.valueOf(shopCarInfo.getId()));
                Log.i(TAG_LOG, "num: " + String.valueOf(shopCarInfo.getP_local_number()));
                foodArray.put(i, foodObj);
            }
//            jsonObject = new JSONObject();
//            jsonObject.put("foods", foodArray);
//            jsonObject.put("pay_type", String.valueOf(payWayNum));
//            jsonObject.put("cost_total", money);
//            jsonObject.put("pay_pwd", paypwd);
//            jsonObject.put("mechine_number",  app.getGroups().get(0).getNumber());
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
//        String foodString = stringArray.toString().replace("\"", "");
            params.addBodyParameter("pay_type", String.valueOf(payWayNum));
            params.addBodyParameter("cost_total", money);
            params.addBodyParameter("pay_pwd", paypwd);
            params.addBodyParameter("cost_real", money);
            params.addBodyParameter("mechine_number", app.getGroups().get(0).getNumber());
            params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
            params.addBodyParameter("foods", String.valueOf(stringArray));
//        if (jsonObject != null)
//            params.setBodyContent(jsonObject.toString());
            Log.i(TAG_LOG, "getNetData jsonObject: " + stringArray.toString());
            Log.i(TAG_LOG, "getNetData params: " + params.toJSONString());
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
                            Log.e(TAG_LOG, "onError: " + ex.getMessage());
                            if (ex instanceof HttpException) { // 网络错误
                                HttpException httpEx = (HttpException) ex;
                                int responseCode = httpEx.getCode();
                                String responseMsg = httpEx.getMessage();
                                String errorResult = httpEx.getResult();
                                Log.e(TAG_LOG, "onError " + " code: " + responseCode + " message: " + responseMsg);
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
                                Log.i(TAG_LOG, "onFinished: " + result);
                                try {
                                    getDataFromJson(result);
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

}
