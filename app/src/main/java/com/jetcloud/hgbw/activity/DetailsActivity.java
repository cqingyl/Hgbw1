package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.FoodDetail;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;


public class DetailsActivity extends BaseActivity {
	private final static String TAG_LOG = DetailsActivity.class.getSimpleName();
	private TextView tv_up,tv_down,number,tv_content,tv_total_price,tv_add_car,tv_go_to_pay,tv_phone;
	private int poductNum = 1;
	private CustomProgressDialog progress;
	public final static String FOOD_OBJECT = "food_object";
	private String titleText;
	private ImageView iv_food;
	private FoodDetail detail;
	private FoodDetail.PMealBean bean;
	private HgbwApplication app;
	private double totalPrice;
	private ShopCarInfo shopCarInfo;
	private LinearLayout ll_nav_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_details);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		app = (HgbwApplication) getApplication();
		shopCarInfo = (ShopCarInfo) getIntent().getSerializableExtra(FOOD_OBJECT);
		if (shopCarInfo != null) {
			titleText = shopCarInfo.getP_name();
			topbar.setCenterText(titleText);
		} else {
			ll_nav_bottom.setVisibility(View.GONE);
		}
		Resources resources = DetailsActivity.this.getResources();
		Drawable drawable = resources.getDrawable(R.drawable.fanhui);
		topbar.setLeftDrawable(false, drawable);
		iv_food = getView(R.id.iv_food);
		tv_content = getView(R.id.tv_content);
		tv_total_price = getView(R.id.tv_total_price);
		tv_add_car = getViewWithClick(R.id.tv_add_car);
		tv_go_to_pay = getViewWithClick(R.id.tv_go_to_pay);
		tv_up = getViewWithClick(R.id.tv_up);
		tv_down = getViewWithClick(R.id.tv_down);
		tv_phone = getView(R.id.tv_phone);
		number = getViewWithClick(R.id.number);
	}
	String result = "{ \"p_meal\" : [\n" +
			"                                                                      {\n" +
			"                                                                        \"p_id\": 1,\n" +
			"                                                                        \"s_name\": \"小炒杏鲍菇\",\n" +
			"                                                                        \"s_picture\": \"../images/小炒杏鲍菇" +
			".png\",\n" +
			"                                                                        \"s_price\": 15,\n" +
			"                                                                        \"s_total\": null,\n" +
			"                                                                        \"s_totalprice\": null,\n" +
			"                                                                        \"s_introduce\": " +
			"\"端上一盘红烧肉，满屋飘香口水流。此菜本非人间有，天上佳肴落街头。北宋大文豪苏东坡也对你推崇备至，焖你的皮，煮你的肉，既酥又烂，吃后口齿流香。他与你友好合作，发明创造了流传百世的\\\"东坡肉\\\"；敝人的祖先更绝，数百年前元兵南侵，我祖从中原南阳逃难，在南下的颠沛流离中，竟然还携带令人垂涎三尺的\\\"东坡肉\\\"！\",\n" +
			"                                                                        \"s_phone\": 12321231\n" +
			"                                                                      }\n" +
			"                                                                    ]\n" +
			"}";
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
			getNetData();
//		try {
//			getDataFromJson(result);
//			bean = detail.getP_meal().get(0);
//			tv_content.setText(bean.getS_introduce());
//			tv_total_price.setText(String.format(DetailsActivity.this.getString(R.string.rmb_display), (double)bean.getS_price()));
//			iv_food.setImageResource(R.drawable.longredmeet);
//			tv_phone.setText(String.valueOf(bean.getS_phone()));
//			shopCarInfo.setP_local_number(1);
//			shopCarInfo.setP_price(bean.getS_price());
//			shopCarInfo.setP_name(bean.getS_name());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.tv_up:
				poductNum++;
				number.setText(String.valueOf(poductNum));
				if (bean != null) {
					totalPrice = bean.getS_price() * poductNum;
					tv_total_price.setText(String.format(DetailsActivity.this.getString(R.string.rmb_display), totalPrice));
					shopCarInfo.setP_local_number(poductNum);
				}
				break;
			case R.id.tv_down:
				if (poductNum>1) {
					poductNum--;
					number.setText(String.valueOf(poductNum));
					if (bean != null) {
						totalPrice = bean.getS_price() * poductNum;
						tv_total_price.setText(String.format(DetailsActivity.this.getString(R.string.rmb_display), totalPrice));
						shopCarInfo.setP_local_number(poductNum);
					}
				}
				break;
			//立即购买
			case R.id.tv_go_to_pay:
				ArrayList<ShopCarInfo> listObj = new ArrayList<ShopCarInfo>();
				listObj.add(shopCarInfo);
				Intent i = new Intent(DetailsActivity.this, DetailPayActivity.class);
				i.putExtra(FOOD_OBJECT, listObj);
				startActivity(i);
				break;
			//添加到购物车
			case R.id.tv_add_car:
				addGoodNumber();
				break;
		}

	}

	/**
	 * 处理json数据
	 */
	private void getDataFromJson(String result) throws JSONException {
		Gson gson = new Gson();
		detail = gson.fromJson(result, FoodDetail.class);
	}


	private void getNetData() {
		final RequestParams params = new RequestParams(HgbwUrl.FOOD_DETAIL);
		//缓存时间
		params.addBodyParameter("p_name", titleText);
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
							Log.e(TAG_LOG, "onError " + " code: " + responseCode + " message: " + responseMsg);
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
						bean = detail.getP_meal().get(0);
						tv_content.setText(bean.getS_introduce());
						tv_total_price.setText(String.format(DetailsActivity.this.getString(R.string.rmb_display), (double)bean.getS_price()));
						ImageOptions imageOptions = new ImageOptions.Builder()
								.setFailureDrawableId(R.drawable.ic_launcher)
								.build();
						String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.BASE_URL + shopCarInfo.getP_picture());
						x.image().bind(iv_food, imgPath, imageOptions);
						tv_phone.setText(bean.getS_phone());
						shopCarInfo.setP_local_number(1);
						shopCarInfo.setP_price(bean.getS_price());
						shopCarInfo.setP_name(bean.getS_name());
					}
				});

				x.task().post(new Runnable() {
					@Override
					public void run() {
						progress = new CustomProgressDialog(DetailsActivity.this, "请稍后", R.drawable.fram2);
						progress.show();
					}
				});
			}
		});
	}

	/***
	 * 添加goods和machine到数据库 或者 增加商品数量
	 * 更新数据库
	 */


	public void addGoodNumber() {
		x.task().run(new Runnable() {
			@Override
			public void run() {
				int shopCarInfoId = shopCarInfo.getP_id();

				ShopCarInfo carInfo;
				MachineInfo machineInfo;
				try {
					carInfo = app.db.selector(ShopCarInfo.class).where("p_id", "=", shopCarInfoId).findFirst();
					//如果数据库里不存在，数量为1,并向数据库添加一项；存在，就取出来+1再存回去
					if (carInfo == null) {
						machineInfo = new MachineInfo();
						machineInfo.setId(shopCarInfo.getP_machine());
						carInfo = new ShopCarInfo();
						carInfo.setP_id(shopCarInfo.getP_id());
						carInfo.setP_type(shopCarInfo.getP_type());
						carInfo.setP_machine(shopCarInfo.getP_machine());
						carInfo.setP_name(shopCarInfo.getP_name());
						carInfo.setP_picture(shopCarInfo.getP_picture());
						carInfo.setP_price(shopCarInfo.getP_price());
						carInfo.setP_address(shopCarInfo.getP_address());
						carInfo.setP_local_number(1);
						carInfo.setP_number(shopCarInfo.getP_number());
						carInfo.setP_vr9(shopCarInfo.getP_vr9());
						app.db.saveOrUpdate(machineInfo);
					} else {
						int oldNum = carInfo.getP_local_number();
						int newNum = oldNum + poductNum;
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
