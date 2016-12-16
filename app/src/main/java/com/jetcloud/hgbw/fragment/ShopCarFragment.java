package com.jetcloud.hgbw.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.adapter.ShopCarFragmentAdapter;
import com.jetcloud.hgbw.bean.GoodsInfo;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcolud.hgbw.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.shopcarfragment)
public class ShopCarFragment extends BaseFragment implements ShopCarFragmentAdapter.ModifyCountInterface, ShopCarFragmentAdapter.CheckInterface {
    private static final String TAG_LOG = ShopCarFragment.class.getSimpleName();
	private static ShopCarFragment shopCarFragment;
	private ShopCarFragmentAdapter adapter;
    @ViewInject(R.id.elv_shopcar)
	private ExpandableListView elv_shopCar;
    @ViewInject(R.id.cb_all)
    private CheckBox allCheckbox;
    @ViewInject(R.id.tv_total_price)
    private TextView tvTotalPrice;
    @ViewInject(R.id.tv_go_to_pay)
    private TextView tvGoToPay;
   @ViewInject(R.id.layout_car_empty)
    private LinearLayout carEmpty;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private List<MachineInfo> groups = new ArrayList<>();
    private Map<String, List<GoodsInfo>> children = new HashMap<>();
//    private SharedPreferences preferences;
//    private static final String SHOP_CAR_NUM = "shop car num";
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
		topbar.setCenterText("购物车");
//		elv_shopCar = getView(R.id.elv_shopcar);
//        allCheckbox = getView(R.id.cb_all);
        elv_shopCar.setGroupIndicator(null);
	}

	@Override
	public void initData() {
        initListData();
		adapter = new ShopCarFragmentAdapter(getActivity(), groups, children);
        adapter.setCheckInterface(this);
        adapter.setModifyCountInterface(this);

		elv_shopCar.setAdapter(adapter);
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			elv_shopCar.expandGroup(i);
		}

	}
    /**
     *  事件
     * */
    @Event(value = {R.id.cb_all, R.id.tv_go_to_pay})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.cb_all :
                doCheckAll();
                break;
            case R.id.tv_go_to_pay:
                //结算
                //弹出dialog
                //确定后清空购物车
                break;
        }
    }
    //加载虚拟数据
    public void initListData() {
            for (int i = 0; i < 3; i++) {
                groups.add(new MachineInfo(i + "", (i + 1) + "号机器"));
                List<GoodsInfo> products = new ArrayList<GoodsInfo>();
                for (int j = 0; j <= i; j++) {
                    String[] img = {"http://pic.58pic.com/uploadfilepic/sheji/2009-11-10/58PIC_ysjihc1990_20091110aaee42cf802a80a0.jpg","http://pic.58pic.com/uploadfilepic/sheji/2009-11-10/58PIC_ysjihc1990_20091110aaee42cf802a80a0.jpg","http://pic.58pic.com/uploadfilepic/sheji/2009-11-10/58PIC_ysjihc1990_20091110aaee42cf802a80a0.jpg"};
                    products.add(new GoodsInfo("土豆片" + j, "味道好极了"+ j, j + 1, 25 + j, img[j]));
                }
                children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
            }
    }
    //点击增加按钮后
	@Override
	public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
		GoodsInfo product = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int countNum = product.getNum();
        product.setNum(++countNum);
        ((TextView)showCountView).setText(String.valueOf(countNum));
        adapter.notifyDataSetChanged();
        calculate();
	}
    //点击减少按钮后
	@Override
	public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int countNum = product.getNum();
        if(countNum > 1){
            product.setNum(--countNum);
            ((TextView)showCountView).setText(String.valueOf(countNum));
            adapter.notifyDataSetChanged();
            calculate();
        }
	}
    //删除食物item
	@Override
	public void childDelete(int groupPosition, int childPosition) {

	}

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        MachineInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
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
        List<GoodsInfo> childs = children.get(group.getId());
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
            List<GoodsInfo> childs = children.get(group.getId());
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
        for (int i = 0; i < groups.size(); i++) {
            MachineInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isSelected()) {
                    totalCount++;
                    totalPrice += product.getMoney() * product.getNum();
                }
            }
        }

        tvTotalPrice.setText(getString(R.string.rmb_display, totalPrice));
        tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if(totalCount==0){
            setCartNum();
        } else{
//            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putInt(SHOP_CAR_NUM, totalCount);
            topbar.setCenterText("购物车" + "(" + totalCount + ")");
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
            List<GoodsInfo> childs = children.get(group.getId());
            for (GoodsInfo goodsInfo : childs) {
                count += 1;
            }
        }
        //购物车已清空
        if(count==0){
            clearCart();
        } else{
            topbar.setCenterText("购物车" + "(" + count + ")");
        }
    }
    /**
     * 清空购物车
     * */
    private void clearCart() {
        topbar.setCenterText("购物车" + "(" + 0 + ")");
//        subtitle.setVisibility(View.GONE);
//        llCart.setVisibility(View.GONE);
        carEmpty.setVisibility(View.VISIBLE);
    }
}
