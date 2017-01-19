package com.jetcloud.hgbw.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MyOrderParentAdapter;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MyOrderBean;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


public class MyOrderActivity extends BaseActivity {

    private final static String TAG_LOG = MyOrderActivity.class.getSimpleName();
    private CustomProgressDialog progress;
    private MyOrderParentAdapter adapter;
    private ListView lv_my_order_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initView(){
        topbar.setCenterText("我的订单");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        lv_my_order_out = getView(R.id.lv_my_order_out);

        getOrderRequest();
    }

    @Override
    protected void loadData() {

    }

    private void initDatas() {
//        for (int i = 0; i < 3; i++) {
//            MachineInfo machineInfo = new MachineInfo();
//            machineInfo.setId("天猫店铺" + (i + 1) + "号店");
//            groups.add(machineInfo);
//            List<ShopCarInfo> products = new ArrayList<ShopCarInfo>();
//            for (int j = 0; j <= i; j++) {
//                ShopCarInfo shopCarInfo = new ShopCarInfo();
//                shopCarInfo.setP_name(groups.get(i).getName());
//                products.add(shopCarInfo);
//            }
//            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
//        }
    }



    /**
     * 处理json数据
     */
    private void getOrderDataFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        MyOrderBean myOrderBean = gson.fromJson(result, MyOrderBean.class);
        List<MyOrderBean.OrdersBean> ordersBeenList = myOrderBean.getOrders();
//        for (int i = 0; i < ordersBeenList.size(); i ++) {
//            MyOrderBean.OrdersBean ordersBean = ordersBeenList.get(i);
//        }
        adapter = new MyOrderParentAdapter(this, ordersBeenList);
        lv_my_order_out.setAdapter(adapter);
    }


    private void getOrderRequest() {
        final RequestParams params = new RequestParams(HgbwUrl.GET_ORDER_URL);
        //缓存时间
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
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
                        Log.e(TAG_LOG, "getOrderRequest onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "getOrderRequest onError " + " code: " + responseCode + " message: " + responseMsg);
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
                        Log.i(TAG_LOG, "getOrderRequest onFinished: " + result);
                            try {
                                getOrderDataFromJson(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG_LOG, "getOrderRequest json error: " + e.getMessage());
                            }
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        progress = new CustomProgressDialog(MyOrderActivity.this, "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });

    }

}
