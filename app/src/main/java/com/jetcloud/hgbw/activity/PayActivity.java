package com.jetcloud.hgbw.activity;

import android.os.Bundle;
import android.util.Log;

import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcolud.hgbw.R;

import java.util.ArrayList;

public class PayActivity extends BaseActivity {
    private final static String TAG_LOG = PayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

        ArrayList<ShopCarInfo> listObj = (ArrayList<ShopCarInfo>) getIntent().getSerializableExtra(DetailsActivity.FOOD_OBJECT);
        Log.i(TAG_LOG, "initView: "+ listObj.size());
    }

    @Override
    protected void loadData() {

    }
}
