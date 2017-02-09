package com.jetcloud.hgbw.activity;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.JumpUtils;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ForgotPasswordActivity extends BaseActivity {

    private final static String TAG_LOG = ForgotPasswordActivity.class.getSimpleName();
    private EditText et_username,et_vernumber,et_password,et_passwordag;
    private CheckBox cb_agree;
    private Button bt_register;
    private TextView tv_getver;
    private Timer mTimer;
    private TimerTask mTask;
    private int time = 60;
    private Handler mHandler = new Handler();
    private boolean isCheck = false;
    private MyEditeListener editeListener ;
    private CustomProgressDialog progress;
    private LinearLayout activity_forget_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_forget_password);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        topbar.setCenterText("重设密码");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        editeListener=new MyEditeListener();
        et_username=getView(R.id.et_username);
        et_username.addTextChangedListener(editeListener);
        et_vernumber=getView(R.id.et_vernumber);
        et_vernumber.addTextChangedListener(editeListener);
        et_password=getView(R.id.et_password);
        et_password.addTextChangedListener(editeListener);
        et_passwordag=getView(R.id.et_passwordag);
        et_passwordag.addTextChangedListener(editeListener);
        cb_agree=getView(R.id.cb_agree);
        bt_register=getViewWithClick(R.id.bt_register);
        tv_getver=getViewWithClick(R.id.tv_getver);

        activity_forget_password = getView(R.id.activity_forget_password);
        activity_forget_password.setBackgroundResource(R.drawable.mine_bg);

        //chebox监听
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                isCheck=arg1;
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_username.getText().toString()) ||
                        TextUtils.isEmpty(et_password.getText().toString())||
                        TextUtils.isEmpty(et_vernumber.getText().toString())||
                        TextUtils.isEmpty(et_passwordag.getText().toString())||!isCheck) {
                    bt_register.setBackground(getResources().getDrawable(
                            R.drawable.bt_loging_bg_no));
                    bt_register.setEnabled(false);
                } else {
                    bt_register.setBackground(getResources().getDrawable(
                            R.drawable.bt_loging_bg));
                    bt_register.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        // TODO Auto-generated method stub

    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        super.onClick(view);
        if (view==tv_getver) {
            if (TextUtils.isEmpty(et_username.getText().toString())) {
                Out.Toast(context, "请输入手机号");
            } else if (et_username.getText().toString().length() != 11) {
                Out.Toast(context,"手机号位数不对");
            } else {
                getVer();
            }
        } else if (view == bt_register){
            if (et_username.getText().toString().length() != 11) {
                Out.Toast(context,"手机号格式不对");
            } else if (!et_password.getText().toString().equals(et_passwordag.getText().toString())){
                Out.Toast(context, "两次密码不一样");
            } else {
                forgetPasswordRequest();
            }
        }
    }
    //获取验证码
    private void getVer() {

        tv_getver.setEnabled(false);
        verificationRequest();

        mTimer = new Timer();
        mTask = new TimerTask() {

            @Override
            public void run() {
                if (time > 0) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            tv_getver.setText("已发送验证码（" + time + "）");
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            tv_getver.setText("重新获取");
                            tv_getver.setEnabled(true);
                        }
                    });
                    time = 60;
                    mTimer.cancel();
                }
                time--;
            }
        };
        mTimer.schedule(mTask, 0, 1000);
    }
    private class MyEditeListener implements TextWatcher{

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            if (TextUtils.isEmpty(et_username.getText().toString()) ||
                    TextUtils.isEmpty(et_password.getText().toString())||
                    TextUtils.isEmpty(et_vernumber.getText().toString())||
                    TextUtils.isEmpty(et_passwordag.getText().toString())||!isCheck) {
                bt_register.setBackground(getResources().getDrawable(
                        R.drawable.bt_loging_bg_no));
                bt_register.setEnabled(false);
            } else {
                bt_register.setBackground(getResources().getDrawable(
                        R.drawable.bt_loging_bg));
                bt_register.setEnabled(true);
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

        }}
    /***
     * 验证码请求
     * */
    private void verificationRequest() {
        final RequestParams params = new RequestParams(HgbwUrl.VERIFICATION_URL);
        //缓存时间
        params.addQueryStringParameter("phone",et_username.getText().toString());
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
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "onFinished code : " + result);

                        }
                    }
                });
            }
        });
    }

    /***
     * 重设密码请求
     * */
    private void forgetPasswordRequest() {
        final RequestParams params = new RequestParams(HgbwUrl.RESET_PWD_URL);
        //缓存时间
        params.addBodyParameter("phone",et_username.getText().toString());
        params.addBodyParameter("pwd",et_password.getText().toString());
        params.addBodyParameter("code",et_vernumber.getText().toString());
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
                        Log.e(TAG_LOG, "forgetPasswordRequest onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "forgetPasswordRequest onError " + " code: " + responseCode + " message: " + responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "forgetPasswordRequest cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        progress.dismiss();
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "forgetPasswordRequest onFinished: " + result);
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
        JumpUtils.check405(ForgotPasswordActivity.this, result);
        Out.Toast(context, jsonObject.getString("status"));
      if (jsonObject.getString("status").equals("success")) {
            finish();
        }

    }
}
