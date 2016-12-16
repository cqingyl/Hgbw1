package com.jetcloud.hgbw.fragment;

import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.MyTicketActivity;
import com.jetcloud.hgbw.activity.RegisterActivity;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.view.CircleImageView;
import com.jetcloud.hgbw.view.TopBar;
import com.jetcolud.hgbw.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class MineFragment extends BaseFragment{
	private static  MineFragment mineFragment;
	private LinearLayout takefood,myorder,myticket,mypoint,mymoney,aboutus;
	private TextView tv_login,tv_register;
	public static MineFragment newInstance() {

		if (mineFragment == null) {
			mineFragment = new MineFragment();
		}
		return mineFragment;
	}
	@Override
	public View initRootView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return View.inflate(getActivity(), R.layout.minefragment, null);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		MainActivity.main_bg.setBackgroundResource(R.drawable.mine_bg);
		initWindow();
		takefood=getViewWithClick(R.id.takefood);
		myorder = getViewWithClick(R.id.myorder);
		myticket = getViewWithClick(R.id.myticket);
		mypoint = getViewWithClick(R.id.mypoint);
		mymoney = getViewWithClick(R.id.mymoney);
		aboutus = getViewWithClick(R.id.aboutus);
		tv_login=getViewWithClick(R.id.tv_login);
		tv_register=getViewWithClick(R.id.tv_register);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		if (view==takefood) {
			Out.Toast(getActivity(), "订餐须知");
		}else if (view==myorder) {
			Out.Toast(getActivity(), "我的订单");
		}else if (view==myticket) {
			startActivity(new Intent(getActivity(),MyTicketActivity.class));
			Out.Toast(getActivity(), "我的卡券");
		}else if (view==mypoint) {
			Out.Toast(getActivity(), "我的卡积分");
		}else if (view==mymoney) {
			Out.Toast(getActivity(), "我的钱包");
		}else if (view==aboutus) {
			Out.Toast(getActivity(), "关于我们");
		}else if (view==tv_login) {
			startActivity(new Intent(getActivity(),LoginActivity.class));
		}else if (view==tv_register) {
			startActivity(new Intent(getActivity(),RegisterActivity.class));
		}

	}
	@TargetApi(19)
	private void initWindow(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   
		}}




}
