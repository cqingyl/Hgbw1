package com.jetcloud.hgbw.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.demo.BaiduLocation;
import com.jetcloud.hgbw.activity.demo.LocationActivity;
import com.jetcloud.hgbw.adapter.HomeFragmentAdapter;
import com.jetcloud.hgbw.adapter.MyWheelAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.FoodBean;
import com.jetcloud.hgbw.bean.MachineBannerBean;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.MachineLocationBean;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.ShopCarUtil;
import com.jetcloud.hgbw.utils.TakeInShopCarAnim;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.ImageCycleView;
import com.jetcloud.hgbw.view.ImageCycleView.ImageCycleViewListener;
import com.jetcloud.hgbw.view.MyListView;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static org.xutils.x.http;


@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements HomeFragmentAdapter.AddGoodNumberInterface, BaiduLocation.MyLocationListener {
    private final static String TAG_LOG = HomeFragment.class.getSimpleName();
    private static HomeFragment homeFragment;
    private ImageCycleView binner;
    private HomeFragmentAdapter adapter;
    private TextView tvTopSearch;
    private TextView tv_city;
    private RadioGroup radioGroup;
    private CustomProgressDialog progress;
    private ScrollView sv_all_layout;
    private WheelView wheelView;
    private PopupWindow window;
    String machineNum;
    String nickName;
    List<MachineLocationBean.MechinesBean> mechinesBeanList;
    List<FoodBean.DataBean> foodBeanDataBeanList;
    MachineLocationBean.MechinesBean mechinesBean;
    private static final int CHOOSE_NEW_FOOD = 0;
    private static final int CHOOSE_HOT_FOOD = 1;
    private static final int CHOOSE_SET_MEAL = 2;
    private static final int CHOOSE_DRINK = 3;
    private LinearLayout ll_choose_machine;
    private TextView tv_machine_name;

    private HgbwApplication app;
    private int total;

    //轮播图点击事件
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO Auto-generated method stub

        }
    };

    // banner图数据
    private ArrayList<String> mImageUrl = new ArrayList<String>();

    private MyListView listView;

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
//        Log.i(TAG, "H onHiddenChanged: " + hidden);
        if (hidden){
            SharedPreferenceUtils.setShopCarNumber(total);
//            Log.i(TAG, "H onHiddenChanged: " + total);
        } else {
            //购物车角标
            total = SharedPreferenceUtils.getShopCarNumber();
            ShopCarUtil.ChangeCorner(getActivity(),total);
//            Log.i(TAG, "H onHiddenChanged: " + total);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {


        total = SharedPreferenceUtils.getShopCarNumber();
        //购物车角标
        ShopCarUtil.ChangeCorner(getActivity(),total);
//        Log.i(TAG, "onResume: " + total);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferenceUtils.setShopCarNumber(total);
        super.onPause();
    }


    @Override
    protected void initView() {
        PackageManager pm = getActivity().getPackageManager();
        AlertDialog alert;
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.RECORD_AUDIO", getActivity().getPackageName()));
        // 判断 存储的machineName 是不是默认的值，如果是说明是未定位
        if (!permission) {
            Out.Toast(getActivity(),"没有这个权限");
            alert = new AlertDialog.Builder(getActivity()).create();
            alert.setTitle("操作提示");
            alert.setMessage("请打开GPS后再尝试");
            alert.getWindow().setBackgroundDrawableResource(R.color.white);

            alert.setButton(DialogInterface.BUTTON_POSITIVE, "去开启",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            alert.show();
        }

        app = (HgbwApplication) getActivity().getApplication();
        binner = getView(R.id.binner);
        tv_city = getView(R.id.tv_city);
        listView = getView(R.id.lv_home);
        ll_choose_machine = getView(R.id.ll_choose_machine);
        tv_machine_name = getView(R.id.tv_machine_name);

        adapter = new HomeFragmentAdapter(getActivity());
        adapter.setNumberInterface(HomeFragment.this);
        listView.setAdapter(adapter);
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
                            ShopCarUtil.ChangeCorner(getActivity(), ++total);
//                            SharedPreferenceUtils.setShopCarNumber(total);
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

    @Override
    public void initData() {

    }


    /**
     * 点击导航栏
     */
    @Event({R.id.ll_location, R.id.ll_choose_machine})
    private void onTopClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                Intent intent = new Intent(getActivity(), LocationActivity.class);
//                intent.putExtra("from", 0);
                startActivity(intent);
                break;
            case R.id.ll_choose_machine :
//                Log.i("onclick", "onClick: ");

                if (mechinesBeanList != null) {
                    initIntoPopwindow(mechinesBeanList);
                }
                break;
        }
    }

    /**
     *  获取location
     * */
    @Override
    public void myLocation(double mylongitude, double mylatitude, String city, String street) {
//        Log.i(TAG_LOG, "myLocation: " + mylatitude);
        if (city != null){
            tv_city.setText(city);
            loadMachineList(mylongitude, mylatitude);
        } else {

        }
    }

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
                            Log.e(TAG_LOG, "getMachineListFromJson onError " + " code: " + responseCode + " message: " + responseMsg);
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
     * */
    private void initIntoPopwindow(final List<MachineLocationBean.MechinesBean> data) {

        View view = View.inflate(getActivity(), R.layout.popu_machine_list_choose,
                null);
        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                height /5*2);
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
                        MachineInfo machineInfo = data.get(wheelView.getCurrentPosition());
                        machineNum = machineInfo.getNumber();
                        getFoodByMachine(CHOOSE_NEW_FOOD, machineNum, machineInfo);
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
     * */
    public void getFoodByMachine(int type, String machineNum, final MachineInfo machineInfo){
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
                            Log.e(TAG_LOG, "getFoodByMachine onError " + " code: " + responseCode + " message: " + responseMsg);
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
                                if (machineInfo != null){
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
    public void getFoodByMachineFromJson (String result) throws JSONException{
        Gson gson = new Gson();
        FoodBean foodBean = gson.fromJson(result, FoodBean.class);
        foodBeanDataBeanList = foodBean.getData();
        adapter.setData(foodBeanDataBeanList);

    }
   /**
     * 获取轮播图
     * */
    public void getBannerRequest(String machineNum){
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
                            Log.e(TAG_LOG, "getBannerRequest onError " + " code: " + responseCode + " message: " + responseMsg);
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
    public void getBannerFromJson (String result) throws JSONException{
        Gson gson = new Gson();
        MachineBannerBean machineBannerBean = gson.fromJson(result, MachineBannerBean.class);
        List<MachineBannerBean.BannerBean> bannerBeanList = machineBannerBean.getBanner();
        for (int i = 0; i < bannerBeanList.size(); i ++) {
            MachineBannerBean.BannerBean bannerBean = bannerBeanList.get(i);
            String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.HOME_URL +  bannerBean.getPic());
            Log.i(TAG_LOG, "getBannerFromJson imgPath: " + imgPath);
            mImageUrl.add(imgPath);
        }
        binner.setImageResources(mImageUrl, mAdCycleViewListener, 0);
    }

}
