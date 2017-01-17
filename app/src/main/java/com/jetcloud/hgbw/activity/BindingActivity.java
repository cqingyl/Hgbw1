package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BindingActivity extends BaseActivity {

    private final static String TAG_LOG = BindingActivity.class.getSimpleName();
    private EditText et_user_count;
    private CustomProgressDialog progress;
    private TextView tv_btn_ok, tv_myaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_binding);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        topbar.setCenterText("绑定账号");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_myaccount = getView(R.id.tv_myaccount);
        tv_btn_ok = getViewWithClick(R.id.tv_btn_ok);
        et_user_count = getView(R.id.et_user_count);

        tv_myaccount.setText(SharedPreferenceUtils.getMyAccount());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_ok:
                if (et_user_count.getText().toString().length() == 11) {
//                Intent i = new Intent(BindingActivity.this, BindNextActivity.class);
//                i.putExtra(JIAOYIBAO_ACCOUNT, et_user_count.getText().toString());
//                startActivity(i);
//                finish();
                    bindRequest();
                } else {
                    Out.Toast(BindingActivity.this, "账号格式不对");
                }
                break;
        }
    }

    @Override
    protected void loadData() {

    }
    /**
     * 绑定请求
     * */
    private void bindRequest() {
        final RequestParams params = new RequestParams(HgbwUrl.TRADE_BIND);
//        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
//        try {
//            js_request.put("referer_id", SharedPreferenceUtils.getMyAccount());//根据实际需求添加相应键值对
//            js_request.put("mobile", et_user_count.getText().toString());
//            js_request.put("referer", "android_hgbw");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        params.addBodyParameter("referer_id", SharedPreferenceUtils.getMyAccount());
        params.addBodyParameter("mobile", et_user_count.getText().toString());
        if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)){
            params.addBodyParameter("bind_type", "bind");
        }
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
        Log.i(TAG_LOG, "bind request: " + params.toJSONString());
        //缓存时间
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
                        progress = new CustomProgressDialog(BindingActivity.this, "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });
    }

    /**
     * 处理json数据
     * "status": "201", "message": "\u8bf7\u4e0d\u7528\u91cd\u590d\u7ed1\u5b9a"
     * 201 : 请不用重复绑定
     */
    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        Log.i(TAG_LOG, "getDataFromJson: " + jsonObject.getString("message"));
        Out.Toast(BindingActivity.this, jsonObject.getString("message"));
        String status = jsonObject.getString("status");
        if (status.equals("200") || status.equals("201")) {
            SharedPreferenceUtils.setTradeAccount(et_user_count.getText().toString());
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.BINDING_STATE);
            String jumpResource = BindingActivity.this.getIntent().getStringExtra(BindingActivity.this.getString(R.string.jump_resource));
            if (jumpResource == null) {
                startActivity(new Intent(BindingActivity.this, MyWalletActivity.class));
            } else if (jumpResource.equals(CarPayActivity.class.getSimpleName()) || jumpResource.equals
                    (HomePayActivity.class.getSimpleName()) || jumpResource.equals(DetailPayActivity.class
                    .getSimpleName())) {//如果来源为结算
                startActivity(new Intent(BindingActivity.this, PayNextActivity.class));
            }
            finish();
        }
    }
}
