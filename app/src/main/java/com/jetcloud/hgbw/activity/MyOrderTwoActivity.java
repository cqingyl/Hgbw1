package com.jetcloud.hgbw.activity;

import android.os.Bundle;
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
import com.jetcloud.hgbw.utils.JumpUtils;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.MyListView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

import static com.jetcloud.hgbw.app.HgbwStaticString.PER_PAGE_ALL_NUM;

public class MyOrderTwoActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private BGARefreshLayout mRefreshLayout;
    private final static String TAG_LOG = MyOrderTwoActivity.class.getSimpleName();
    private CustomProgressDialog progress;
    private MyOrderParentAdapter adapter;
    private MyListView mListView;
    private TextView tv_empty;
    private LinearLayout activity_my_order;
    private List<MyOrderBean.OrdersBean> ordersBeenList;

    private int perPageNum = 0;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_two);
        mListView = (MyListView) findViewById(R.id.lv_my_order_out);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        initRefreshLayout();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
//        topbar.setCenterText("我的订单");
//        Resources resources = this.getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
//        topbar.setLeftDrawable(false, drawable);

        tv_empty = getView(R.id.tv_empty);
        mListView = getView(R.id.lv_my_order_out);
//        activity_my_order = getView(R.id.activity_my_order);
//        activity_my_order.setBackgroundResource(R.drawable.mine_bg);

    }

    @Override
    protected void loadData() {

    }

    private void initRefreshLayout() {
            // 为BGARefreshLayout 设置代理
            mRefreshLayout.setDelegate(this);
            // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
            BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
            // 设置下拉刷新和上拉加载更多的风格
            mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


            // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
            // 设置正在加载更多时不显示加载更多控件
            // mRefreshLayout.setIsShowLoadingMoreView(false);
            // 设置正在加载更多时的文本
            refreshViewHolder.setLoadingMoreText("加载更多");
            // 设置整个加载更多控件的背景颜色资源 id
            refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.gray_light);
            // 设置整个加载更多控件的背景 drawable 资源 id
            refreshViewHolder.setLoadMoreBackgroundDrawableRes(R.color.gray_light);
            // 设置下拉刷新控件的背景颜色资源 id
            refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.gray_light);
            // 设置下拉刷新控件的背景 drawable 资源 id
            refreshViewHolder.setRefreshViewBackgroundDrawableRes(R.color.gray_light);
            // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//            mRefreshLayout.setCustomHeaderView(mBanner, false);
            // 可选配置  -------------END
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            Out.Toast(MyOrderTwoActivity.this, "没有最新数据了");
            return;
        }
        getOrderRequest(mNewPageNumber);

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 4) {
            mRefreshLayout.endLoadingMore();
            Out.Toast(MyOrderTwoActivity.this, "没有最新数据了");
            return false;
        }
        getOrderRequest(mMorePageNumber);
        return true;
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
            }
        });

    }
    /**
     * 处理json数据
     */
    private void getOrderDataFromJson(String result, int page) throws JSONException {
        JumpUtils.check405(MyOrderTwoActivity.this, result);
        Gson gson = new Gson();
        MyOrderBean myOrderBean = gson.fromJson(result, MyOrderBean.class);
        List<MyOrderBean.OrdersBean> newOrdersBeenList = myOrderBean.getOrders();

        if (page == 0) {
            if (newOrdersBeenList == null || newOrdersBeenList.isEmpty()) {
                mListView.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("你还未有订单");
            } else {
                ordersBeenList = newOrdersBeenList;
                adapter = new MyOrderParentAdapter(MyOrderTwoActivity.this, ordersBeenList);
                mListView.setAdapter(adapter);
                perPageNum = ordersBeenList.size();
                if (perPageNum >= PER_PAGE_ALL_NUM ) {
                }
            }
            mRefreshLayout.endRefreshing();
        } else {
            if (newOrdersBeenList == null || newOrdersBeenList.isEmpty()) {
                Log.i(TAG_LOG, "getOrderDataFromJson: " + "没有更多了");
            } else {
                adapter.addNewData(newOrdersBeenList);
                perPageNum = ordersBeenList.size() + newOrdersBeenList.size();
                Log.i(TAG_LOG, "getOrderDataFromJson perPageNum: " + perPageNum);
            }
            mRefreshLayout.endLoadingMore();
        }

    }

}
