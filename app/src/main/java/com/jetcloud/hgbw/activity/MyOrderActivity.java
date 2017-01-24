package com.jetcloud.hgbw.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MyOrderParentAdapter;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MyOrderBean;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.widget.XListView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jetcloud.hgbw.app.HgbwStaticString.PER_PAGE_ALL_NUM;


public class MyOrderActivity extends BaseActivity implements XListView.IXListViewListener{
    private final static String TAG_LOG = MyOrderActivity.class.getSimpleName();
    private CustomProgressDialog progress;
    private MyOrderParentAdapter adapter;
    private XListView mListView;
    private TextView tv_empty;
    private LinearLayout activity_my_order;
    private List<MyOrderBean.OrdersBean> ordersBeenList;
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;
    private int perPageNum;
    int page = 0;
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

        tv_empty = getView(R.id.tv_empty);
        mListView = getView(R.id.xlv_my_order_out);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        activity_my_order = getView(R.id.activity_my_order);
        activity_my_order.setBackgroundResource(R.drawable.mine_bg);
        //隐藏加载更多
        mListView.setPullLoadEnable(false);
    }

    @Override
    protected void loadData() {
        getOrderRequest(page);

    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.autoRefresh();
        }
    }

    /**
     * 处理json数据
     */
    private void getOrderDataFromJson(String result, int page) throws JSONException {
        Gson gson = new Gson();
        MyOrderBean myOrderBean = gson.fromJson(result, MyOrderBean.class);
        List<MyOrderBean.OrdersBean> newOrdersBeenList = myOrderBean.getOrders();

        if (newOrdersBeenList == null || newOrdersBeenList.isEmpty()) {
            mListView.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
            tv_empty.setText("你还未有订单");
        } else {
            if (page == 0) {
                ordersBeenList = newOrdersBeenList;
                adapter = new MyOrderParentAdapter(MyOrderActivity.this, ordersBeenList);
                mListView.setAdapter(adapter);
                perPageNum = ordersBeenList.size();
                onLoad();
            } else {
                adapter.addNewData(newOrdersBeenList);
                perPageNum = ordersBeenList.size() + newOrdersBeenList.size();
                onLoad();
            }
        }

    }


    private void getOrderRequest(final int page) {
        final RequestParams params = new RequestParams(HgbwUrl.GET_ORDER_URL);
        //缓存时间
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
        params.addBodyParameter("page", String.valueOf(page));
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
                            mListView.setVisibility(View.GONE);
                            tv_empty.setVisibility(View.VISIBLE);
                            tv_empty.setText("你还未有订单");
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
//                        progress.dismiss();
                        if (!hasError && result != null) {
                        Log.i(TAG_LOG, "getOrderRequest onFinished: " + result);
                            try {
                                getOrderDataFromJson(result, page);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG_LOG, "getOrderRequest json error: " + e.getMessage());
                            }
                        }
                    }

                });
//                x.task().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progress = new CustomProgressDialog(MyOrderActivity.this, "请稍后", R.drawable.fram2);
//                        progress.show();
//                    }
//                });
            }
        });

    }
    @Override
    public void onRefresh() {
        page = 0;
        getOrderRequest(page);
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG_LOG, "onLoadMore: " + perPageNum);
        if (perPageNum > PER_PAGE_ALL_NUM ){
            mListView.setPullLoadEnable(true);
            getOrderRequest(++ page);
        } else {
            mListView.setPullLoadEnable(false);
        }
    }
}
