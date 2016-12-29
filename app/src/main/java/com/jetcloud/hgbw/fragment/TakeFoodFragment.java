package com.jetcloud.hgbw.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.TakeFoodFragmentAdapter;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.TakeFoodInfo;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.MyListView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class TakeFoodFragment extends BaseFragment{
	private static final String TAG_LOG = TakeFoodFragment.class.getSimpleName();
	private static TakeFoodFragment takeFoodFragment;
	private MyListView lv_takefood;
	private TakeFoodFragmentAdapter adapter;
	private CustomProgressDialog progress;
	List<TakeFoodInfo.MealBean>data = new ArrayList<TakeFoodInfo.MealBean>();
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
		// TODO Auto-generated method stub
		topbar.setCenterText("取餐列表");
		lv_takefood= getView(R.id.lv_takefood);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		data = new ArrayList<TakeFoodInfo.MealBean>();

		adapter = new TakeFoodFragmentAdapter(getActivity(), data);
		lv_takefood.setAdapter(adapter);
		getNetData();
	}

	private void getNetData() {
		final RequestParams params = new RequestParams(HgbwUrl.TAKE_FOOD);
		//缓存时间
//		params.addBodyParameter("m_id", "ok2SdwCTyMl0B2Vou5NVsv7GCgr4");
		params.addBodyParameter("m_id", "a43a467afdf-5");
		params.addBodyParameter("type", "1");
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
//                    	Log.i(TAG_LOG, "onFinished: " + result);
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
						progress = new CustomProgressDialog(getActivity(),"请稍后", R.drawable.fram2);
						progress.show();
					}
				});
			}
		});

	}
	/**
	 * 处理json数据
	 * */
	private void getDataFromJson(String result) throws JSONException {
		Gson gson = new Gson();
		TakeFoodInfo takeFoodInfo = gson.fromJson(result, TakeFoodInfo.class);
		data.addAll(takeFoodInfo.getMeal());
        adapter.notifyDataSetChanged();
	}
}
