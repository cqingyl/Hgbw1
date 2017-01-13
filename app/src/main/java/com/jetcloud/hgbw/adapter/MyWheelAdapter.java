package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.MachineLocationBean;
import com.wx.wheelview.adapter.BaseWheelAdapter;

import java.util.List;

/**
 * Created by Cqing on 2017/1/11.
 */

public class MyWheelAdapter extends BaseWheelAdapter {

    private Context context;
    private List<MachineLocationBean.MechinesBean> data;
    public MyWheelAdapter(Context context, List<MachineLocationBean.MechinesBean> data){
        this.context = context;
        this.data = data;
    }
    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_into_account, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText((data.get(position).getNickname()));

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.tv_account);
        }
    }
}
