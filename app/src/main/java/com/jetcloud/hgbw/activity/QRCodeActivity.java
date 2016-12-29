package com.jetcloud.hgbw.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_qrcode)
public class QRCodeActivity extends BaseActivity {
    private final static String TAG_LOG = QRCodeActivity.class.getSimpleName();
    private CustomProgressDialog progress;
    @ViewInject(R.id.iv_qr_code)
    ImageView iv_qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qrcode);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //设置屏幕亮度
        setWindowBrightness(255);
        topbar.setCenterText("我的二维码");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
//        iv_qr_code = getView(R.id.iv_twocode);
        WindowManager wm = (WindowManager) QRCodeActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = iv_qr_code.getLayoutParams();
        layoutParams.width=width/3*2;
        layoutParams.height=width/3*2;
        iv_qr_code.setLayoutParams(layoutParams);
    }

    @Override
    protected void loadData() {
        getNetData();
    }

    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.HOME_DATA_URL);
        //缓存时间
        params.addQueryStringParameter("myR_lng", "104.06792346");
        params.addQueryStringParameter("myR_lat", "30.67994285");
        params.addQueryStringParameter("type", "mobile");
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
//                        Log.i(TAG_LOG, "onFinished: " + result);
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
                        progress = new CustomProgressDialog(QRCodeActivity.this, "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });

    }

    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
    }

    //调整屏幕亮度
    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    //设置服务器轮询
    public void settime(){
        x.task().run(new Runnable() {
            @Override
            public void run() {
                // 需要做的事:发送消息
                Message message = new Message();
                message.what = 999;

            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
