package com.jetcloud.hgbw.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jetcloud.hgbw.adapter.TakeFoodFragmentAdapter;
import com.jetcloud.hgbw.view.MyListView;
import com.jetcolud.hgbw.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TakeFoodFragment extends BaseFragment{
	private static TakeFoodFragment takeFoodFragment;
	private MyListView lv_takefood;
	private TakeFoodFragmentAdapter adapter;
	public static TakeFoodFragment newInstance() {
		if (takeFoodFragment == null) {
			takeFoodFragment = new TakeFoodFragment();
		}
		return takeFoodFragment;
	}
	@Override
	public View initRootView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return View.inflate(getActivity(), R.layout.takefoodfragment, null);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topbar.setCenterText("取餐列表");
		lv_takefood= getView(R.id.lv_takefood);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		List<String>list = new ArrayList<String>();
		list.add(";");
		list.add(";");
		list.add(";");
		adapter = new TakeFoodFragmentAdapter(getActivity(), list);
		lv_takefood.setAdapter(adapter);
		
	}

}
