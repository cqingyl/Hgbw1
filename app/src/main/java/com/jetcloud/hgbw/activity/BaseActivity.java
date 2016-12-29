package com.jetcloud.hgbw.activity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.manager.SystemBarTintManager;
import com.jetcloud.hgbw.view.TopBar;
import com.jetcloud.hgbw.view.TopBar.ITopBarClickListener;


public abstract class BaseActivity extends FragmentActivity implements
OnClickListener {

	public Context context;
	public HgbwApplication application;

	public TopBar topbar;
	public FragmentManager manager;
	public Fragment currentFragment;
	public Fragment firstFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		context.setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
		manager = getSupportFragmentManager();

		initApplication();
		initTopBar();
		initWindow();
		
		initView();
		if (savedInstanceState == null) {
			loadData();
		}
	}
	private void initTopBar() {
		topbar = (TopBar) findViewById(R.id.topbar);

		if (topbar != null) {
			topbar.setTopBarClickListener(new ITopBarClickListener() {

				@Override
				public void rightClick() {
					topBarRight();
				}

				@Override
				public void leftClick() {
					topBarLeft();
				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		context = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void ToastContent(String content) {
		Toast t = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		t.show();
	}

	@SuppressWarnings("unchecked")
	public final <E extends View> E getViewWithClick(int id) {
		try {

			((E) findViewById(id)).setOnClickListener(this);

			return (E) findViewById(id);
		} catch (ClassCastException ex) {

			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	public final <E extends View> E getView(int id) {
		try {

			return (E) findViewById(id);
		} catch (ClassCastException ex) {
			throw ex;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.removeActivity(this);

	}
	private void initApplication() {
		application = (HgbwApplication) getApplication();
		application.addActivity(this);
	}
	@Override
	public void onClick(View view) {

	}



	protected abstract void initView();

	protected abstract void loadData();



	public void topBarRight() {

	}

	public void topBarLeft() {
		finish();
	}



	@SuppressLint("ResourceAsColor")
	private void initStatusBar() {
		setTranslucentStatus(true);
		SystemBarTintManager mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setNavigationBarTintEnabled(true);
		mTintManager.setTintColor(R.color.top_bar_color);
		mTintManager.setNavigationBarTintColor(R.color.top_bar_color);
	}

	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}




	/**
	 * fragment切换
	 */
	public void switchContent(Fragment to, int content) {
		if (currentFragment != to) {
			currentFragment = to;
			FragmentTransaction ft = hideMainFragment();
			if (!to.isAdded()) {
				ft.add(content, to);
			}
//			if (to == HgbwApplication.getCarFragment()) {
//				ft.replace(content, currentFragment).commit();
//				return;
//			}
			ft.show(to);
			ft.commitAllowingStateLoss();
		}
	}

	/*public void showPlayFragment(Fragment from, Fragment to) {
		if (currentFragment != to) {
			if (from.equals(BodhiApplication.getHomeFragment())
					|| from.equals(BodhiApplication.getSpeechFragment())
					|| from.equals(BodhiApplication.getVoiceFragment())
					|| from.equals(BodhiApplication.getQaFragment())) {
				firstFragment = from;
			}
			currentFragment = to;
			manager.beginTransaction()
					.setCustomAnimations(R.anim.push_bottom_in,
							R.anim.push_bottom_in, R.anim.push_bottom_out,
							R.anim.push_bottom_out)
					.add(R.id.fl_all_content, to).addToBackStack(null)
					.commitAllowingStateLoss();
		}
	}*/

	public void openFragment(Fragment from, Fragment to) {
		if (from.equals(HgbwApplication.getHomeFragment())
				|| from.equals(HgbwApplication.getCarFragment())
				|| from.equals(HgbwApplication.getTakeFoodFragment())
				|| from.equals(HgbwApplication.getMineFragment())) {
			firstFragment = from;
		}
		currentFragment = to;
		manager.beginTransaction()
		.setCustomAnimations(R.anim.push_right_in,
				R.anim.push_right_in, R.anim.push_right_out,
				R.anim.push_right_out).add(R.id.fl_content, to)
				.addToBackStack(null).commitAllowingStateLoss();
	}


	public FragmentTransaction hideMainFragment() {
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		if (HgbwApplication.getHomeFragment() != null) {
			fragmentTransaction.hide(HgbwApplication.getHomeFragment());
		}
		if (HgbwApplication.getTakeFoodFragment() != null) {
			fragmentTransaction.hide(HgbwApplication.getTakeFoodFragment());
		}
		if (HgbwApplication.getCarFragment() != null) {
			fragmentTransaction.hide(HgbwApplication.getCarFragment());
		}
		if (HgbwApplication.getMineFragment() != null) {
			fragmentTransaction.hide(HgbwApplication.getMineFragment());
		}
		return fragmentTransaction;
	}

	public void onReturnBtn() {
		manager.popBackStack();
		int sum = manager.getBackStackEntryCount();
		if (sum == 1) {
			currentFragment = firstFragment;
			manager.beginTransaction().show(currentFragment)
			.commitAllowingStateLoss();
		}
	}
	@TargetApi(19)
	private void initWindow(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   
		}}



}
