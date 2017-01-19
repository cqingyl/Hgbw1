package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.QRCodeActivity;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.TakeFoodInfo;
import com.jetcloud.hgbw.view.MyListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.jetcloud.hgbw.app.HgbwStaticString.FOOD_OUT;
import static com.jetcloud.hgbw.app.HgbwStaticString.ORDER_NUM;

/***
 * Created by Cqing on 2016/12/30.
 */

public class TakeFoodParentAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<TakeFoodInfo.OrdersBean> data;
    private static final String TAG_LOG = TakeFoodParentAdapter.class.getSimpleName();
    private Context context;
    private HgbwApplication app;
    private MachineInfo machineInfo;
    private List<TakeFoodInfo.OrdersBean.FoodInfoBean.FoodsBean> foodInfoBeanList;

    public TakeFoodParentAdapter(Context context, List<TakeFoodInfo.OrdersBean> data) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        app = (HgbwApplication) context.getApplicationContext();
        //只有一个机器
        machineInfo = app.getGroups().get(0);
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        final TakeFoodParentViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_take_food_parent, null);
            holder = new TakeFoodParentViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TakeFoodParentViewHolder) convertView.getTag();
        }
        //加载子list view
        final TakeFoodInfo.OrdersBean ordersBean = data.get(i);
        foodInfoBeanList = ordersBean.getFood_info().getFoods();
        holder.lv_myorder_in.setAdapter(new TakeFoodChildrenAdapter(context, foodInfoBeanList));

        String machineName = machineInfo.getNumber();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        holder.tv_machine_name.setText(String.format(context.getString(R.string.machine_name), "成都", machineNum));
        holder.tv_order_number.setText(String.format(context.getString(R.string.take_food_order_num), ordersBean
                .getNumber()));
        holder.tv_time.setText(String.format(context.getString(R.string.trade_time), ordersBean
                .getCreate_time()));
        if (data.get(i).getPay_type().equals(HgbwStaticString.PAY_WAY_VR9)) {
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_gcb_total), ordersBean
                    .getCost_real()));
        } else if (data.get(i).getPay_type().equals(HgbwStaticString.PAY_WAY_CNY)) {
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_total), ordersBean
                    .getCost_real()));
        }
        holder.ll_btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QRCodeActivity.class);
                intent.putExtra(FOOD_OUT, ordersBean.getFood_info().getFoodd_out());
                intent.putExtra(ORDER_NUM, ordersBean.getNumber());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class TakeFoodParentViewHolder {
        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_order_number)
        TextView tv_order_number;
        @ViewInject(R.id.ll_btn_qr)
        LinearLayout ll_btn_qr;
        @ViewInject(R.id.lv_myorder_in)
        MyListView lv_myorder_in;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.tv_total_price)
        TextView tv_total_price;

        TakeFoodParentViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
