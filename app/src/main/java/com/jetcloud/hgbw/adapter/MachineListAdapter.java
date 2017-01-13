package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jetcloud.hgbw.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Cqing on 2017/1/6.
 */

public class MachineListAdapter extends BaseAdapter {
    private static final String TAG_LOG = MachineListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private List<String> data;
    private Context context;


    public MachineListAdapter(Context context, List<String> data) {
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
            view = mLayoutInflater.inflate(R.layout.item_machine_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
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
