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
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.PasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jetcloud.hgbw.fragment.ShopCarFragment.MACHINE_OBJECT;
import static com.jetcloud.hgbw.fragment.ShopCarFragment.PRODUC_OBJECT;

public class PayNextActivity extends BaseActivity {
    private final static String TAG_LOG = PayNextActivity.class.getSimpleName();
    private List<ShopCarInfo> listObj;
    private ShopCarInfo shopCarInfo;
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private TextView tv_price;
    private CustomProgressDialog progress;
    private Button btn_pay;
    private double money;
    private String payWay;
    private String orderNum;
    private BigDecimal bdGcb;
    private String intentResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(com.jetcloud.hgbw.R.layout.activity_comfirm_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("支付");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_price = getView(R.id.tv_price);
        btn_pay = getViewWithClick(R.id.btn_pay);
        //产生订单号
        orderNum = String.valueOf(System.currentTimeMillis());
    }

    @Override
    protected void loadData() {
        Intent i = getIntent();
        children = (Map<String, List<ShopCarInfo>>) i.getSerializableExtra(PRODUC_OBJECT);
        groups = (List<MachineInfo>) i.getSerializableExtra(MACHINE_OBJECT);
        money = i.getDoubleExtra("money",0.00);
        payWay  = i.getStringExtra("payway");
        if (i.hasExtra("resource")) {
            intentResource = i.getStringExtra("resource");
        }
//        for (int i = 0; i < groups.size(); i++) {
//            listObj = children.get(groups.get(i).getId());
//        }
//        Log.i(TAG_LOG, "initView: " + listObj.size());
//        //只有一件商品
//        if (listObj != null) {
//            shopCarInfo = listObj.get(0);
//            topbar.setCenterText(shopCarInfo.getP_name());
//            float totalNum = shopCarInfo.getP_price() * shopCarInfo.getP_local_number();
//            tv_price.setText(getString(R.string.pay_money, totalNum));
//        }
    }

    @Override
    public void onClick(View view) {
        final PasswordDialog passwordDialog;
        if (view.getId() == R.id.btn_pay){
            //如果是vr9
            if (payWay.equals("vr9")) {
                passwordDialog = new PasswordDialog(PayNextActivity.this, String.format(PayNextActivity.this.getString(R.string.gcb_display),money));
                passwordDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                passwordDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Log.i(TAG_LOG, "balance_type: " + payWay);
//                        Log.i(TAG_LOG, "mobile: " + SharedPreferenceUtils.getTradeAccount());
//                        Log.i(TAG_LOG, "paypwd: " + passwordDialog.getPasswordInputView().toString());
//                        Log.i(TAG_LOG, "source_id: " + orderNum);
                        bdGcb = BigDecimal.valueOf(money);
//                        Log.i(TAG_LOG, "amount: " + bdGcb);
                        getNetData(passwordDialog.getPasswordInputView().toString(), orderNum, String.valueOf(bdGcb));
                    }
                });
                passwordDialog.show();
            }
        }
    }

    /**
     * 处理json数据
     * {"status": "200", "identity": "visitor", "message": "\u8ba2\u5355\u652f\u4ed8\u6210\u529f"}
     * getDataFromJson: 订单支付成功
     *
     */
    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        Log.i(TAG_LOG, "getDataFromJson: " + jsonObject.getString("message"));
        Out.Toast(PayNextActivity.this, jsonObject.getString("message"));
        if (jsonObject.getString("status").equals("200")){
            uploadOrder();
        }
    }

    /**
     * 支付
     * */
    private void getNetData(String paypwd, String orderNum, String money) {
        final RequestParams params = new RequestParams(HgbwUrl.TRADE_PAY);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", SharedPreferenceUtils.getTradeAccount());
            jsonObject.put("paypwd", paypwd);
            jsonObject.put("source", "android_hgbw");
            jsonObject.put("source_id", orderNum);
            jsonObject.put("amount", money);
            jsonObject.put("balance_type", payWay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObject.toString());
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
                        return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
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
    /***
     * 上传订单号，以及相关信息，未完成
     * */
    private void uploadOrder() {
        final RequestParams params = new RequestParams(HgbwUrl.TRADE_PAY);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", SharedPreferenceUtils.getTradeAccount());
            jsonObject.put("source", "android_hgbw");
            jsonObject.put("source_id", orderNum);
            jsonObject.put("amount", money);
            jsonObject.put("balance_type", payWay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(jsonObject.toString());
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
                        return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
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
