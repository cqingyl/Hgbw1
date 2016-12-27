package com.example.xutilstest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.bean.GoodsInfo;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private final static String TAG_LOG = Main2Activity.class.getSimpleName();
    @ViewInject(R.id.text)
    private TextView text;
    Map<String, List<GoodsInfo>> group = new HashMap<>();
    String json = "{\n" +
            "  \"a\": [\n" +
            "    {\n" +
            "      \"p_id\": 1,\n" +
            "      \"p_type\": \"a\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"午餐肉套餐\",\n" +
            "      \"p_picture\": \"../images/cp01.png\",\n" +
            "      \"p_price\": 15,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"p_id\": 12,\n" +
            "      \"p_type\": \"a\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"午餐肉套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 12,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 2,\n" +
            "      \"p_number\": 12\n" +
            "    }\n" +
            "  ],\n" +
            "  \"b\": [\n" +
            "    {\n" +
            "      \"p_id\": 2,\n" +
            "      \"p_type\": \"b\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"石板烧鹅套餐\",\n" +
            "      \"p_picture\": \"../images/cp01.png\",\n" +
            "      \"p_price\": 20,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 2\n" +
            "    }\n" +
            "  ],\n" +
            "  \"c\": [\n" +
            "    {\n" +
            "      \"p_id\": 3,\n" +
            "      \"p_type\": \"c\",\n" +
            "      \"p_machine\": \"HG010100002\",\n" +
            "      \"p_name\": \"红烧肉套餐\",\n" +
            "      \"p_picture\": \"../images/cp01.png\",\n" +
            "      \"p_price\": 30,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 3\n" +
            "    }\n" +
            "  ],\n" +
            "  \"d\": [\n" +
            "    {\n" +
            "      \"p_id\": 4,\n" +
            "      \"p_type\": \"d\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"鱼香肉丝套餐\",\n" +
            "      \"p_picture\": \"../images/cp01.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 4\n" +
            "    }\n" +
            "  ],\n" +
            "  \"e\": [\n" +
            "    {\n" +
            "      \"p_id\": 5,\n" +
            "      \"p_type\": \"e\",\n" +
            "      \"p_machine\": \"HG010100002\",\n" +
            "      \"p_name\": \"鱼香肉丝套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 5\n" +
            "    },\n" +
            "    {\n" +
            "      \"p_id\": 6,\n" +
            "      \"p_type\": \"e\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"鱼香肉丝套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 6\n" +
            "    },\n" +
            "    {\n" +
            "      \"p_id\": 7,\n" +
            "      \"p_type\": \"e\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"午餐肉套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 7\n" +
            "    },\n" +
            "    {\n" +
            "      \"p_id\": 8,\n" +
            "      \"p_type\": \"e\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"午餐肉套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 6,\n" +
            "      \"p_number\": 8\n" +
            "    },\n" +
            "    {\n" +
            "      \"p_id\": 11,\n" +
            "      \"p_type\": \"e\",\n" +
            "      \"p_machine\": \"HG010100001\",\n" +
            "      \"p_name\": \"午餐肉套餐\",\n" +
            "      \"p_picture\": \"../images/banner.png\",\n" +
            "      \"p_price\": 25,\n" +
            "      \"p_address\": \"成都市新希望国际\",\n" +
            "      \"p_numb\": 2,\n" +
            "      \"p_number\": 8\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        XUtilsTestApplication application = (XUtilsTestApplication) getApplication();
        Log.i("tag", "onCreate: "+application.s);

        x.view().inject(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getAData();
//        Map<String, List<String>> group = new HashMap<>();
//        List<String> data = new ArrayList<>();
//        for (int i = 0; i < 3; i ++) {
//            for (int j = 0; j < 3; j++) {
//                String s = "aa" + j;
//                data.add(s);
//            }
//        group.put("" + i, data);
//        }
//        for (int i = 0; i < 3; i ++) {
//                data = group.get("" + i);
//            Log.i("asd", "group: " + data.size());
//            for (int j = 0; j < 3; j++) {
//                Log.i("ads", "data: " + data.get(j));
//            }
//        }
        try {
            getDataFromJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        GoodsInfo2 goodsInfo = gson.fromJson(result,GoodsInfo2.class);
        List<GoodsInfo2.ABean> dataA = goodsInfo.getA();
        List<GoodsInfo2.BBean> dataB = goodsInfo.getB();
        List<GoodsInfo2.CBean> dataC = goodsInfo.getC();
        List<GoodsInfo2.DBean> dataD = goodsInfo.getD();
        List<GoodsInfo2.EBean> dataE = goodsInfo.getE();
        List<GoodsInfo> data = new ArrayList<>();
        data.addAll(dataE);

        Log.i("data a", "getDataFromJson: " + dataA.size());
        Log.i("data b", "getDataFromJson: " + dataB.size());
        Log.i("data c", "getDataFromJson: " + dataC.size());
        Log.i("data d", "getDataFromJson: " + dataD.size());
        Log.i("data e", "getDataFromJson: " + data.size());
    }


    private void getAData() {
        final RequestParams params = new RequestParams("http://www.suiedai.com/order/takefood");
        //缓存时间
//        params.addQueryStringParameter("order_m_id", "a43a467afdf-5");
//        params.addQueryStringParameter("type", "1");
        params.addBodyParameter("order_m_id", "a43a467afdf-5");
        params.addBodyParameter("type", "1");
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
                            Log.i(TAG_LOG, "onSuccess: ");
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "onError " + " code: " + responseCode + " message: " + responseMsg);
                            text.setText(responseMsg);
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
//                            try {
//                                getDataFromJson(result);


//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                    List<GoodsInfo> data = new ArrayList<GoodsInfo>();
//                    data.add(goodsInfo);
                        }
                    }

                });
                x.task().post(new Runnable() { // UI同步执行
                    @Override
                    public void run() {
                        progress = new ProgressDialog(Main2Activity.this);
                        progress.show();
                    }
                });

            }

        });


    }
}
