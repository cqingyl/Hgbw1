package com.jetcloud.hgbw.activity;

import com.jetcolud.hgbw.R;
import com.jetcolud.hgbw.R.drawable;
import com.jetcolud.hgbw.R.id;
import com.jetcolud.hgbw.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends BaseActivity {
	private TextView tv_up,tv_down,number;
	private int poductNum=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_details);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topbar.setCenterText("红烧肉套餐");
		Resources resources = DetailsActivity.this.getResources();
		 Drawable drawable = resources.getDrawable(R.drawable.fanhui);
		topbar.setLeftDrawable(false, drawable);
		tv_up= getViewWithClick(R.id.tv_up);
		tv_down= getViewWithClick(R.id.tv_down);
		number= getViewWithClick(R.id.number);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		if (view==tv_up) {
			poductNum++;
			number.setText(poductNum+"");
		}else if (view==tv_down) {
			if (poductNum>1) {
				poductNum--;
				number.setText(poductNum+"");
			}
		}
	}
	

}
