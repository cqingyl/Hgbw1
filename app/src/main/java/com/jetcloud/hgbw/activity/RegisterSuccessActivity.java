package com.jetcloud.hgbw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jetcloud.hgbw.R;

public class RegisterSuccessActivity extends BaseActivity {

    private Button bt_gologing;
    private LinearLayout activity_register_success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_register_success);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0==bt_gologing) {
            RegisterSuccessActivity.this.finish();
        }
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        bt_gologing = getViewWithClick(R.id.bt_gologing);
        activity_register_success = getView(R.id.activity_register_success);
//        activity_register_success.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {
        // TODO Auto-generated method stub
        topbar.setCenterText("注册成功");
    }

}
