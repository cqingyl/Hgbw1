package com.jetcloud.hgbw.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	private List<ShopCarInfo> foodList;
	private Map<String, List<ShopCarInfo>> children = new HashMap<>();
	private String machineNum;
	private HgbwApplication app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		LinearLayout ll = new LinearLayout(this);
//		ll.setBackgroundColor(getResources().getColor(R.color.transparent));
//		ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		setContentView(ll);

    	api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);

		app = (HgbwApplication) getApplication();
		machineNum = SharedPreferenceUtils.getMachineNum();
		children = app.getChildren();
		foodList = children.get(machineNum);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.i("log", "onResp: " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Out.Toast(WXPayEntryActivity.this, String.valueOf(resp.errCode));
			if (resp.errCode == -1) {
				Out.Toast(WXPayEntryActivity.this, "支付失败!");
				finish();
			} else if (resp.errCode == 0) {
				Out.Toast(WXPayEntryActivity.this, "支付成功!");
				new ClearCarAsyncTask().execute();
			} else if (resp.errCode == -2) {
				Out.Toast(WXPayEntryActivity.this, "已取消支付");
				finish();
			}
		}
	}

	class ClearCarAsyncTask extends AsyncTask<String, Void, Boolean>{


		@Override
		protected Boolean doInBackground(String... strings) {
			if (SharedPreferenceUtils.getCarPay().equals(SharedPreferenceUtils.WITH_CARPAY)){
				for (int i = 0; i < foodList.size(); i ++) {
					WhereBuilder whereBuilder = WhereBuilder.b("id", "=", foodList.get(i).getId());
					try {
						app.db.delete(ShopCarInfo.class,whereBuilder);
						if (app.db.selector(ShopCarInfo.class).findAll() == null){
							app.db.delete(MachineInfo.class);
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				int oldNum = SharedPreferenceUtils.getShopCarNumber();
				int newNum = oldNum - foodList.size();
				if (newNum > 0) {
					SharedPreferenceUtils.setShopCarNumber(newNum);
				} else {
					SharedPreferenceUtils.setShopCarNumber(0);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean isOk) {
			if (isOk) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(HgbwStaticString.JUMP_RESOURCE, WXPayEntryActivity.class.getSimpleName());
				WXPayEntryActivity.this.finish();
				startActivity(intent);
			} else {
				WXPayEntryActivity.this.finish();
			}
		}
	}

}