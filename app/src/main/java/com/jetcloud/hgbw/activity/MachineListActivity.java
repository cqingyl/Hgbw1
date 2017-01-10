package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.demo.LocationActivity;
import com.jetcloud.hgbw.adapter.MachineListAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineListBean;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class MachineListActivity extends BaseActivity {

    private final static String TAG_LOG = MachineListActivity.class.getSimpleName();
    @ViewInject(R.id.ll_btn_layout)
    LinearLayout ll_btn_layout;
    @ViewInject(R.id.lv_machine)
    ListView lv_machine;
    @ViewInject(R.id.activity_machine_list)
    LinearLayout activity_machine_list;
    @ViewInject(R.id.btn_shenghui)
    Button btn_shenghui;
    private CustomProgressDialog progress;
    private MachineListActivity context;
    private HgbwApplication app;
    private List<MachineListBean.DataBean> data;
    private Button[] btns;
    private MachineListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_machine_list);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void initView() {
        context = MachineListActivity.this;
        app = (HgbwApplication) getApplication();
        topbar.setCenterText("机器列表");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);


        btn_shenghui.setOnClickListener(this);

    }

    @Override
    protected void loadData() {
//        getNetData();
    }

    /**
     * 处理json数据
     */
    private void getDataFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        MachineListBean machineListBean = gson.fromJson(result, MachineListBean.class);

        data = machineListBean.getData();
        btns = new Button[data.size()];
        for (int i = 0; i < data.size(); i++) {
            MachineListBean.DataBean dataBean = data.get(i);
            dataBean.getNumber();
            btns[i] = new Button(context);
            btns[i].setOnClickListener(context);
            btns[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            btns[i].setText(dataBean.getNickname());
            ll_btn_layout.addView(btns[i]);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        if (btn_shenghui == view) {
            startActivity(new Intent(MachineListActivity.this, LocationActivity.class));
        } else {
            for (int i = 0; i < data.size(); i++) {
                if (view == btns[i]) {
                    MachineListBean.DataBean dataBean = data.get(i);
                    Log.i(TAG_LOG, "onClick: " + dataBean.getNumber());
//                getMachineListFromNet(dataBean.getNumber());
                    adapter = new MachineListAdapter(context, data);
                    lv_machine.setAdapter(adapter);
                }
            }
        }
    }


    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.MACHINE_DATA_URL);
        //缓存时间
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
                        progress = new CustomProgressDialog(context, "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });

    }

}
