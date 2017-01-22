package com.jetcloud.hgbw.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.DetailsActivity;
import com.jetcloud.hgbw.activity.HomePayActivity;
import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.FoodBean;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jetcloud.hgbw.app.HgbwStaticString.FOOD_ID;
import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE;


public class HomeFragmentAdapter extends BaseAdapter {
    private Activity context;
    private List<FoodBean.DataBean> list;
    private HolderClickListener mHolderClickListener;
    private AddGoodNumberInterface numberInterface;
    private HgbwApplication app;
    private MachineInfo machineInfo;
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    public void setData(List<FoodBean.DataBean> data){
        this.list = data;
        machineInfo = app.getGroups().get(0);
        this.notifyDataSetChanged();
    }
    public HomeFragmentAdapter(Activity context) {
        super();
        this.context = context;
        app = (HgbwApplication) context.getApplication();
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
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setFailureDrawableId(R.drawable.ic_launcher)
                .build();
        String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + list.get(position).getPic());
        x.image().bind(holder.imgFood, imgPath, imageOptions);
        holder.tvFoodName.setText(list.get(position).getName());
        holder.tvMachineName.setText(machineInfo.getNumber());
        holder.tvAddress.setText(machineInfo.getAddress());
        final ImageView img_food = holder.imgFood;
        final FoodBean.DataBean dataBean = list.get(position);

        holder.tv_price_vr9.setText(context.getString(R.string.rmb_display, dataBean.getPrice_cny()));
        holder.tv_price_vr9.setText(context.getString(R.string.gcb_display, dataBean.getPrice_vr9()));
        holder.tvIncar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mHolderClickListener != null) {
                    int[] start_location = new int[2];
                    img_food.getLocationInWindow(start_location);//获取点击商品图片的位置
                    Drawable drawable = img_food.getDrawable();//复制一个新的商品图标
                    mHolderClickListener.onHolderClick(drawable, start_location);
                    numberInterface.addGoodNumber(dataBean);
                }
            }
        });
//        holder.ivDetils.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//            }
//        });
        holder.tvBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                } else {
                    Intent i = new Intent(context, HomePayActivity.class);
                    saveListToApp(dataBean);
                    context.startActivity(i);
                }
            }
        });
        holder.ll_all_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(new Intent(context, DetailsActivity.class));
//                saveListToApp(dataBean);
                i.putExtra(MACHINE, machineInfo);
                i.putExtra(FOOD_ID, String.valueOf(dataBean.getId()));
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private void saveListToApp(FoodBean.DataBean dataBean) {
        Map<String,List<ShopCarInfo>> newList = new HashMap<>();
        List<ShopCarInfo> newInfoList = new ArrayList<>();
        List<MachineInfo> newMachineList = new ArrayList<>();
        newMachineList.add(machineInfo);
        app.setGroups(newMachineList);
        newInfoList.add(dataBean);
        newList.put(machineInfo.getNumber(), newInfoList);
        app.setChildren(newList);
    }
    public interface AddGoodNumberInterface {
        public void addGoodNumber(FoodBean.DataBean dataBean);
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
        @ViewInject(R.id.ll_all_layout)
        private LinearLayout ll_all_layout;
        @ViewInject(R.id.iv_detils)
        private ImageView ivDetils;
        @ViewInject(R.id.tv_btn_apply)
        private TextView tvBtnApply;
        @ViewInject(R.id.tv_address)
        private TextView tvAddress;
        @ViewInject(R.id.tv_price_cny)
        private TextView tv_price_cny;
        @ViewInject(R.id.tv_price_vr9)
        private TextView tv_price_vr9;
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
