package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MyWalletAdapter;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import java.util.ArrayList;

public class MyWalletActivity extends BaseActivity {

    private TextView tv_btn_ok;
    private ListView lv_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wallet);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        topbar.setCenterText("我的钱包");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);

        tv_btn_ok = getViewWithClick(R.id.tv_btn_ok);
        lv_card = getView(R.id.lv_card);
        /****/
        SharedPreferenceUtils.setBindStatus(0);
        /****/
        int status = SharedPreferenceUtils.getBindStatus();
        if (status == 0) {
            lv_card.setVisibility(View.GONE);
            tv_btn_ok.setVisibility(View.VISIBLE);
        } else {
            lv_card.setVisibility(View.VISIBLE);
            tv_btn_ok.setVisibility(View.GONE);

            ArrayList<String> list = new ArrayList<>();
            list.add("15820521459");
            lv_card.setAdapter(new MyWalletAdapter(this,list));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_btn_ok){
            startActivity(new Intent(MyWalletActivity.this, BindingActivity.class));
            finish();
        }
    }

    @Override
    protected void loadData() {

    }
}
