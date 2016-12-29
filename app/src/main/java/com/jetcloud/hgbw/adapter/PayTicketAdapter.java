package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jetcloud.hgbw.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Cqing on 2016/12/28.
 */

public class PayTicketAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<String> mData;
    public PayTicketAdapter(Context context, List<String> data) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
    }
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
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
        TicketViewHolder holder = null;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_pay_ticket, null);
            holder = new TicketViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TicketViewHolder) view.getTag();
        }
        holder.tv_ticket_content.setText(mData.get(i));
        return view;
    }

    private class TicketViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.tv_ticket_content)
        TextView tv_ticket_content;
        @ViewInject(R.id.cb_ticket)
        CheckBox cb_ticket;

        public TicketViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }
    }
}
