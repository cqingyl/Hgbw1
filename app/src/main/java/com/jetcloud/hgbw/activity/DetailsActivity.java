package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.FoodDetailBean;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jetcloud.hgbw.app.HgbwStaticString.FOOD_ID;
import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE;


public class DetailsActivity extends BaseActivity {
	private final static String TAG_LOG = DetailsActivity.class.getSimpleName();
	private TextView tv_up,tv_down,number,tv_content,tv_price_vr9,tv_price_cny,tv_add_car,tv_go_to_pay,tv_phone;
	private int poductNum = 1;
	private CustomProgressDialog progress;
	private String titleText;
	private ImageView iv_food;
	private HgbwApplication app;
	private ShopCarInfo shopCarInfo;
	private LinearLayout ll_nav_bottom;
	private ScrollView sv_all_layout;
	private TextView tv_empty;

	private double totalPriceCny;
	private double totalPriceVr9;
	private List<ShopCarInfo> listObj;
	private List<MachineInfo> groups = new ArrayList<>();
	private MachineInfo machineInfo;
	private String machineNum;
	private String foodId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_details);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		app = (HgbwApplication) getApplication();
		Resources resources = DetailsActivity.this.getResources();
		Drawable drawable = resources.getDrawable(R.drawable.fanhui);
		topbar.setLeftDrawable(false, drawable);

		iv_food = getView(R.id.iv_food);
		tv_content = getView(R.id.tv_content);
		tv_price_vr9 = getView(R.id.tv_price_vr9);
		tv_price_cny = getView(R.id.tv_price_cny);
		tv_add_car = getViewWithClick(R.id.tv_add_car);
		tv_go_to_pay = getViewWithClick(R.id.tv_go_to_pay);
		tv_up = getViewWithClick(R.id.tv_up);
		tv_down = getViewWithClick(R.id.tv_down);
		tv_phone = getView(R.id.tv_phone);
		number = getViewWithClick(R.id.number);
		ll_nav_bottom = getView(R.id.ll_nav_bottom);
		sv_all_layout = getView(R.id.sv_all_layout);
		tv_empty = getView(R.id.tv_empty);
	}

	@Override
	protected void loadData() {

		Intent i = getIntent();
		if (i.hasExtra(MACHINE) && i.hasExtra(FOOD_ID)) {
			machineInfo = (MachineInfo) i.getSerializableExtra(MACHINE);
			foodId = i.getStringExtra(FOOD_ID);
			machineNum = machineInfo.getNumber();
			Log.i("log", "food id: " + foodId);
			Log.i("log", "machine num: " + machineNum);
			getDetailDataRequest();
		}
		//只有一件商品
//		groups = app.getGroups();
//		listObj = app.getChildren().get(groups.get(0).getNumber());
//		shopCarInfo = listObj.get(0);
//		if (shopCarInfo != null) {
//			titleText = shopCarInfo.getName();
//			topbar.setCenterText(titleText);
//		} else {
//			ll_nav_bottom.setVisibility(View.GONE);
//		}
//
//
//		tv_content.setText(shopCarInfo.getDescription());
//		tv_total_price.setText(String.format(DetailsActivity.this.getString(R.string.rmb_display), shopCarInfo.getPrice_cny()));
//		ImageOptions imageOptions = new ImageOptions.Builder()
//				.setFailureDrawableId(R.drawable.ic_launcher)
//				.build();
//		String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + shopCarInfo.getPic());
//		x.image().bind(iv_food, imgPath, imageOptions);
//		tv_phone.setText("13340902246");
//		shopCarInfo.setP_local_number(1);
//		shopCarInfo.setPrice_cny(shopCarInfo.getPrice_cny());
//		shopCarInfo.setName(shopCarInfo.getName());

	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.tv_up:
				poductNum++;
				number.setText(String.valueOf(poductNum));
				if (shopCarInfo != null) {
					totalPriceCny = shopCarInfo.getPrice_cny() * poductNum;
					totalPriceVr9 = shopCarInfo.getPrice_vr9() * poductNum;
					tv_price_cny.setText(String.format(DetailsActivity.this.getString(R.string.take_food_total), totalPriceCny));
					tv_price_vr9.setText(String.format(DetailsActivity.this.getString(R.string.take_food_gcb_total), totalPriceVr9));
					shopCarInfo.setP_local_number(poductNum);
				}
				break;
			case R.id.tv_down:
				if (poductNum>1) {
					poductNum--;
					number.setText(String.valueOf(poductNum));
					if (shopCarInfo != null) {
						totalPriceCny = shopCarInfo.getPrice_cny() * poductNum;
						totalPriceVr9 = shopCarInfo.getPrice_vr9() * poductNum;
						tv_price_cny.setText(String.format(DetailsActivity.this.getString(R.string.take_food_total), totalPriceCny));
						tv_price_vr9.setText(String.format(DetailsActivity.this.getString(R.string.take_food_gcb_total), totalPriceVr9));
						shopCarInfo.setP_local_number(poductNum);
					}
				}
				break;
			//立即购买
			case R.id.tv_go_to_pay:
				if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
					context.startActivity(new Intent(context, LoginActivity.class));
				} else {
					List<MachineInfo> groups = new ArrayList<>();
					groups.add(machineInfo);
					Map<String, List<ShopCarInfo>> children = new HashMap<>();
					List<ShopCarInfo> shopCarInfos = new ArrayList<>();
					shopCarInfos.add(shopCarInfo);
					children.put(machineInfo.getNumber(),shopCarInfos);
					app.setGroups(groups);
					app.setChildren(children);
					app.setTotalGcb(totalPriceCny);
					app.setTotalPrice(totalPriceVr9);
					Intent i = new Intent(DetailsActivity.this, DetailPayActivity.class);
					startActivity(i);
				}
				break;
			//添加到购物车
			case R.id.tv_add_car:
				addGoodNumber();
				break;
		}

	}

	/***
	 * 详情请求
	 * */
	public void getDetailDataRequest() {
		final RequestParams params = new RequestParams(HgbwUrl.FOOD_DETAIL_URL);
		//缓存时间
		params.addQueryStringParameter("food_id", foodId);
		params.addQueryStringParameter("mechine_number", machineNum);
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
						Log.e(TAG_LOG, "getDetailDataRequest onError: " + ex.getMessage());
						if (ex instanceof HttpException) { // 网络错误
							HttpException httpEx = (HttpException) ex;
							int responseCode = httpEx.getCode();
							String responseMsg = httpEx.getMessage();
							String errorResult = httpEx.getResult();
							Log.e(TAG_LOG, "getDetailDataRequest onError " + " code: " + responseCode + " message: " + responseMsg);
						} else { // 其他错误
							ll_nav_bottom.setVisibility(View.GONE);
							sv_all_layout.setVisibility(View.GONE);
							topbar.setCenterText("商品不存在");
							tv_empty.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onCancelled(CancelledException cex) {
						Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFinished() {
						if (!hasError && result != null) {
							Log.i(TAG_LOG, "getDetailDataRequest onFinished: " + result);
							getDataFromJson(result);
						}
					}
				});
			}
		});
	}
	/**
	 * 获取详情数据
	 * */
	public void getDataFromJson(String result) {
		Gson gson = new Gson();
		FoodDetailBean foodDetailBean = gson.fromJson(result, FoodDetailBean.class);
		FoodDetailBean.InfoBean infoBean = foodDetailBean.getInfo();
		if (foodDetailBean.getStatus().equals("success") && infoBean.getNum() != 0){
			ll_nav_bottom.setVisibility(View.VISIBLE);
			sv_all_layout.setVisibility(View.VISIBLE);
			tv_empty.setVisibility(View.GONE);
			shopCarInfo = infoBean;
			titleText = shopCarInfo.getName();
			topbar.setCenterText(titleText);
			tv_content.setText(shopCarInfo.getDescription());
			tv_price_cny.setText(String.format(DetailsActivity.this.getString(R.string.take_food_total), shopCarInfo
					.getPrice_cny()));
			tv_price_vr9.setText(String.format(DetailsActivity.this.getString(R.string.take_food_gcb_total), shopCarInfo
					.getPrice_vr9()));
			ImageOptions imageOptions = new ImageOptions.Builder()
					.setFailureDrawableId(R.drawable.ic_launcher)
					.build();
			String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + shopCarInfo.getPic());
			x.image().bind(iv_food, imgPath, imageOptions);
			tv_phone.setText("13340902246");
			shopCarInfo.setP_local_number(1);
			shopCarInfo.setPrice_cny(shopCarInfo.getPrice_cny());
			shopCarInfo.setName(shopCarInfo.getName());
		} else {
			ll_nav_bottom.setVisibility(View.GONE);
			sv_all_layout.setVisibility(View.GONE);
			tv_empty.setVisibility(View.VISIBLE);
			topbar.setCenterText("商品不存在");
		}
	}
	/***
	 * 添加goods和machine到数据库 或者 增加商品数量
	 * 更新数据库
	 */
	public void addGoodNumber() {
		x.task().run(new Runnable() {
			@Override
			public void run() {
				int shopCarInfoId = shopCarInfo.getId();

				ShopCarInfo carInfo;
				MachineInfo machineInfo;
				int oldNum;
				int newNum;
				try {
					carInfo = app.db.selector(ShopCarInfo.class).where("id", "=", shopCarInfoId).findFirst();
					//如果数据库里不存在，数量为1,并向数据库添加一项；存在，就取出来+1再存回去
					if (carInfo == null) {
						carInfo = new ShopCarInfo();
						machineInfo = new MachineInfo();
						machineInfo.setAddress(app.getGroups().get(0).getAddress());
						machineInfo.setNickname(app.getGroups().get(0).getNickname());
						machineInfo.setNumber(app.getGroups().get(0).getNumber());
						machineInfo.setCity(app.getGroups().get(0).getCity());
						carInfo.setP_machine(app.getGroups().get(0).getNumber());
						carInfo.setId(shopCarInfo.getId());
						carInfo.setName(shopCarInfo.getName());
						carInfo.setPic(shopCarInfo.getPic());
						carInfo.setPrice_cny(shopCarInfo.getPrice_cny());
						carInfo.setP_local_number(1);
						carInfo.setNum(shopCarInfo.getNum());
						carInfo.setPrice_vr9(shopCarInfo.getPrice_vr9());
						app.db.saveOrUpdate(machineInfo);
					} else {
						oldNum = carInfo.getP_local_number();
						newNum = oldNum + poductNum;
						carInfo.setP_local_number(newNum);
					}
					//增加商品数量
					app.db.saveOrUpdate(carInfo);
					int oldTotalNum = SharedPreferenceUtils.getShopCarNumber();
					int newTotalNum = oldTotalNum + poductNum;
					SharedPreferenceUtils.setShopCarNumber(newTotalNum);
				} catch (DbException e) {
					e.printStackTrace();
					Log.e(TAG_LOG, "添加失败: " + e.getMessage());
				}
			}
		});
	}
}
