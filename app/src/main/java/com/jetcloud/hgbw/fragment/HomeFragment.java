package com.jetcloud.hgbw.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.DetailsActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.adapter.HomeFragmentAdapter;
import com.jetcloud.hgbw.adapter.MyWheelAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.baidumap.BaiduLocation;
import com.jetcloud.hgbw.baidumap.LocationActivity;
import com.jetcloud.hgbw.bean.FoodBean;
import com.jetcloud.hgbw.bean.MachineBannerBean;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.MachineLocationBean;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.ShopCarUtil;
import com.jetcloud.hgbw.utils.TakeInShopCarAnim;
import com.jetcloud.hgbw.view.CusAlertDialogWithTwoBtn;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.ImageCycleView;
import com.jetcloud.hgbw.view.ImageCycleView.ImageCycleViewListener;
import com.jetcloud.hgbw.view.MyListView;
import com.jetcloud.hgbw.view.ObservableScrollView;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.jetcloud.hgbw.app.HgbwStaticString.FOOD_ID;
import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE;
import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE_LIST;
import static org.xutils.x.http;


@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements HomeFragmentAdapter.AddGoodNumberInterface, BaiduLocation
        .MyLocationListener {
    private final static String TAG_LOG = HomeFragment.class.getSimpleName();
    private static HomeFragment homeFragment;
    @ViewInject(R.id.binner)
    ImageCycleView binner;
    @ViewInject(R.id.rb_home)
    RadioButton rb_home;
    @ViewInject(R.id.rb_takefood)
    RadioButton rb_takefood;
    @ViewInject(R.id.rb_shopcar)
    RadioButton rb_shopcar;
    @ViewInject(R.id.rb_mine)
    RadioButton rb_mine;
    @ViewInject(R.id.rdo_group)
    RadioGroup rdo_group;
    @ViewInject(R.id.bottom_bar)
    LinearLayout bottom_bar;
    @ViewInject(R.id.lv_home)
    MyListView lv_home;
    @ViewInject(R.id.sv_all_layout)
    ObservableScrollView sv_all_layout;
    @ViewInject(R.id.iv_location)
    ImageView iv_location;
    @ViewInject(R.id.tv_city)
    TextView tv_city;
    @ViewInject(R.id.ll_location)
    LinearLayout ll_location;
    @ViewInject(R.id.tv_machine_name)
    TextView tv_machine_name;
    @ViewInject(R.id.ll_choose_machine)
    LinearLayout ll_choose_machine;
    @ViewInject(R.id.ll_search)
    LinearLayout ll_search;
    @ViewInject(R.id.ll_top_nav)
    LinearLayout ll_top_nav;
    private HomeFragmentAdapter adapter;
    private WheelView wheelView;
    private PopupWindow window;
    private String machineNum;
    private String nickName;
    private List<MachineLocationBean.MechinesBean> mechinesBeanList;
    private List<FoodBean.DataBean> foodBeanDataBeanList;
    private MachineLocationBean.MechinesBean mechinesBean;
    private List<MachineBannerBean.BannerBean> bannerBeanList;
    private static final int CHOOSE_NEW_FOOD = 0;
    private static final int CHOOSE_HOT_FOOD = 1;
    private static final int CHOOSE_SET_MEAL = 2;
    private static final int CHOOSE_DRINK = 3;
    private int scrollX;
    private int scrollY;
    private HgbwApplication app;
    private int total;
    private boolean isFirst = true;
//坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑
    /**
     * 轮播图点击事件
     */
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            if (bannerBeanList.get(position).getBtype().equals("A")) {
                String foodId = String.valueOf(bannerBeanList.get(position).getUrl());
                Log.i("log", "onImageClick: " + foodId);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(MACHINE, mechinesBean);
                intent.putExtra(FOOD_ID, foodId);
                startActivity(intent);
            }
        }
    };
    //坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑坑
    // banner图数据
    private ArrayList<String> mImageUrl = new ArrayList<String>();

    public static HomeFragment newInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public View initRootView(LayoutInflater inflater, ViewGroup container) {
        // TODO Auto-generated method stub
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
    if (!isFirst) {
        Log.i(TAG_LOG, "1 onHiddenChanged: " + hidden);
    }
        if (!hidden && !isFirst) {
            ShopCarUtil.ChangeCorner(getActivity(), SharedPreferenceUtils.getShopCarNumber());
//            mHandler.postDelayed(mScrollView, 100);
//            ll_top_nav.setBackgroundResource(R.drawable.btn);
//            ll_location.setBackgroundResource(R.drawable.btn);
//            ll_search.setBackgroundResource(R.drawable.btn);
//            ll_choose_machine.setBackgroundResource(R.drawable.btn);
        }
        isFirst = false;
        super.onHiddenChanged(hidden);
    }
//    Handler  mHandler = new Handler();
//    private Runnable mScrollView = new Runnable(){
//        @Override
//        public void run() {
//            sv_all_layout.scrollTo(scrollX, scrollY);//改变滚动条的位置  
//        }
//    };
    @Override
    public void onResume() {
//        Log.i(TAG_LOG, "1 onResume: ");
        ShopCarUtil.ChangeCorner(getActivity(), SharedPreferenceUtils.getShopCarNumber());
//        mHandler.postDelayed(mScrollView, 100);
//        ll_top_nav.setBackgroundResource(R.drawable.btn);
//        ll_location.setBackgroundResource(R.drawable.btn);
//        ll_search.setBackgroundResource(R.drawable.btn);
//        ll_choose_machine.setBackgroundResource(R.drawable.btn);
        super.onResume();
    }



    @Override
    protected void initView() {

        app = (HgbwApplication) getActivity().getApplication();
//        binner = getView(R.id.binner);
//        tv_city = getView(R.id.tv_city);
//        listView = getView(R.id.lv_home);
//        ll_choose_machine = getView(R.id.ll_choose_machine);
//        tv_machine_name = getView(R.id.tv_machine_name);

//        /**滚动时设置透明度*/
//        sv_all_layout.setScrollViewListener(new ScrollViewListener() {
//            @Override
//            public void onScrollChanged(ObservableScrollView scrollView, int l, int t, int oldl, int oldt, boolean
//                    isStop) {
//                if (isStop) {
//                    ll_top_nav.setBackgroundResource(R.drawable.btn);
//                    ll_location.setBackgroundResource(R.drawable.btn);
//                    ll_search.setBackgroundResource(R.drawable.btn);
//                    ll_choose_machine.setBackgroundResource(R.drawable.btn);
//                } else {
//                    ll_top_nav.setBackgroundResource(R.color.transparent);
//                    ll_location.setBackgroundResource(R.color.transparent);
//                    ll_search.setBackgroundResource(R.color.transparent);
//                    ll_choose_machine.setBackgroundResource(R.color.transparent);
//                }
//                scrollX = l;
//                scrollY = t;
//                Log.i(TAG_LOG, "onScrollChanged: " + t + " " + oldt);
//                Log.i(TAG_LOG, "onScrollChanged: " + isStop);
//
//            }
//        });
        adapter = new HomeFragmentAdapter(getActivity());
        adapter.setNumberInterface(HomeFragment.this);
        lv_home.setAdapter(adapter);
        //设置飞入动画
        adapter.SetOnSetHolderClickListener(new HomeFragmentAdapter.HolderClickListener() {

            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {

                TakeInShopCarAnim anim = new TakeInShopCarAnim(getActivity(), MainActivity
                        .INSHOPCAR);
                anim.doAnim(drawable, start_location);
            }
        });

        BaiduLocation.setMyLocationListener(this);
        BaiduLocation.getLocation(app);
    }

    @Override
    public void initData() {

    }

    /***
     * 添加goods和machine到数据库 或者 增加商品数量
     * 更新数据库
     */
    @Override
    public void addGoodNumber(final FoodBean.DataBean dataBean) {
        x.task().run(new Runnable() {
            @Override
            public void run() {

                int shopCarInfoId = dataBean.getId();

                ShopCarInfo carInfo;
                MachineInfo machineInfo;
                try {
                    carInfo = app.db.selector(ShopCarInfo.class).where("id", "=", shopCarInfoId).findFirst();

                    //如果数据库里不存在，数量为1,并向数据库添加一项；存在，就取出来+1再存回去
                    int num;
                    if (carInfo == null) {
                        carInfo = new ShopCarInfo();
                        machineInfo = new MachineInfo();
                        machineInfo.setAddress(app.getGroups().get(0).getAddress());
                        machineInfo.setNickname(app.getGroups().get(0).getNickname());
                        machineInfo.setNumber(app.getGroups().get(0).getNumber());
                        machineInfo.setCity(app.getGroups().get(0).getCity());
                        carInfo.setP_machine(app.getGroups().get(0).getNumber());
                        carInfo.setId(dataBean.getId());
                        carInfo.setName(dataBean.getName());
                        carInfo.setPic(dataBean.getPic());
                        carInfo.setPrice_cny(dataBean.getPrice_cny());
                        carInfo.setP_local_number(1);
                        carInfo.setNum(dataBean.getNum());
                        carInfo.setPrice_vr9(dataBean.getPrice_vr9());
                        app.db.saveOrUpdate(machineInfo);
                    } else {
                        num = carInfo.getP_local_number();
                        carInfo.setP_local_number(++num);
                    }
                    //增加商品数量
                    app.db.saveOrUpdate(carInfo);

                    x.task().post(new Runnable() {
                        @Override
                        public void run() {
                            //        改变购物车角标
                            int oldTotalNum = SharedPreferenceUtils.getShopCarNumber();
                            Log.i(TAG, "run: "+ oldTotalNum);
                            int newTotalNum = ++oldTotalNum  ;
                            SharedPreferenceUtils.setShopCarNumber(newTotalNum);
                            ShopCarUtil.ChangeCorner(getActivity(), newTotalNum);
                        }
                    });
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.e(TAG_LOG, "添加失败: " + e.getMessage());
                }
            }
        });
    }

    //    选择xx类型的商品
    @Event(value = R.id.rdo_group, type = RadioGroup.OnCheckedChangeListener.class)
    private void rdoBtn(RadioGroup radioGroup, int i) {
        int type;
        switch (i) {
            case R.id.rb_home:
                type = CHOOSE_NEW_FOOD;
                break;
            case R.id.rb_takefood:
                type = CHOOSE_HOT_FOOD;
                break;
            case R.id.rb_shopcar:
                type = CHOOSE_SET_MEAL;
                break;
            case R.id.rb_mine:
                type = CHOOSE_DRINK;
                break;
            default:
                type = CHOOSE_NEW_FOOD;
        }
        getFoodByMachine(type, machineNum, null);
    }


    /**
     * 点击导航栏
     */
    @Event({R.id.ll_location, R.id.ll_choose_machine})
    private void onTopClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra(MACHINE_LIST, (Serializable) mechinesBeanList);
                startActivity(intent);
                break;
            case R.id.ll_choose_machine:
//                Log.i("onclick", "onClick: ");

                if (mechinesBeanList != null) {
                    initIntoPopwindow(mechinesBeanList);
                }
                break;
        }
    }

    /**
     * 获取 定位后的location
     */
    @Override
    public void myLocation(double mylongitude, double mylatitude, String city, String street) {
//        Log.i(TAG_LOG, "myLocation: " + "\n" + mylatitude + "\n" + mylongitude);
        if (city != null) {
            tv_city.setText(city);
            if (MainActivity.isPermissionRequested) {
                loadMachineList(mylongitude, mylatitude);
            }
        } else {

        }
    }

    /**
     * 获取机器列表
     **/
    private void loadMachineList(double mylongitude, double mylatitude) {
        final RequestParams params = new RequestParams(HgbwUrl.MACHINE_LOCATION_URL);
        final CustomProgressDialog[] dialog = {null};
        //缓存时间
        params.addQueryStringParameter("longitude", String.valueOf(mylongitude));
        params.addQueryStringParameter("latitude", String.valueOf(mylatitude));
        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                http().get(params, new Callback.CacheCallback<String>() {

                    private boolean hasError = false;
                    private String result = null;


                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                        if (result != null) {
                            this.result = result;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_LOG, "getMachineListFromJson onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "getMachineListFromJson onError " + " code: " + responseCode + " message: " +
                                    "" + responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        dialog[0].dismiss();
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "getMachineListFromJson onFinished: " + result);
                            try {
                                getMachineListFromJson(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog[0] = new CustomProgressDialog(getActivity(), "请稍后", R.drawable.fram2);
                        dialog[0].show();
                    }
                });
            }
        });

    }

    private void getMachineListFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        MachineLocationBean machineLocationBean = gson.fromJson(result, MachineLocationBean.class);
        mechinesBeanList = machineLocationBean.getMechines();
        //选择第一个作为默认机器
        mechinesBean = mechinesBeanList.get(0);
        machineNum = mechinesBean.getNumber();
        nickName = mechinesBean.getNickname();
        SharedPreferenceUtils.setMachineNum(machineNum);
        tv_machine_name.setText(nickName);
        MachineInfo machineInfo = mechinesBean;
        List<MachineInfo> machineInfoList = new ArrayList<>();
        machineInfoList.add(machineInfo);
        app.setGroups(machineInfoList);

        getFoodByMachine(CHOOSE_NEW_FOOD, machineNum, null);
        getBannerRequest(machineNum);
    }


    /**
     * 点击标题时的弹窗
     */
    private void initIntoPopwindow(final List<MachineLocationBean.MechinesBean> data) {

        View view = View.inflate(getActivity(), R.layout.popu_machine_list_choose,
                null);
        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                height / 5 * 2);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        window.setFocusable(true);
        window.update();
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // popwindow消失回调
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f; // 0.0-1.0
                getActivity().getWindow().setAttributes(lp);
            }
        });
        window.showAtLocation(ll_choose_machine, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.8f; // 0.0-1.0  半透明
        getActivity().getWindow().setAttributes(lp);

        //添加滚动视图
        wheelView = (WheelView) view.findViewById(R.id.wheelview);
        wheelView.setWheelAdapter(new MyWheelAdapter(getActivity(), data));
        // common皮肤
        wheelView.setSkin(WheelView.Skin.Holo);
        // 数据集合
        wheelView.setWheelData(data);
        // 确定按钮,点击之后跳转
        view.findViewById(R.id.tv_save)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        System.out.println(wheelView.getCurrentPosition());
                        final MachineInfo machineInfo = data.get(wheelView.getCurrentPosition());
                        if (!machineNum.equals(machineInfo.getNumber())) {
                            machineNum = machineInfo.getNumber();

                            final CusAlertDialogWithTwoBtn alertDialog = new CusAlertDialogWithTwoBtn(getActivity());
                            alertDialog.setTitle("警告");
                            alertDialog.setContent("你的购物车会消失");
                            alertDialog.setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getFoodByMachine(CHOOSE_NEW_FOOD, machineNum, machineInfo);
                                    window.dismiss();
                                    alertDialog.dismiss();
                                    //删除购物车
                                    try {
                                        app.db.delete(ShopCarInfo.class);
                                        app.db.delete(MachineInfo.class);
                                        total = 0;
                                        ShopCarUtil.ChangeCorner(getActivity(), total);
                                        SharedPreferenceUtils.setShopCarNumber(total);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                        window.dismiss();
                    }
                });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
    }

    /**
     * 获取食品列表
     */
    public void getFoodByMachine(int type, String machineNum, final MachineInfo machineInfo) {
        final RequestParams params = new RequestParams(HgbwUrl.FOOD_BY_MACHINE_URL);
        final CustomProgressDialog[] dialog = {null};
        //缓存时间
        params.addQueryStringParameter("mechine_number", machineNum);
        switch (type) {
            case CHOOSE_NEW_FOOD:
                params.addQueryStringParameter("order", "id");
                break;
            case CHOOSE_HOT_FOOD:
                params.addQueryStringParameter("order", "sell");
                break;
            case CHOOSE_SET_MEAL:
                params.addQueryStringParameter("kind", "A");
                break;
            case CHOOSE_DRINK:
                params.addQueryStringParameter("kind", "B");
                break;
            default:
                params.addQueryStringParameter("order", "id");
        }
        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().get(params, new Callback.CacheCallback<String>() {

                    private boolean hasError = false;
                    private String result = null;


                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                        if (result != null) {
                            this.result = result;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_LOG, "getFoodByMachine onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "getFoodByMachine onError " + " code: " + responseCode + " message: " +
                                    responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        dialog[0].dismiss();
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "getFoodByMachineFromJson onFinished: " + result);
                            try {
                                //切换机器成功时调用
                                if (machineInfo != null) {
                                    List<MachineInfo> machineInfoList = new ArrayList<MachineInfo>();
                                    machineInfoList.add(machineInfo);
                                    app.setGroups(machineInfoList);
                                    tv_machine_name.setText(machineInfo.getNickname());
                                }
                                getFoodByMachineFromJson(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog[0] = new CustomProgressDialog(getActivity(), "请稍后", R.drawable.fram2);
                        dialog[0].show();
                    }
                });
            }
        });
    }

    public void getFoodByMachineFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        FoodBean foodBean = gson.fromJson(result, FoodBean.class);
        foodBeanDataBeanList = foodBean.getData();
        adapter.setData(foodBeanDataBeanList);

    }

    /**
     * 获取轮播图
     */
    public void getBannerRequest(String machineNum) {
        final RequestParams params = new RequestParams(HgbwUrl.BANNER_URL);
        final CustomProgressDialog[] dialog = {null};
        //缓存时间
        params.addQueryStringParameter("mechine_number", machineNum);

        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().get(params, new Callback.CacheCallback<String>() {

                    private boolean hasError = false;
                    private String result = null;


                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                        if (result != null) {
                            this.result = result;
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_LOG, "getBannerRequest onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "getBannerRequest onError " + " code: " + responseCode + " message: " +
                                    responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        dialog[0].dismiss();
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "getBannerRequest onFinished: " + result);
                            try {
                                getBannerFromJson(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog[0] = new CustomProgressDialog(getActivity(), "请稍后", R.drawable.fram2);
                        dialog[0].show();
                    }
                });
            }
        });
    }

    public void getBannerFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        MachineBannerBean machineBannerBean = gson.fromJson(result, MachineBannerBean.class);
        bannerBeanList = machineBannerBean.getBanner();
        for (int i = 0; i < bannerBeanList.size(); i++) {
//        Log.i(TAG_LOG, "getBannerFromJson: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + bannerBeanList
// .get(i).getUrl());
            MachineBannerBean.BannerBean bannerBean = bannerBeanList.get(i);
            String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL + bannerBean.getPic());
            Log.i(TAG_LOG, "getBannerFromJson imgPath: " + imgPath);
            mImageUrl.add(imgPath);
        }
        binner.setImageResources(mImageUrl, mAdCycleViewListener, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        x.view().inject(this, rootView);
        return rootView;
    }
}
