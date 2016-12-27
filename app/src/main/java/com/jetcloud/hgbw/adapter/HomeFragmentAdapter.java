package com.jetcloud.hgbw.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetcloud.hgbw.activity.DetailsActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcolud.hgbw.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.jetcolud.hgbw.R.id.iv_detils;

public class HomeFragmentAdapter extends BaseAdapter {
    private Activity context;
    private List<ShopCarInfo> list;
    private HolderClickListener mHolderClickListener;
    private AddGoodNumberInterface numberInterface;
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public void setData(List<ShopCarInfo> data){
        this.list = data;
        this.notifyDataSetChanged();
    }
    public HomeFragmentAdapter(Activity context, List<ShopCarInfo> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.homefragment_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvFoodName.setText(list.get(position).getP_name());
        holder.tvMachineName.setText(list.get(position).getP_machine());
        holder.tvAddress.setText(list.get(position).getP_address());
        holder.tvPrice.setText(String.valueOf(list.get(position).getP_price()));
        final ImageView img_food = holder.imgFood;
        final ShopCarInfo shopCarInfo = list.get(position);
        holder.tvIncar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mHolderClickListener != null) {
                    int[] start_location = new int[2];
                    img_food.getLocationInWindow(start_location);//获取点击商品图片的位置
                    Drawable drawable = img_food.getDrawable();//复制一个新的商品图标
                    mHolderClickListener.onHolderClick(drawable, start_location);
                    numberInterface.addGoodNumber(shopCarInfo);
                }
            }
        });
        holder.ivDetils.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(new Intent(context, DetailsActivity.class));
                i.putExtra(DetailsActivity.FOOD_OBJECT,shopCarInfo);
                MainActivity.mainActivity.startActivity(i);
            }
        });
        return convertView;
    }

    public interface AddGoodNumberInterface {
        public void addGoodNumber(ShopCarInfo shopCarInfo);
    }

    public AddGoodNumberInterface getNumberInterface() {
        return numberInterface;
    }

    public void setNumberInterface(AddGoodNumberInterface numberInterface) {
        this.numberInterface = numberInterface;
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }


    class ViewHolder {
        @ViewInject(iv_detils)
        private ImageView ivDetils;
        @ViewInject(R.id.tv_btn_apply)
        private TextView tvBtnApply;
        @ViewInject(R.id.tv_address)
        private TextView tvAddress;
        @ViewInject(R.id.tv_price)
        private TextView tvPrice;
        @ViewInject(R.id.tv_food_name)
        private TextView tvFoodName;
        @ViewInject(R.id.tv_machine_name)
        private TextView tvMachineName;
        @ViewInject(R.id.img_food)
        private ImageView imgFood;
        @ViewInject(R.id.tv_incar)
        private TextView tvIncar;

        public ViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
