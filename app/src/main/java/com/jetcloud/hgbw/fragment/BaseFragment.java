package com.jetcloud.hgbw.fragment;

import com.jetcloud.hgbw.view.TopBar;
import com.jetcloud.hgbw.view.TopBar.ITopBarClickListener;
import com.jetcolud.hgbw.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	public FragmentActivity mActivity;
	public View rootView;
	public TopBar topbar;
	public Fragment mFragment;
	private PopupWindow popupWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mFragment = this;
		super.onCreate(savedInstanceState);
	}

	public View findViewById(int viewId) {
		return rootView.findViewById(viewId);
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

	/*
	 * 返回一个需要展示的View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		rootView = initRootView(inflater, container);
		initTopBar();
		initView();
		
		return rootView;
	}







	/*
	 * 当Activity初始化之后可以在这里进行一些数据的初始化操作
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		
	}

	/**
	 * 子类实现此抽象方法返回View进行展示
	 * 
	 * @param container
	 * 
	 * @return
	 */
	public abstract View initRootView(LayoutInflater inflater,
			ViewGroup container);

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 子类在此方法中实现数据的初始化
	 */
	public abstract void initData();

	@Override
	public void onClick(View view) {

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		// if (!hidden) {
		// ((BaseActivity) BodhiApplication.getMainActivity()).currentFragment =
		// this;
		// }
	}
	private void initTopBar() {
		topbar = (TopBar) findViewById(R.id.topbar);

	}
	
}
