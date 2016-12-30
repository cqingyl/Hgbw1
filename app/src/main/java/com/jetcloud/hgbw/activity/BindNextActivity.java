package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import passwordinputview.PasswordInputView;

public class BindNextActivity extends BaseActivity {
    private final static String TAG_LOG = BindingActivity.class.getSimpleName();
    private TextView tv_btn_ok;
    private PasswordInputView passwordInputView;
    private CustomProgressDialog progress;
    private String TradeTreasureAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bing_next);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("输入密码");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        Intent i = getIntent();
        TradeTreasureAccount = i.getStringExtra(BindingActivity.JIAOYIBAO_ACCOUNT);
        passwordInputView = getView(R.id.passwordInputView);
        tv_btn_ok = getViewWithClick(R.id.tv_btn_ok);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_btn_ok){
            if (passwordInputView.getText().length() == 6){
                //发送
//                getNetData();
//                SharedPreferenceUtils.setBindStatus(1);
                finish();
                startActivity(new Intent(BindNextActivity.this, MyWalletActivity.class));

            } else {
                Out.Toast(BindNextActivity.this, "请输入密码" );
            }
        }
    }

    @Override
    protected void loadData() {

    }

    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.TRADE_BIND);
        //缓存时间
        params.addQueryStringParameter("referer_id", SharedPreferenceUtils.getMyAccount());
        params.addQueryStringParameter("mobile", TradeTreasureAccount);
        params.addQueryStringParameter("referer", "android_hgbw");
        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().get(params, new Callback.CacheCallback<String>() {

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
                        progress = new CustomProgressDialog(BindNextActivity.this, "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });
    }
        /**
         * 处理json数据
         */
    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        finish();
        startActivity(new Intent(BindNextActivity.this, MyWalletActivity.class));
    }
}
