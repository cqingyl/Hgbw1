package com.jetcloud.hgbw.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SettingActivity extends BaseActivity {

    private final static String TAG_LOG = SettingActivity.class.getSimpleName();
    @ViewInject(R.id.btn_logout)
    LinearLayout btn_logout;
    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("设置");
        Resources resources = SettingActivity.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
            btn_logout.setVisibility(View.GONE);
        } else {
            btn_logout.setVisibility(View.VISIBLE);
        }
        btn_logout.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        if (view == btn_logout) {
            getNetData();
        }
    }

    /***
     * 注销请求
     */
    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.LOGOUT_URL);
        //缓存时间
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
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
                        progress = new CustomProgressDialog(context, "请稍后", R.drawable.fram2);
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
        Out.Toast(context, jsonObject.getString("status"));
        if (jsonObject.has("status") && jsonObject.getString("status").equals("success")) {
            SharedPreferenceUtils.setIdentity(SharedPreferenceUtils.WITHOUT_LOGIN);
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
            SharedPreferenceUtils.setMyAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
            SharedPreferenceUtils.setTradeAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
            finish();
        }
    }
}
