package com.jetcloud.hgbw.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.TopBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class BindingActivity extends BaseActivity {

    private final static String TAG_LOG = BindingActivity.class.getSimpleName();
    @ViewInject(R.id.topbar)
    TopBar topbar;
    @ViewInject(R.id.tv_myaccount)
    TextView tv_myaccount;
    @ViewInject(R.id.et_trade_account)
    EditText et_trade_account;
    @ViewInject(R.id.et_trade_password)
    EditText et_trade_password;
    @ViewInject(R.id.tv_btn_ok)
    TextView tv_btn_ok;
    @ViewInject(R.id.activity_binding)
    LinearLayout activity_binding;
    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_binding);
        x.view().inject(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        topbar.setCenterText("绑定账号");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        tv_btn_ok.setOnClickListener(this);

//        tv_myaccount.setText(SharedPreferenceUtils.getMyAccount());
//        activity_binding.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_ok:
                finish();
                Intent i = new Intent(BindingActivity.this, BindNextActivity.class);
                i.putExtra(HgbwStaticString.JUMP_RESOURCE, BindingActivity.this.getIntent().getStringExtra(HgbwStaticString.JUMP_RESOURCE));
                startActivity(i);
                break;
        }
    }

    @Override
    protected void loadData() {

    }


}
