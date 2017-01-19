package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.QRCodeActivity;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.MyOrderBean;
import com.jetcloud.hgbw.view.MyListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.jetcloud.hgbw.app.HgbwStaticString.FOOD_OUT;
import static com.jetcloud.hgbw.app.HgbwStaticString.ORDER_NUM;

/***
 * Created by Cqing on 2016/12/30.
 */

public class MyOrderParentAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<MyOrderBean.OrdersBean> data;
    private static final String TAG_LOG = MyOrderParentAdapter.class.getSimpleName();
    private Context context;
    private HgbwApplication app;
    private MachineInfo machineInfo;
    private List<MyOrderBean.OrdersBean.FoodInfoBean.FoodsBean> foodInfoBeanList;

    public MyOrderParentAdapter(Context context, List<MyOrderBean.OrdersBean> data) {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final MyOrderViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_order_parent, null);
            holder = new MyOrderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyOrderViewHolder) convertView.getTag();
        }
        //加载子list view
        final MyOrderBean.OrdersBean ordersBean = data.get(i);
        foodInfoBeanList = ordersBean.getFood_info().getFoods();
        holder.lv_myorder_in.setAdapter(new MyOrderChildrenAdapter(context, foodInfoBeanList));

        String machineName = machineInfo.getNumber();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        holder.tv_machine_name.setText(String.format(context.getString(R.string.machine_name),"成都",machineNum));
        holder.tv_order_number.setText(String.format(context.getString(R.string.take_food_order_num), ordersBean.getNumber()));
        holder.tv_time.setText(String.format(context.getString(R.string.trade_time), ordersBean
                .getCreate_time()));

        if (ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_VR9)) {
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_gcb_total), ordersBean.getCost_real()));
        } else if (ordersBean.getPay_type().equals(HgbwStaticString.PAY_WAY_CNY)){
            holder.tv_total_price.setText(String.format(context.getString(R.string.take_food_total), ordersBean.getCost_real()));
        }

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.btn_go_to_take_food.setOnClickListener(new View.OnClickListener() {
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
        @ViewInject(R.id.btn_go_to_take_food)
        Button btn_go_to_take_food;

        MyOrderViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
