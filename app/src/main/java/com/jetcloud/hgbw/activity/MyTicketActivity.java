package com.jetcloud.hgbw.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.adapter.MyticketAdapter;
import com.jetcloud.hgbw.view.MyListView;

public class MyTicketActivity extends BaseActivity {
	private MyListView lv_myticket;
	private MyticketAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_my_ticket);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topbar.setCenterText("我的卡券");
		Resources resources = MyTicketActivity.this.getResources();
		 Drawable drawable = resources.getDrawable(R.drawable.fanhui);
		topbar.setLeftDrawable(false, drawable);
		lv_myticket = getView(R.id.lv_myticket);
		adapter = new MyticketAdapter(MyTicketActivity.this);
		lv_myticket.setAdapter(adapter);
		
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}

}
