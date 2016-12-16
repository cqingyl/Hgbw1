package com.jetcloud.hgbw.view;

import android.content.Context;
import android.util.AttributeSet;

public class MyExpandableListView extends android.widget.ExpandableListView{

	
	public MyExpandableListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		   int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
				   
			        MeasureSpec.AT_MOST);  
			  
			        super.onMeasure(widthMeasureSpec, expandSpec); 
	}
}
