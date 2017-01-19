package com.jetcloud.hgbw.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.TakeFoodParentAdapter;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.TakeFoodInfo;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.MyListView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class TakeFoodFragment extends BaseFragment{
	private static final String TAG_LOG = TakeFoodFragment.class.getSimpleName();
	private static TakeFoodFragment takeFoodFragment;
	private MyListView lv_takefood;
	private TakeFoodParentAdapter adapter;
	private CustomProgressDialog progress;
	public static TakeFoodFragment newInstance() {
		if (takeFoodFragment == null) {
			takeFoodFragment = new TakeFoodFragment();
		}
		return takeFoodFragment;
	}
	@Override
	public View initRootView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return View.inflate(getActivity(), R.layout.fragment_takefood, null);
	}

	@Override
	protected void initView() {
		topbar.setCenterText("取餐列表");
		lv_takefood= getView(R.id.lv_takefood);
	}

	@Override
	public void initData() {
		getOrderRequest();
	}

	/**
	 * 处理json数据
	 */
	private void getOrderDataFromJson(String result) throws JSONException {
		Gson gson = new Gson();
		TakeFoodInfo takeFoodInfo = gson.fromJson(result, TakeFoodInfo.class);
		List<TakeFoodInfo.OrdersBean> ordersBeenList = takeFoodInfo.getOrders();
//        for (int i = 0; i < ordersBeenList.size(); i ++) {
//            MyOrderBean.OrdersBean ordersBean = ordersBeenList.get(i);
//        }

		adapter = new TakeFoodParentAdapter(getActivity(), ordersBeenList);
		lv_takefood.setAdapter(adapter);

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
						progress = new CustomProgressDialog(getActivity(), "请稍后", R.drawable.fram2);
						progress.show();
					}
				});
			}
		});

	}
}
