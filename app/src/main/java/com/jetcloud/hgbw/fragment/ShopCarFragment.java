package com.jetcloud.hgbw.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.CarPayActivity;
import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.adapter.ShopCarFragmentAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.ShopCarUtil;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.jetcloud.hgbw.R.id.cb_all;

@ContentView(R.layout.fragment_shopcar)
public class ShopCarFragment extends BaseFragment implements ShopCarFragmentAdapter.ModifyCountInterface,
        ShopCarFragmentAdapter.CheckInterface {
    private static final String TAG_LOG = ShopCarFragment.class.getSimpleName();
    private static ShopCarFragment shopCarFragment;
    private ShopCarFragmentAdapter adapter;
//    @ViewInject(R.id.elv_shopcar)
    private ExpandableListView elv_shopCar;
    private ArrayList<ShopCarInfo> listObj;
    @ViewInject(cb_all)
    private CheckBox allCheckbox;
    @ViewInject(R.id.tv_price_cny)
    private TextView tvTotalPrice;
    @ViewInject(R.id.tv_go_to_pay)
    private TextView tvGoToPay;
    @ViewInject(R.id.layout_car_empty)
    private LinearLayout carEmpty;
    @ViewInject(R.id.tv_price_vr9)
    private TextView tv_price_vr9;
    private double totalPrice = 0.00;// 购买的商品总价
    private double totalGcb;
    private int totalCount = 0;// 购买的商品总数量
    private int total;
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<ShopCarInfo>> children = new HashMap<>();
    private HgbwApplication app;
    private boolean isFirst = true;
    public final static String MACHINE_OBJECT = "machine_object";
    public final static String PRODUC_OBJECT = "product_object";

    public static ShopCarFragment newInstance() {
        if (shopCarFragment == null) {
            shopCarFragment = new ShopCarFragment();
        }
        return shopCarFragment;
    }


    @Override
    public View initRootView(LayoutInflater inflater, ViewGroup container) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void initView() {
        app = (HgbwApplication) getActivity().getApplication();
//		topbar.setCenterText("购物车");
		elv_shopCar = getView(R.id.elv_shopcar);
       allCheckbox = getView(R.id.cb_all);

    }

    @Override
    public void initData() {
        groups = new ArrayList<>();
        children = new HashMap<>();
        loadListData();
        adapter = new ShopCarFragmentAdapter(getActivity(),groups, children);
//        Log.i(TAG, "onHiddenChanged: "+ elv_shopCar + groups + children + adapter);
        elv_shopCar.setAdapter(adapter);
        elv_shopCar.setGroupIndicator(null);
        adapter.setCheckInterface(this);// 关键步骤1,设置复选框接口
        adapter.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口

        for (int i = 0; i < adapter.getGroupCount(); i++) {
            elv_shopCar.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    //判断是不是空购物车
    private boolean isEmptyCar() {
//        Log.i(TAG, "isEmptyCar: " + total);
        if (total < 1) {
            clearCart();
            return false;
        } else {
            topbar.setCenterText("购物车" + "(" + total + ")");
            carEmpty.setVisibility(View.GONE);
            return true;
        }
    }

    //加载数据
    public void loadListData() {
        try {

            List<MachineInfo> machine = app.db.selector(MachineInfo.class).findAll();
            if (machine != null) {
                Log.i(TAG_LOG, "loadListData: " + machine.size());
                groups.clear();
                groups.addAll(machine);
                for (int i = 0; i < groups.size(); i++) {
                    children.clear();
                    List<ShopCarInfo> good = app.db.selector(ShopCarInfo.class).where("p_machine", "=", groups.get(i).getNumber()).findAll();

                    children.put(groups.get(i).getNumber(), good);
//                        Log.i(TAG, "loadListData: " + children.get());


//                adapter.notifyDataSetChanged();
//            Log.i(TAG, "machine name: " + groups.get(0).getId());
//            Log.i(TAG, "machine name: " + children.get(groups.get(0).getId()).get(0).getP_local_number());
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG_LOG, "查询失败: " + e.getMessage());
        }

    }

    /**
     * fragment 采用 add(),hide()方式，它的生命周期依赖于activity，so当切换fragment时onResume只会实现一次，
     * so，当其他fragment变化时，点会此个fragment，此个fragment 会做出相应的变化
     * onHiddenChanged() 在 触发isHide() 后执行
     */

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!isFirst) {
            Log.i(TAG_LOG, "3 onHiddenChanged: " + hidden);
        }
        if (hidden && !isFirst) {
            SharedPreferenceUtils.setShopCarNumber(total);
            Log.i(TAG_LOG, "S onHiddenChanged: " + total);
        } else if (!hidden && !isFirst) {
            if (total != SharedPreferenceUtils.getShopCarNumber()){
                total = SharedPreferenceUtils.getShopCarNumber();
                Log.i(TAG_LOG, "S onHiddenChanged: " + total);
            }
            totalPrice = 0;
            totalGcb = 0;
            allCheckbox.setChecked(false);
            tvGoToPay.setText("去支付(" + 0 + ")");
            tvTotalPrice.setText(getString(R.string.take_food_total, totalPrice));
            tv_price_vr9.setText(getString(R.string.take_food_gcb_total, totalGcb));
            initData();
            isEmptyCar();
        }
        isFirst = false;
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        Log.i(TAG_LOG, "3 onResume: ");
        total = SharedPreferenceUtils.getShopCarNumber();
        //购物车角标
        ShopCarUtil.ChangeCorner(getActivity(), total);
        isEmptyCar();
//        Log.i(TAG_LOG, "onResume: " + total);
        super.onResume();
    }


    /**
     * 事件
     */
    @Event(value = {cb_all, R.id.tv_go_to_pay})
    private void getEvent(View view) {
        switch (view.getId()) {
            case cb_all:
                doCheckAll();
                break;
            case R.id.tv_go_to_pay:
                //结算
                if (totalCount == 0) {
                    Toast.makeText(getActivity(), "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    } else {
                        saveListToApp();
                        Intent intent = new Intent(getActivity(), CarPayActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
        }
    }

    /**
     * 保存数据到全局list
     * **/
    public void saveListToApp() {
        List<MachineInfo> newGroupsList = new ArrayList<>();
        Map<String,List<ShopCarInfo>> newList = new HashMap<>();
        for (int i = 0; i < groups.size(); i ++){
            MachineInfo machineInfo = groups.get(i);
            List<ShopCarInfo> shopCarInfos = children.get(machineInfo.getNumber());
            List<ShopCarInfo> newInfoList = new ArrayList<>();
            for (int j = 0; j < shopCarInfos.size(); j ++){
                ShopCarInfo shopCarInfo = shopCarInfos.get(j);
                //挑出被选中的商品保存到全局
                if ( shopCarInfo.isSelected()) {
                    newInfoList.add(shopCarInfo);
                }
            }
            if ( newInfoList.size() > 0){
                newGroupsList.add(machineInfo);
                newList.put(machineInfo.getNumber(), newInfoList);
            }
        }
        app.setGroups(newGroupsList);
        app.setChildren(newList);
        app.setTotalGcb(totalGcb);
        app.setTotalPrice(totalPrice);
    }
    //点击增加按钮后
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCarInfo product = (ShopCarInfo) adapter.getChild(groupPosition, childPosition);

        int countNum = product.getP_local_number();
        product.setP_local_number(++countNum);
        ShopCarUtil.ChangeCorner(getActivity(), ++total);
        SharedPreferenceUtils.setShopCarNumber(total);
        Log.i(TAG, "doIncrease: " + total);
        isEmptyCar();
        try {
            app.db.saveOrUpdate(product);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG_LOG, " 增加数量失败： " + e.getMessage());
        }
//        try {
//            ShopCarInfo goodsInfo = db.selector(ShopCarInfo.class).findFirst();
//            Log.i(TAG_LOG, "doIncrease: " + goodsInfo.getP_numb());
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        ((TextView) showCountView).setText(String.valueOf(countNum));
        adapter.notifyDataSetChanged();
        calculate();
    }

    //点击减少按钮后
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCarInfo product = (ShopCarInfo) adapter.getChild(groupPosition, childPosition);
        int countNum = product.getP_local_number();
        if (countNum > 1) {
            product.setP_local_number(--countNum);
//            ShopCarUtil.decCornerNum(getActivity(),1);
            ShopCarUtil.ChangeCorner(getActivity(), --total);
            SharedPreferenceUtils.setShopCarNumber(total);
            isEmptyCar();
            try {
                app.db.saveOrUpdate(product);
            } catch (DbException e) {
                e.printStackTrace();
                Log.e(TAG_LOG, " 减少数量失败： " + e.getMessage());
            }
//            try {
//                ShopCarInfo goodsInfo = db.selector(ShopCarInfo.class).findFirst();
//                Log.i(TAG_LOG, "doDecrease: " + goodsInfo.getP_numb());
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            ((TextView) showCountView).setText(String.valueOf(countNum));
            adapter.notifyDataSetChanged();
            calculate();
        }
    }

    //删除item
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        ShopCarInfo shopCarInfo = children.get(groups.get(groupPosition).getNumber()).remove(childPosition);
        WhereBuilder wbc = WhereBuilder.b("id", "=", shopCarInfo.getId());
        int goodNum = shopCarInfo.getP_local_number();
        try {
            app.db.delete(ShopCarInfo.class, wbc);
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG_LOG, " 删除child失败： " + e.getMessage());
        }
        //减少商品总量
        total -= goodNum;
        if (total < 0) {
            total = 0;
        }
        ShopCarUtil.ChangeCorner(getActivity(), total);
        SharedPreferenceUtils.setShopCarNumber(total);
        isEmptyCar();
        MachineInfo group = groups.get(groupPosition);
        List<ShopCarInfo> childs = children.get(group.getNumber());
        if (childs.size() == 0) {
            WhereBuilder wbg = WhereBuilder.b("id", "=", groups.get(groupPosition).getNumber());
            try {
                app.db.delete(MachineInfo.class, wbg);
            } catch (DbException e) {
                e.printStackTrace();
                Log.e(TAG_LOG, " 删除group失败： " + e.getMessage());
            }
            children.remove(groups.get(groupPosition).getNumber());
            groups.remove(groupPosition);
        }
        adapter.notifyDataSetChanged();
        Log.i(TAG, "childDelete: " +groups.size()+children.size());
        //     handler.sendEmptyMessage(0);
        calculate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        MachineInfo group = groups.get(groupPosition);
        List<ShopCarInfo> childs = children.get(group.getNumber());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setSelected(isChecked);
        }
        if (isAllCheck())
            allCheckbox.setChecked(true);
        else
            allCheckbox.setChecked(false);
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        MachineInfo group = groups.get(groupPosition);
        List<ShopCarInfo> childs = children.get(group.getNumber());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isSelected() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setSelected(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setSelected(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allCheckbox.setChecked(true);// 全选
        } else {
            allCheckbox.setChecked(false);// 反选
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    private boolean isAllCheck() {
        for (MachineInfo group : groups) {
            if (!group.isSelected())
                return false;
        }
        return true;
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setSelected(allCheckbox.isChecked());
            MachineInfo group = groups.get(i);
            List<ShopCarInfo> childs = children.get(group.getNumber());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setSelected(allCheckbox.isChecked());
            }
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        totalGcb = 0.00;

        for (int i = 0; i < groups.size(); i++) {
            MachineInfo group = groups.get(i);
            List<ShopCarInfo> childs = children.get(group.getNumber());
            for (int j = 0; j < childs.size(); j++) {
                ShopCarInfo product = childs.get(j);
                if (product.isSelected()) {
                    totalCount++;
                    totalPrice += product.getPrice_cny() * product.getP_local_number();
                    totalGcb += product.getPrice_vr9() * product.getP_local_number();
                }
            }
        }

        tvTotalPrice.setText(getString(R.string.take_food_total, totalPrice));
        tv_price_vr9.setText(getString(R.string.take_food_gcb_total, totalGcb));
        tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if (totalCount == 0) {
            setCartNum();
        } else {
//            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putInt(SHOP_CAR_NUM, totalCount);
//            topbar.setCenterText("购物车" + "(" + totalCount + ")");
        }
    }

    /**
     * 设置购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setSelected(allCheckbox.isChecked());
            MachineInfo group = groups.get(i);
            List<ShopCarInfo> childs = children.get(group.getNumber());
            for (ShopCarInfo shopCarInfo : childs) {
                count += 1;
            }
        }
        //购物车已清空
        if (count == 0) {
            clearCart();
        } else {
//            topbar.setCenterText("购物车" + "(" + count + ")");
        }
    }

    /**
     * 清空购物车
     */
    private void clearCart() {
        topbar.setCenterText("购物车" + "(" + 0 + ")");
//        subtitle.setVisibility(View.GONE);
//        llCart.setVisibility(View.GONE);
        carEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        groups.clear();
        totalPrice = 0;
        totalGcb = 0;
        children.clear();
    }
}
