package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.TakeFoodInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class TakeFoodFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<TakeFoodInfo.OrdersBean> list;

    public TakeFoodFragmentAdapter(Context context, List<TakeFoodInfo.OrdersBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.takefoodfragment_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder {
        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_order)
        TextView tv_order;
        @ViewInject(R.id.tv_btn_qr)
        TextView tv_btn_qr;
        @ViewInject(R.id.img_food)
        ImageView img_food;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.tv_good_name)
        TextView tv_good_name;
        @ViewInject(R.id.tv_price)
        TextView tv_price;
        @ViewInject(R.id.tv_good_num)
        TextView tv_good_num;
        @ViewInject(R.id.tv_total_price)
        TextView tv_total_price;

        ViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
