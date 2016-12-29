package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.QRCodeActivity;
import com.jetcloud.hgbw.bean.TakeFoodInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class TakeFoodFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<TakeFoodInfo.MealBean> list;

    @Override
    public int getCount() {
        return list.size();
    }

    public TakeFoodFragmentAdapter(Context context, List<TakeFoodInfo.MealBean> list) {
        super();
        this.context = context;
        this.list = list;
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
//        holder.img_food
        TakeFoodInfo.MealBean mealBean = list.get(position);
        String machineName = mealBean.getT_jiqi();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        holder.tv_machine_name.setText(String.format(context.getString(R.string.machine_name), "成都",machineNum));
        holder.tv_order.setText(String.format(context.getString(R.string.take_food_order_num), mealBean.getT_num()));
        holder.tv_good_name.setText(mealBean.getT_name());
        holder.tv_good_num.setText(String.format(context.getString(R.string.take_food_num),mealBean.getT_total()));
        holder.tv_price.setText(String.format(context.getString(R.string.rmb_display),(double)mealBean.getT_price()));
        holder.tv_time.setText(mealBean.getT_time());
        holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_total),(double)mealBean.getT_totalprice()));
        holder.tv_btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, QRCodeActivity.class));
            }
        });
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
