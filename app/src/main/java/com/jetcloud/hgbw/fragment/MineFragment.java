package com.jetcloud.hgbw.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.MyOrderActivity;
import com.jetcloud.hgbw.activity.MyTicketActivity;
import com.jetcloud.hgbw.activity.MyWalletActivity;
import com.jetcloud.hgbw.activity.RegisterActivity;
import com.jetcloud.hgbw.activity.SettingActivity;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@SuppressLint("ResourceAsColor")
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {
    private final static String TAG_LOG = MineFragment.class.getSimpleName();
    private static MineFragment mineFragment;
    @ViewInject(R.id.ll_login_layout)
    LinearLayout ll_login_layout;
    @ViewInject(R.id.tv_login)
    TextView tv_login;
    @ViewInject(R.id.tv_register)
    TextView tv_register;
    @ViewInject(R.id.ll_logout_layout)
    LinearLayout ll_logout_layout;
    @ViewInject(R.id.takefood)
    LinearLayout takefood;
    @ViewInject(R.id.myorder)
    LinearLayout myorder;
    @ViewInject(R.id.myticket)
    LinearLayout myticket;
    @ViewInject(R.id.mypoint)
    LinearLayout mypoint;
    @ViewInject(R.id.mymoney)
    LinearLayout mymoney;
    @ViewInject(R.id.aboutus)
    LinearLayout aboutus;
    @ViewInject(R.id.setting)
    LinearLayout setting;

    boolean isFirst = true;

    public static MineFragment newInstance() {

        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }
        return mineFragment;
    }

    @Override
    public View initRootView(LayoutInflater inflater, ViewGroup container) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        MainActivity.main_bg.setBackgroundResource(R.drawable.mine_bg);
        initWindow();
        takefood.setOnClickListener(this);
        myorder.setOnClickListener(this);
        myticket.setOnClickListener(this);
        mypoint.setOnClickListener(this);
        mymoney.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume() {
        isFirst = false;
        isHideBtnResiterAndLogin();
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG_LOG, "H onHiddenChanged: " + hidden);
        if (!hidden && !isFirst) {
            isHideBtnResiterAndLogin();
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * 显示“登录/注册”与否
     * */
    public boolean isHideBtnResiterAndLogin () {
        if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
            ll_login_layout.setVisibility(View.GONE);
            ll_logout_layout.setVisibility(View.VISIBLE);
            return true;
        } else {
            ll_login_layout.setVisibility(View.VISIBLE);
            ll_logout_layout.setVisibility(View.GONE);
            return false;
        }
    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        super.onClick(view);
        if (view == takefood) {
            Out.Toast(getActivity(), "订餐须知");
        } else if (view == myorder) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
            }
        } else if (view == myticket) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyTicketActivity.class));

            }
        } else if (view == mypoint) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                Out.Toast(getActivity(), "我的卡积分");
            }
        } else if (view == mymoney) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
            }
        } else if (view == aboutus) {
            Out.Toast(getActivity(), "关于我们");
        } else if (view == tv_login) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (view == tv_register) {
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        } else if (view == setting) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        }

    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        x.view().inject(this, rootView);
        return rootView;
    }

}
