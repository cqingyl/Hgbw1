package com.jetcloud.hgbw.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.demo.LocationActivity;
import com.jetcloud.hgbw.adapter.HomeFragmentAdapter;
import com.jetcloud.hgbw.adapter.HomeFragmentAdapter.HolderClickListener;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.TakeInShopCarAnim;
import com.jetcloud.hgbw.view.ImageCycleView;
import com.jetcloud.hgbw.view.ImageCycleView.ImageCycleViewListener;
import com.jetcloud.hgbw.view.MyListView;
import com.jetcolud.hgbw.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
	private static HomeFragment homeFragment;
	private ImageCycleView binner;
	private HomeFragmentAdapter adapter ;
	private TextView tvTopSearch;
	//轮播图点击事件
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(int position, View imageView) {
			// TODO Auto-generated method stub

		}
	};

	// binner图数据
	private ArrayList<String> mImageUrl = new ArrayList<String>();
	
	private MyListView listView;
	public static HomeFragment newInstance() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}
		return homeFragment;
	}
	@Override
	public View initRootView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return View.inflate(getActivity(), R.layout.homefragment, null);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		binner = getView(R.id.binner);
		listView = getView(R.id.lv_home);
		tvTopSearch = getViewWithClick(R.id.tv_top_search);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		//初始化轮播图
		loadAdData();
		//listview设置适配器
		List<String>list = new ArrayList<String>();
		list.add(";");
		list.add(";");
		
		adapter=new HomeFragmentAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		//设置飞入动画
		adapter.SetOnSetHolderClickListener(new HolderClickListener() {
			
			@Override
			public void onHolderClick(Drawable drawable, int[] start_location) {
				// TODO Auto-generated method stub
				TakeInShopCarAnim anim = new TakeInShopCarAnim(getActivity(),MainActivity.INSHOPCAR);
				anim.doAnim(drawable, start_location);

				addCornerNum();
			}
		});
	}
	//增加购物车数量
	public void addCornerNum() {
		int shopCarNumber = SharedPreferenceUtils.getShopCarNumber();
		SharedPreferenceUtils.setShopCarNumber(++shopCarNumber);
		TextView tvCorner = (TextView) getActivity().findViewById(R.id.tv_corner);
		tvCorner.setText(String.valueOf(shopCarNumber));
	}
	private void loadAdData() {
		for (int i = 0; i < 3; i++) {

			mImageUrl.add("http://pic.58pic.com/uploadfilepic/sheji/2009-11-10/58PIC_ysjihc1990_20091110aaee42cf802a80a0.jpg");

		}
		binner.setImageResources(mImageUrl, mAdCycleViewListener, 0);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.tv_top_search:
				Log.i("onclick", "onClick: ");
				Intent intent = new Intent(getActivity(), LocationActivity.class);
				intent.putExtra("from", 0);
				startActivity(intent);
				break;
		}
	}
}
