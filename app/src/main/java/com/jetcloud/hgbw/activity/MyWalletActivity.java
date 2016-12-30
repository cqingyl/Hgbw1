package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MyWalletAdapter;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class MyWalletActivity extends BaseActivity {

    private final static String TAG_LOG = MyWalletActivity.class.getSimpleName();
    private CustomProgressDialog progress;
    private TextView tv_btn_ok, tv_btn_no;
    private ListView lv_card;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wallet);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("我的钱包");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        tv_btn_ok = getViewWithClick(R.id.tv_btn_ok);
        tv_btn_no = getViewWithClick(R.id.tv_btn_no);
        lv_card = getView(R.id.lv_card);
        /****/
//        SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
//        getNetData();
        /****/
        String status = SharedPreferenceUtils.getBindStatus();
        if (status.equals(SharedPreferenceUtils.UNBINDING_STATE)) {
            lv_card.setVisibility(View.GONE);
            tv_btn_no.setVisibility(View.GONE);
            tv_btn_ok.setVisibility(View.VISIBLE);
        } else {
            lv_card.setVisibility(View.VISIBLE);
            tv_btn_no.setVisibility(View.VISIBLE);
            tv_btn_ok.setVisibility(View.GONE);

            list = new ArrayList<>();
            list.add(SharedPreferenceUtils.getTradeAccount());
            lv_card.setAdapter(new MyWalletAdapter(this,list));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_btn_ok){
            startActivity(new Intent(MyWalletActivity.this, BindingActivity.class));
            finish();
        } else if (view.getId() == R.id.tv_btn_no) {
            getNetData();
        }
    }

    @Override
    protected void loadData() {

    }

    /**
     * 解绑操作
     */
    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.TRADE_BIND);
        JSONObject js_request = new JSONObject();//服务器需要传参的json对象
        try {
            js_request.put("referer_id", SharedPreferenceUtils.getMyAccount());//根据实际需求添加相应键值对
//            js_request.put("mobile", SharedPreferenceUtils.getTradeAccount());
            js_request.put("mobile", "15928038352");
            js_request.put("referer", "android_hgbw");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.setBodyContent(js_request.toString());
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
                        progress = new CustomProgressDialog(MyWalletActivity.this, "请稍后", R.drawable.fram2);
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
        Log.i(TAG_LOG, "getDataFromJson: " + jsonObject.getString("message"));
        String status = jsonObject.getString("status");
        if (status.equals("200")){
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
            lv_card.setVisibility(View.GONE);
            tv_btn_no.setVisibility(View.GONE);
            tv_btn_ok.setVisibility(View.VISIBLE);
            lv_card.setAdapter(new MyWalletAdapter(this,null));
        }
    }
}
