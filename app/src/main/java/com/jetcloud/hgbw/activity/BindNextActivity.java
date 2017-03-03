package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.JumpUtils;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.TopBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class BindNextActivity extends BaseActivity {
    private final static String TAG_LOG = BindingActivity.class.getSimpleName();
    @ViewInject(R.id.topbar)
    TopBar topbar;
    @ViewInject(R.id.et_trade_account)
    EditText et_trade_account;
    @ViewInject(R.id.et_trade_password)
    EditText et_trade_password;
    @ViewInject(R.id.tv_btn_ok)
    TextView tv_btn_ok;
    @ViewInject(R.id.activity_next_binding)
    LinearLayout activity_next_binding;
    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_next);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("绑定账号");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_btn_ok.setOnClickListener(this);

//        activity_next_binding.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        if (et_trade_account.getText().length() != 11) {
            Out.Toast(BindNextActivity.this, "手机格式不对");
        } else if (et_trade_password.getText().toString().isEmpty()) {
            Out.Toast(BindNextActivity.this, "密码不能为空");
        } else {
            bindRequest();
        }
    }

    /**
     * 绑定请求
     */
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
        params.addBodyParameter("mobile", et_trade_account.getText().toString());
        params.addBodyParameter("pay_pwd", et_trade_password.getText().toString());
        if (SharedPreferenceUtils.getBindStatus().equals(SharedPreferenceUtils.UNBINDING_STATE)) {
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
                        progress = new CustomProgressDialog(BindNextActivity.this, "请稍后", R.drawable.fram2);
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
        String status = jsonObject.getString("status");
        JumpUtils.check405(BindNextActivity.this, result);
        if (status.equals("200")) {
            SharedPreferenceUtils.setTradeAccount(et_trade_account.getText().toString());
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.BINDING_STATE);
            String jumpResource = BindNextActivity.this.getIntent().getStringExtra(HgbwStaticString.JUMP_RESOURCE);
            if (jumpResource == null) {
                startActivity(new Intent(BindNextActivity.this, MyWalletActivity.class));
            } else if (jumpResource.equals(CarPayActivity.class.getSimpleName()) || jumpResource.equals
                    (HomePayActivity.class.getSimpleName()) || jumpResource.equals(DetailPayActivity.class
                    .getSimpleName())) {//如果来源为结算
                startActivity(new Intent(BindNextActivity.this, PayNextActivity.class));
            }
            finish();
        } else if (status.equals("201") || status.equals("400")) {
            Out.Toast(BindNextActivity.this, jsonObject.getString("message"));
        }
    }
}
