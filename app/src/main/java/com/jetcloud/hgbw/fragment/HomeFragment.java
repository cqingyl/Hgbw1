package com.jetcloud.hgbw.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.demo.LocationActivity;
import com.jetcloud.hgbw.adapter.HomeFragmentAdapter;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.bean.GoodsInfo;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.ShopCarUtil;
import com.jetcloud.hgbw.utils.TakeInShopCarAnim;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.jetcloud.hgbw.view.ImageCycleView;
import com.jetcloud.hgbw.view.ImageCycleView.ImageCycleViewListener;
import com.jetcloud.hgbw.view.MyListView;

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

import static android.content.ContentValues.TAG;

@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements HomeFragmentAdapter.AddGoodNumberInterface {
    private final static String TAG_LOG = HomeFragment.class.getSimpleName();
    private static HomeFragment homeFragment;
    private ImageCycleView binner;
    private HomeFragmentAdapter adapter;
    private TextView tvTopSearch;
    private RadioGroup radioGroup;
    private CustomProgressDialog progress;
    //////////////暂时无用，先保留
    private static final int NEW_FOOD = 0;
    private static final int HOT_FOOD = 1;
    private static final int SET_MEAL = 2;
    private static final int DRINK = 3;
    int type = NEW_FOOD;
    ////////////
    List<ShopCarInfo> dataA = new ArrayList<>();
    List<ShopCarInfo> dataB = new ArrayList<>();
    List<ShopCarInfo> dataC = new ArrayList<>();
    List<ShopCarInfo> dataD = new ArrayList<>();
    List<ShopCarInfo> dataE = new ArrayList<>();
    private HgbwApplication app;
    private int total;
    private String result = "{ \n" +
            "    \"a\": [ \n" +
            "        { \n" +
            "            \"p_id\": 1, \n" +
            "            \"p_type\": \"a\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"小炒杏鲍菇\", \n" +
            "            \"p_picture\": \"../images/小炒杏鲍菇.png\", \n" +
            "            \"p_price\": 15, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 4, \n" +
            "            \"p_vr9\": 0.075\n" +
            "        }, \n" +
            "        { \n" +
            "            \"p_id\": 7, \n" +
            "            \"p_type\": \"a\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"香菇炖鸡\", \n" +
            "            \"p_picture\": \"../images/香菇炖鸡.png\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 10, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }\n" +
            "    ], \n" +
            "    \"b\": [ \n" +
            "        { \n" +
            "            \"p_id\": 2, \n" +
            "            \"p_type\": \"b\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"番茄炒蛋\", \n" +
            "            \"p_picture\": \"../images/番茄炒蛋.png\", \n" +
            "            \"p_price\": 20, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 1, \n" +
            "            \"p_vr9\": 0.08\n" +
            "        }, \n" +
            "        { \n" +
            "            \"p_id\": 5, \n" +
            "            \"p_type\": \"b\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"酸辣粉\", \n" +
            "            \"p_picture\": \"../images/酸辣粉.jpg\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }\n" +
            "    ], \n" +
            "    \"c\": [ \n" +
            "        { \n" +
            "            \"p_id\": 3, \n" +
            "            \"p_type\": \"c\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"红烧肉\", \n" +
            "            \"p_picture\": \"../images/红烧肉.png\", \n" +
            "            \"p_price\": 30, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": -7, \n" +
            "            \"p_vr9\": 0.15\n" +
            "        }\n" +
            "    ], \n" +
            "    \"d\": [ \n" +
            "        { \n" +
            "            \"p_id\": 4, \n" +
            "            \"p_type\": \"d\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"红烧豆腐\", \n" +
            "            \"p_picture\": \"../images/红烧豆腐.jpg\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }, \n" +
            "        { \n" +
            "            \"p_id\": 8, \n" +
            "            \"p_type\": \"d\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"香辣翅根\", \n" +
            "            \"p_picture\": \"../images/香辣翅根.png\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }\n" +
            "    ], \n" +
            "    \"e\": [ \n" +
            "        { \n" +
            "            \"p_id\": 6, \n" +
            "            \"p_type\": \"e\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"香芹肉丝\", \n" +
            "            \"p_picture\": \"../images/香芹肉丝.jpg\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }, \n" +
            "        { \n" +
            "            \"p_id\": 11, \n" +
            "            \"p_type\": \"e\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"麻辣香锅\", \n" +
            "            \"p_picture\": \"../images/麻辣香锅.jpg\", \n" +
            "            \"p_price\": 25, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.09\n" +
            "        }, \n" +
            "        { \n" +
            "            \"p_id\": 13, \n" +
            "            \"p_type\": \"e\", \n" +
            "            \"p_machine\": \"hg010100001\", \n" +
            "            \"p_name\": \"地三鲜\", \n" +
            "            \"p_picture\": \"../images/地三鲜.jpg\", \n" +
            "            \"p_price\": 12, \n" +
            "            \"p_address\": \"成都市武侯区武科东二路\", \n" +
            "            \"p_number\": 5, \n" +
            "            \"p_vr9\": 0.06\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";
    //轮播图点击事件
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO Auto-generated method stub

        }
    };

    // binner图数据
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
        Log.i(TAG, "initView: " +    System.currentTimeMillis());

        app = (HgbwApplication) getActivity().getApplication();
        binner = getView(R.id.binner);
        listView = getView(R.id.lv_home);
        adapter = new HomeFragmentAdapter(getActivity(), dataA);
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
    }

    /**
     * 处理json数据
     */
    private void getDataFromJson(String result) throws JSONException {
        Gson gson = new Gson();
        GoodsInfo goodsInfo = gson.fromJson(result, GoodsInfo.class);

        dataA = new ArrayList<>();
        dataA.addAll(goodsInfo.getA());
        dataB = new ArrayList<>();
        dataB.addAll(goodsInfo.getB());
        dataC = new ArrayList<>();
        dataC.addAll(goodsInfo.getC());
        dataD = new ArrayList<>();
        dataD.addAll(goodsInfo.getD());
        dataE = new ArrayList<>();
        dataE.addAll(goodsInfo.getE());
        adapter.notifyDataSetChanged();
        //初始化轮播图
        loadAdData();
    }


    private void getNetData() {
        final RequestParams params = new RequestParams(HgbwUrl.HOME_DATA_URL);
        //缓存时间
        params.addQueryStringParameter("myR_lng", "104.06792346");
        params.addQueryStringParameter("myR_lat", "30.67994285");
        params.addQueryStringParameter("type", "mobile");
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
                        Log.e(TAG_LOG, "onError: " + ex.getMessage());
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e(TAG_LOG, "onError " + " code: " + responseCode + " message: " + responseMsg);
                        } else { // 其他错误
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        progress.dismiss();
                        if (!hasError && result != null) {
//                        Log.i(TAG_LOG, "onFinished: " + result);
                            try {
                                getDataFromJson(result);
                                adapter.setData(dataA);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG_LOG, " json error: " + e.getMessage());
                            }
                        }
                    }

                });
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        progress = new CustomProgressDialog(getActivity(), "请稍后", R.drawable.fram2);
                        progress.show();
                    }
                });
            }
        });

    }

    /***
     * 添加goods和machine到数据库 或者 增加商品数量
     * 更新数据库
     */
    @Override
    public void addGoodNumber(final ShopCarInfo shopCarInfo) {
        x.task().run(new Runnable() {
            @Override
            public void run() {

                int shopCarInfoId = shopCarInfo.getP_id();

                ShopCarInfo carInfo = new ShopCarInfo();
                MachineInfo machineInfo = new MachineInfo();

                try {
                    carInfo = app.db.selector(ShopCarInfo.class).where("p_id", "=", shopCarInfoId).findFirst();

                    //如果数据库里不存在，数量为1,并向数据库添加一项；存在，就取出来+1再存回去
                    int num;
                    if (carInfo == null) {
                        machineInfo.setId(shopCarInfo.getP_machine());
                        carInfo = new ShopCarInfo();
                        carInfo.setP_id(shopCarInfo.getP_id());
                        carInfo.setP_type(shopCarInfo.getP_type());
                        carInfo.setP_machine(shopCarInfo.getP_machine());
                        carInfo.setP_name(shopCarInfo.getP_name());
                        carInfo.setP_picture(shopCarInfo.getP_picture());
                        carInfo.setP_price(shopCarInfo.getP_price());
                        carInfo.setP_address(shopCarInfo.getP_address());
                        carInfo.setP_local_number(1);
                        carInfo.setP_number(shopCarInfo.getP_number());
                        carInfo.setP_vr9(shopCarInfo.getP_vr9());
                        app.db.saveOrUpdate(machineInfo);
                    } else {
                        num = carInfo.getP_local_number();
                        carInfo.setP_local_number(++num);
                    }
                    //增加商品数量
                    app.db.saveOrUpdate(carInfo);
//                    List<ShopCarInfo> shopCarInfos = app.db.selector(ShopCarInfo.class).findAll();
//                    List<MachineInfo> machine = app.db.selector(MachineInfo.class).findAll();
//                    Log.i(TAG, "shopCarInfos size: " + shopCarInfos.size() + " machine size: " + machine.size());
//                    Log.i(TAG, "shopCarInfos first object local num: " + shopCarInfos.get(0).getP_local_number());
//                    Log.i(TAG, "machine first object id: "+ m.get(0).getId());

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
                    Log.e(TAG, "添加失败: " + e.getMessage());
                }
            }
        });
    }

    //    选择xx类型的商品
    @Event(value = R.id.rdo_group, type = RadioGroup.OnCheckedChangeListener.class)
    private void rdoBtn(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_home:
                adapter.setData(dataA);
                type = NEW_FOOD;
                break;
            case R.id.rb_takefood:
                adapter.setData(dataB);
                type = HOT_FOOD;
                break;
            case R.id.rb_shopcar:
                adapter.setData(dataC);
                type = SET_MEAL;
                break;
            case R.id.rb_mine:
                adapter.setData(dataD);
                type = DRINK;
                break;
        }
    }

    @Override
    public void initData() {
        getNetData();
    }

    /**
     * 加载轮播数据
     * **/
    private void loadAdData() {

        for (int i = 0; i < dataE.size(); i++) {
//            String imgPath = dataE.get(i).getP_picture();
//            mImageUrl.add(HgbwUrl.BASE_URL + imgPath);

            String imgPath = ImageLoaderCfg.toBrowserCode(HgbwUrl.BASE_URL + dataE.get(i).getP_picture());
            Log.i(TAG, "loadAdData: " + imgPath);
            mImageUrl.add(imgPath);
        }
        binner.setImageResources(mImageUrl, mAdCycleViewListener, 0);

    }

    /**
     * 点击导航栏
     */
    @Event(R.id.tv_top_search)
    private void onTopClick(View view) {
        switch (view.getId()) {
            case R.id.tv_top_search:
                Log.i("onclick", "onClick: ");
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("from", 0);
                startActivity(intent);
                break;
        }
    }

}
