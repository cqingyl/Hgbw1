package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.activity.DetailsActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcolud.hgbw.R;

import java.util.List;

public class HomeFragmentAdapter extends BaseAdapter{
	private Context context ;
	private List<String> list;
	private HolderClickListener mHolderClickListener;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public HomeFragmentAdapter(Context context, List<String> list) {
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
			arg1 = View.inflate(context, R.layout.homefragment_list_item, null);
		}
		final ImageView img_food = (ImageView) arg1.findViewById(R.id.img_food);
		ImageView iv_detils = (ImageView) arg1.findViewById(R.id.iv_detils);
		TextView tv_incar= (TextView) arg1.findViewById(R.id.tv_incar);
		tv_incar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
                if(mHolderClickListener!=null){
                    int[] start_location = new int[2];
                    img_food.getLocationInWindow(start_location);//获取点击商品图片的位置
                    Drawable drawable = img_food.getDrawable();//复制一个新的商品图标
                    mHolderClickListener.onHolderClick(drawable,start_location);
                }
			}
		});
		iv_detils.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.mainActivity.startActivity(new Intent(context,DetailsActivity.class));
			}
		});
		return arg1;
	}
	 public void SetOnSetHolderClickListener(HolderClickListener holderClickListener){
	        this.mHolderClickListener = holderClickListener;
	    }
	    public interface HolderClickListener{
	        public void onHolderClick(Drawable drawable,int[] start_location);
	    }

}
