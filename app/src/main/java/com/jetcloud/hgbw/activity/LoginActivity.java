package com.jetcloud.hgbw.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.UserBean;
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

@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity {
    private final static String TAG_LOG = LoginActivity.class.getSimpleName();
    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @ViewInject(R.id.tv_register)
    TextView tv_register;
    @ViewInject(R.id.tv_forget_pwd)
    TextView tv_forget_pwd;
    @ViewInject(R.id.bt_loging)
    Button bt_loging;

    private MyEditeListener editeListener;

    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_login);
        x.view().inject(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub

        bt_loging.setOnClickListener(this);
        editeListener = new MyEditeListener();
        et_username.addTextChangedListener(editeListener);
        et_password.addTextChangedListener(editeListener);
        tv_register.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);

    }

    @Override
    protected void loadData() {
        // TODO Auto-generated method stub
        bt_loging.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        super.onClick(view);
        if (view == bt_loging) {
            if (et_username.getText().toString().length() != 11) {
                Out.Toast(context, "手机位数不对");
            } else {
                Out.Toast(LoginActivity.this, "登录");
                getNetData();
            }
        } else if (view == tv_register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } else if (view == tv_forget_pwd) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

        }
    }

    private class MyEditeListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            if (TextUtils.isEmpty(et_username.getText().toString()) || TextUtils.isEmpty(et_password.getText().toString())) {
                bt_loging.setBackground(getResources().getDrawable(
                        R.drawable.bt_loging_bg_no));
                bt_loging.setEnabled(false);
            } else {
                bt_loging.setBackground(getResources().getDrawable(
                        R.drawable.bt_loging_bg));
                bt_loging.setEnabled(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }
    }

    /***
     * 登录请求
     */
    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.LOGIN_URL);
        //缓存时间
        params.addBodyParameter("phone", et_username.getText().toString());
        params.addBodyParameter("pwd", et_password.getText().toString());
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
        if (jsonObject.getString("status").equals("success")) {
            SharedPreferenceUtils.setMyAccount(et_username.getText().toString());
            SharedPreferenceUtils.setMyPassword(et_password.getText().toString());
            SharedPreferenceUtils.setIdentity(jsonObject.getString("identity"));
//            finish();
            getUserInfoRequest();
        }

    }
    /**
     * 获取用户信息
     * */
    private void getUserInfoRequest() {
        final RequestParams params = new RequestParams(HgbwUrl.PIC_AND_NICK_URL);
        //缓存时间
        params.addQueryStringParameter("identity", SharedPreferenceUtils.getIdentity());
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
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "get nick and pic onFinished: " + result);
                            try {
                                getUserDataFromJson(result);
                                JSONObject jsonObject = new JSONObject(result);


//                                jsonObject.getString("")
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }
        });

    }

    /**
     * 判断用户是否绑定交易宝
     * */
    public void getUserDataFromJson(String result) {
        Gson gson = new Gson();
        UserBean userBean = gson.fromJson(result, UserBean.class);

        String tradeAccount = userBean.getTradebook_acct();
        if (tradeAccount != null && !tradeAccount.isEmpty()) {
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.BINDING_STATE);
        }
        finish();
    }
}
