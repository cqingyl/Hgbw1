package com.jetcloud.hgbw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by Cqing on 2016/12/29.
 */

public class MyWalletAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<String> mData;
    public MyWalletAdapter(Context context, List<String> data) {
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
        WalletViewHolder holder = null;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_my_wallet_gcb_card, viewGroup, false);
            holder = new WalletViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (WalletViewHolder) view.getTag();
        }
        holder.tv_cardnumber.setText(mData.get(i));
        return view;
    }

    private class WalletViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.tv_cardnumber)
        TextView tv_cardnumber;

        public WalletViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }
    }
}
