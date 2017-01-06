package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.bean.MachineListBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Cqing on 2017/1/6.
 */

public class MachineListAdapter extends BaseAdapter {
    private static final String TAG_LOG = MachineListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private List<MachineListBean.DataBean> data;
    private Context context;


    public MachineListAdapter(Context context, List<MachineListBean.DataBean> data) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public MachineListBean.DataBean getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_machine_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_machine_name.setText(context.getString(R.string.machine_list_name, i + 1));
        holder.tv_machine_num.setText(context.getString(R.string.machine_list_num, getItem(i).getNumber()));
        holder.tv_machine_address.setText(context.getString(R.string.machine_list_address, getItem(i).getLocate()));
        return view;
    }


    private static class ViewHolder {
        @ViewInject(R.id.tv_machine_name)
        TextView tv_machine_name;
        @ViewInject(R.id.tv_machine_num)
        TextView tv_machine_num;
        @ViewInject(R.id.tv_machine_address)
        TextView tv_machine_address;

        ViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
