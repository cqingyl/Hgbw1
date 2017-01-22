package com.jetcloud.hgbw.baidumap;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLauchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.LoadingActivity;
import com.jetcloud.hgbw.bean.MachineLocationBean;
import com.jetcloud.hgbw.utils.Out;

import java.util.ArrayList;
import java.util.List;

import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE_LIST;


public class LocationActivity extends Activity {

    private final static String TAG_LOG = LoadingActivity.class.getSimpleName();
    // 定位相关
    private LocationClient mLocClient;
    private MyBroadcastReceiver receiver;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;//方向
    private List<MachineLocationBean.MechinesBean> mechinesBeanList;//含有经纬度
    public MyLocationListenner myListener = new MyLocationListenner();
    private Marker[] mMarkers;
    private BikeNavigateHelper mNaviHelper;//步行导航
    BikeNaviLauchParam param;
    private LatLng myLatLng; //我的位置
    private static boolean isPermissionRequested = false;
    View view;
    InfoWindow infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //注册广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        // 网络错误
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        // 效验key失败
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(receiver, filter);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        init();
        getData();
        if (mechinesBeanList != null) {
            initOverlay();
        }
        /**
         * 点击标记物
         * */
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Log.i("log", "onMarkerClick: ");

                if (myLatLng != null) {
                    initBikeNavigateHelper(myLatLng, marker.getPosition());

//                    LinearLayout linearLayout = new LinearLayout(LocationActivity.this);
//                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
// .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    linearLayout.setBackgroundResource(R.drawable.mark_popu);
//                    Button button = new Button(LocationActivity.this);
//                    button.setBackgroundResource(R.drawable.bike_nav_bg);
//                    button.setText("更改位置");
//                    button.setTextColor(0x0000f);
//                    button.setWidth(300);
//                    linearLayout.addView(button);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Out.Toast(LocationActivity.this, marker.getPosition().toString());
//                            startBikeNavi();
//                        }
//                    });
//                    infoWindow = new InfoWindow(button, marker.getPosition(), -70);
                    view = View.inflate(LocationActivity.this, R.layout.view_nav, null);
                    view.findViewById(R.id.ib_go_to_navi).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Out.Toast(LocationActivity.this, marker.getPosition().toString());
                            startBikeNavi();
                        }
                    });
                    TextView tv_nick = (TextView) view.findViewById(R.id.tv_nick);
                    for (int i = 0; i < mMarkers.length; i ++) {
                        if (mMarkers[i] == marker)
                            tv_nick.setText(mechinesBeanList.get(i).getNickname());
                    }

                    infoWindow = new InfoWindow(view, marker.getPosition(), -70);
                    mBaiduMap.showInfoWindow(infoWindow);

                }

                return true;
            }
        });

        //地图渲染完成时回调
        mBaiduMap.setOnMapRenderCallbadk(new BaiduMap.OnMapRenderCallback() {
            @Override
            public void onMapRenderFinished() {
                drawMark();
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void initBikeNavigateHelper(LatLng startPt, LatLng endPt) {
        mNaviHelper = BikeNavigateHelper.getInstance();
        param = new BikeNaviLauchParam().stPt(startPt).endPt(endPt);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MACHINE_LIST)) {
            mechinesBeanList = (List<MachineLocationBean.MechinesBean>) intent.getSerializableExtra(MACHINE_LIST);
        }
    }

    private void init() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        myOrientationListener.start();
        //描述地图将要发生的变化，使用工厂类MapStatusUpdateFactory创建，设置级别
        //为18，进去就是18了，默认是12
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setMaxAndMinZoomLevel(3.0f, 18.0f);
        //是否显示缩放按钮
        //mMapView.showZoomControls(false);
        /***
         * 隐藏百度地图logo
         * */
        mMapView.removeViewAt(1);
        //显示指南针
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        //显示位置
        mBaiduMap.setCompassPosition(new Point(-1, -1));
    }

    BitmapDescriptor bitmap;

    // 绘制mark覆盖物
    private void drawMark() {


    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //销毁广播
        unregisterReceiver(receiver);


        // 退出时销毁定位
        mLocClient.stop();
        myOrientationListener.stop();
        mLocClient.unRegisterLocationListener(myListener);
        // 关闭定位图层
        mBaiduMap.clear();
        mMapView.onDestroy();
        super.onDestroy();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        //实现一个广播
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 网络错误
            if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(LocationActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                // key效验失败
            } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(LocationActivity.this, "百度地图key效验失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentX).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            // 设置自定义图标  
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.mylocation);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                    true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            if (isFirstLoc) {
                isFirstLoc = false;
                myLatLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(myLatLng).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    public void initOverlay() {
        // add marker overlay
        LatLng[] latLngs = new LatLng[mechinesBeanList.size()];
        mMarkers = new Marker[mechinesBeanList.size()];
        for (int i = 0; i < mechinesBeanList.size(); i++) {
            latLngs[i] = new LatLng(Double.parseDouble(mechinesBeanList.get(i).getLatitude()), Double.parseDouble
                    (mechinesBeanList.get(i).getLongitude()));
            Log.i(TAG_LOG, "latLngs: " + i + "\nlatitude: " + latLngs[i].latitude + " \nlongitude" + latLngs[i]
                    .longitude);
            MarkerOptions ooA = new MarkerOptions().position(latLngs[i]).icon(bd)
                    .zIndex(9).draggable(true);
            // 掉下动画
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            mMarkers[i] = (Marker) (mBaiduMap.addOverlay(ooA));
        }
    }

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.mark);

    /**
     * 开始骑行导航
     */
    private void startBikeNavi() {
        Log.d("View", "startBikeNavi");
        mNaviHelper.initNaviEngine(this, new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                Log.d("View", "engineInitSuccess");
                routePlanWithParam();
            }

            @Override
            public void engineInitFail() {
                Log.d("View", "engineInitFail");
            }
        });
    }

    private void routePlanWithParam() {
        mNaviHelper.routePlanWithParams(param, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("View", "onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(LocationActivity.this, BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d("View", "onRoutePlanFail");
            }

        });
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }

}