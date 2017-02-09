package com.jetcloud.hgbw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoadingActivity extends Activity {

    private final static String TAG_LOG = LoadingActivity.class.getSimpleName();
    private final static int MSG_FINISH_LAUNCHERACTIVITY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 1500);
//        doGoOn();
        setContentView(R.layout.activity_loading);

    }


    public Handler mHandler = new Handler () {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    jumpMainActivity();
                    break;
                default:
                    break;
            }
        }
    };
    private void doGoOn() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
//                    jumpMainActivity();
//                }else {
//                    getLoginRequest();
//                }

            }
        }.start();
    }
    /**
     * 预登陆
     * */
    public void getLoginRequest() {

        final RequestParams params = new RequestParams(HgbwUrl.LOGIN_URL);
        //缓存时间
        params.addBodyParameter("phone", SharedPreferenceUtils.getMyAccount());
        params.addBodyParameter("pwd", SharedPreferenceUtils.getMyPassword());
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

            }
        });

    }

    /**
     * 处理json数据
     *
     */
    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("status").equals("success")) {
            SharedPreferenceUtils.setIdentity(jsonObject.getString("identity"));
            Log.i(TAG_LOG, "getDataFromJson: " + jsonObject.getString("identity"));
            jumpMainActivity();
        }

    }
    /**
     * 跳转主界面
     * */
    public void jumpMainActivity () {
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        LoadingActivity.this.finish();
    }
}
