package com.jetcloud.hgbw.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class EditAccountActivity extends BaseActivity {
    private final static String TAG_LOG = EditAccountActivity.class.getSimpleName();
    private EditText et_account;
    private ProgressDialog progressDialog;
    private RelativeLayout activity_edit_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_account);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initView() {
        et_account = getView(R.id.et_account);
        activity_edit_account = getView(R.id.activity_edit_account);
        activity_edit_account.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {
        topbar.setCenterText("设置昵称");
//        Resources resources = EditAccountActivity.this.getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
//        topbar.setLeftDrawable(false, drawable);
        topbar.setLeftText("取消", null, null);
        topbar.setRightText("保存", null, null);
    }
    @Override
    public void topBarRight() {
        super.topBarRight();
        if (!et_account.getText().toString().isEmpty()) {
            uploadTitle(et_account.getText().toString());
        }

    }
    /**
     * 上传昵称
     * */
    private void uploadTitle(String nickName) {
        final RequestParams params = new RequestParams(HgbwUrl.PIC_AND_NICK_URL);
        //缓存时间
        params.addBodyParameter("nickname", nickName);
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
//                        progress.dismiss();
                        progressDialog.dismiss();
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "upload onFinished: " + result);
                            AlertDialog dialog = new AlertDialog.Builder(EditAccountActivity.this).create();
                            dialog.setTitle("保存成功！");
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i(TAG_LOG, "onClick: ");
                                    finish();
                                }
                            });
                            dialog.show();
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(EditAccountActivity.this);
                        progressDialog.setTitle("请稍后");
                        progressDialog.show();
//                        progress = new CustomProgressDialog(EditAccountActivity.this, "请稍后", R.drawable.fram2);
//                        progress.show();
                    }
                });
            }
        });

    }
}
