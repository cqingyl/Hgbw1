package com.example.xutilstest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Cqing on 2016/12/8.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private Context context ;
    private List<Good> data;
    private int goodNum;
    static   int goodId;
    public MyAdapter(Context context, List<Good> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shopcart_product, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        holder.tvIntro.setText(data.get(position).getTitle());
        holder.tvColorSize.setText(data.get(position).getContent());
        goodNum = data.get(position).getNumber();
        holder.tvBuyNum.setText(String.valueOf(goodNum));
        holder.tvPrice.setText(String.valueOf(data.get(position).getPrice()));
    }



    @Override
    public int getItemCount() {
        return data == null ? 0: data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.check_box)
        CheckBox checkBox;
        @ViewInject(R.id.iv_adapter_list_pic)
        ImageView ivAdapterListPic;
        @ViewInject(R.id.tv_intro)
        TextView tvIntro;
        @ViewInject(R.id.tv_color_size)
        TextView tvColorSize;
        @ViewInject(R.id.tv_price)
        TextView tvPrice;
        @ViewInject(R.id.tv_discount_price)
        TextView tvDiscountPrice;
        @ViewInject(R.id.tv_buy_num)
        TextView tvBuyNum;
        @ViewInject(R.id.rl_no_edtor)
        RelativeLayout rlNoEdtor;
        @ViewInject(R.id.tv_reduce)
        TextView tvReduce;
        @ViewInject(R.id.tv_num)
        TextView tvNum;
        @ViewInject(R.id.tv_add)
        TextView tvAdd;
        @ViewInject(R.id.ll_change_num)
        LinearLayout llChangeNum;
        @ViewInject(R.id.tv_colorsize)
        TextView tvColorsize;
        @ViewInject(R.id.tv_goods_delete)
        TextView tvGoodsDelete;
        @ViewInject(R.id.ll_edtor)
        LinearLayout llEdtor;
        @ViewInject(R.id.stub)
        ViewStub stub;
        MyViewHolder(View view) {
            super(view);
            x.view().inject(this,view);
        }

    }

}
