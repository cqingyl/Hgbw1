package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.MyOrderBean;
import com.jetcloud.hgbw.view.MyListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cqing on 2016/12/30.
 */

public class MyOrderOutAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<MyOrderBean.OrdersBean> data;
    private int productNum = 1;
    private static final String TAG_LOG = MyOrderOutAdapter.class.getSimpleName();
    private Context context;


    public MyOrderOutAdapter(Context context, List<MyOrderBean.OrdersBean> data) {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final MyOrderViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_order_out, null);
            holder = new MyOrderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyOrderViewHolder) convertView.getTag();
        }

//            ImageOptions imageOptions = new ImageOptions.Builder()
//                    .setFailureDrawableId(R.drawable.ic_launcher)
//                    .build();
//            x.image().bind(childViewHolder.imgFood, HgbwUrl.BASE_URL + shopCarInfo.getP_picture(), imageOptions);
//            Log.i(TAG, "getChildView: " +  HgbwUrl.BASE_URL + shopCarInfo.getP_picture());

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.lv_myorder_in.setAdapter(new MyOrderInAdapter(context, new ArrayList<String>()));
        holder.btn_go_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }

    private static class MyOrderViewHolder {
        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_order_number)
        TextView tv_order_number;
        @ViewInject(R.id.tv_btn_apply)
        TextView tv_btn_apply;
        @ViewInject(R.id.lv_myorder_in)
        MyListView lv_myorder_in;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.tv_total_price)
        TextView tv_total_price;
        @ViewInject(R.id.btn_cancel)
        Button btn_cancel;
        @ViewInject(R.id.btn_go_to_pay)
        Button btn_go_to_pay;

        MyOrderViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
