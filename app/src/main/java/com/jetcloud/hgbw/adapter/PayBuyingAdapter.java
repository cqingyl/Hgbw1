package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;

/**
 * Created by Cqing on 2016/12/28.
 */

public class PayBuyingAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    private List<ShopCarInfo> data;
    private int productNum = 1;
    private static final String TAG_LOG = ShopCarFragmentAdapter.class.getSimpleName();
    private Context context;
    private List<MachineInfo> groups;
    private Map<String, List<ShopCarInfo>> children;


    public PayBuyingAdapter(Context context, List<MachineInfo> groups, Map<String, List<ShopCarInfo>> children) {
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
        String groupId = groups.get(groupPosition).getNumber();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShopCarInfo> products = children.get(groups.get(groupPosition).getNumber());
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
        String machineName = machineInfo.getNickname();
        String machineLocation = machineInfo.getCity();
        Log.i(TAG_LOG, "city: " + machineLocation);
        /**String machineNum = machineName.substring(machineName.length() - 3, machineName.length());*/
        groupViewHolder.tvMachineTitle.setText(String.format(context.getString(R.string.machine_name),
                machineLocation, machineName));
        groupViewHolder.cbGroup.setChecked(true);
        groupViewHolder.cbGroup.setClickable(false);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View
            convertView, final ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_pay_buying, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

//        Log.i(TAG, "getChildView: " );
        final ShopCarInfo shopCarInfo = (ShopCarInfo) getChild(groupPosition, childPosition);
        //如果食物不为空
        if (shopCarInfo != null) {
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setFailureDrawableId(R.drawable.ic_launcher)
                    .build();
            x.image().bind(childViewHolder.img_food, HgbwUrl.HOME_URL + shopCarInfo.getPic(), imageOptions);
            childViewHolder.tv_food_title.setText(String.valueOf(shopCarInfo.getName()));
            childViewHolder.tv_price_cny.setText(context.getString(R.string.rmb_display, shopCarInfo.getPrice_cny()));
            childViewHolder.tv_price_vr9.setText(context.getString(R.string.gcb_display, shopCarInfo.getPrice_vr9()));
            childViewHolder.tv_num.setText(String.format(context.getString(R.string.take_food_num), shopCarInfo
                    .getP_local_number()));

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
        @ViewInject(R.id.cb_item)
        CheckBox cb_item;
        @ViewInject(R.id.img_food)
        ImageView img_food;
        @ViewInject(R.id.tv_food_title)
        TextView tv_food_title;
        @ViewInject(R.id.tv_price_cny)
        TextView tv_price_cny;
        @ViewInject(R.id.tv_price_vr9)
        TextView tv_price_vr9;
        @ViewInject(R.id.tv_num)
        TextView tv_num;

        ChildViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
