package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Cqing on 2017/1/3.
 */

public class MyOrderInAdapter extends BaseAdapter {
    private static final String TAG_LOG = MyOrderInAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private List<String> data;
    private int productNum = 1;
    private Context context;


    public MyOrderInAdapter(Context context, List<String> data) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_my_order_in, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private static class ViewHolder {
        @ViewInject(R.id.img_food)
        ImageView img_food;
        @ViewInject(R.id.tv_food_title)
        TextView tv_food_title;
        @ViewInject(R.id.tv_money)
        TextView tv_money;
        @ViewInject(R.id.tv_num)
        TextView tv_num;

        ViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
