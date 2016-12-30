package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;

/**
 * Created by Cqing on 2016/12/30.
 */

public class MyOrderAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    private List<ShopCarInfo> data;
    private int productNum = 1;
    private static final String TAG_LOG = MyOrderAdapter.class.getSimpleName();
    private Context context;
    private List<MachineInfo> groups;
    private Map<String, List<ShopCarInfo>> children;


    public MyOrderAdapter(Context context, List<MachineInfo> groups, Map<String, List<ShopCarInfo>> children) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getGroupCount() {
        return (groups != null) ? groups.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShopCarInfo> products = children.get(groups.get(groupPosition).getId());
        return products.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.shopcarfragmentlist_grop_item, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final MachineInfo machineInfo = (MachineInfo) getGroup(groupPosition);
        String machineName = machineInfo.getId();
        String machineNum = machineName.substring(machineName.length() - 3, machineName.length());
        groupViewHolder.tvMachineTitle.setText(String.format(context.getString(R.string.machine_name), "成都",
                machineNum));
        groupViewHolder.cbGroup.setChecked(true);
        groupViewHolder.cbGroup.setClickable(false);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View
            convertView, final ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_order, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

//        Log.i(TAG, "getChildView: " );
        final ShopCarInfo shopCarInfo = (ShopCarInfo) getChild(groupPosition, childPosition);
        //如果食物不为空
        if (shopCarInfo != null) {
//            ImageOptions imageOptions = new ImageOptions.Builder()
//                    .setFailureDrawableId(R.drawable.ic_launcher)
//                    .build();
//            x.image().bind(childViewHolder.imgFood, HgbwUrl.BASE_URL + shopCarInfo.getP_picture(), imageOptions);
//            Log.i(TAG, "getChildView: " +  HgbwUrl.BASE_URL + shopCarInfo.getP_picture());

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    private static class GroupViewHolder {
        @ViewInject(R.id.tv_machine_title)
        TextView tvMachineTitle;
        @ViewInject(R.id.cb_group)
        CheckBox cbGroup;

        GroupViewHolder(View view) {
            x.view().inject(this, view);
        }
    }

    private static class ChildViewHolder {

        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_order_number)
        TextView tv_order_number;
        @ViewInject(R.id.tv_btn_apply)
        TextView tv_btn_apply;
        @ViewInject(R.id.img_food)
        ImageView img_food;
        @ViewInject(R.id.tv_food_title)
        TextView tv_food_title;
        @ViewInject(R.id.tv_money)
        TextView tv_money;
        @ViewInject(R.id.tv_num)
        TextView tv_num;
        @ViewInject(R.id.tv_time)
        TextView tv_time;
        @ViewInject(R.id.tv_total_price)
        TextView tv_total_price;
        @ViewInject(R.id.btn_cancel)
        Button btn_cancel;
        @ViewInject(R.id.btn_go_to_pay)
        Button btn_go_to_pay;

        ChildViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
