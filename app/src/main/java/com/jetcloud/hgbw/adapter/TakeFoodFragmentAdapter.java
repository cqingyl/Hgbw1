package com.jetcloud.hgbw.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcolud.hgbw.R;

public class TakeFoodFragmentAdapter extends BaseAdapter{
	private Context context ;
	private List<String> list;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public TakeFoodFragmentAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1==null) {
			arg1 = View.inflate(context, R.layout.takefoodfragment_list_item, null);
		}
		final ImageView img_food = (ImageView) arg1.findViewById(R.id.img_food);
		TextView tv_incar= (TextView) arg1.findViewById(R.id.tv_incar);
		
		return arg1;
	}
	

}
