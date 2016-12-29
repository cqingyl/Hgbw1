package com.jetcloud.hgbw.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.ShopCarInfo;

import java.util.ArrayList;

import static com.jetcloud.hgbw.activity.DetailsActivity.FOOD_OBJECT;

public class PayNextActivity extends BaseActivity {
    private final static String TAG_LOG = PayNextActivity.class.getSimpleName();
    private ArrayList<ShopCarInfo> listObj;
    private ShopCarInfo shopCarInfo;
    private TextView tv_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(com.jetcloud.hgbw.R.layout.activity_comfirm_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("支付");
        tv_price = (TextView) findViewById(R.id.tv_price);
    }

    @Override
    protected void loadData() {
        listObj = (ArrayList<ShopCarInfo>) getIntent().getSerializableExtra(FOOD_OBJECT);
        Log.i(TAG_LOG, "initView: " + listObj.size());
        //只有一件商品
        if (listObj != null) {
            shopCarInfo = listObj.get(0);
            topbar.setCenterText(shopCarInfo.getP_name());
            float totalNum = shopCarInfo.getP_price() * shopCarInfo.getP_local_number();
            tv_price.setText(getString(R.string.pay_money, totalNum));
        }
    }
}
