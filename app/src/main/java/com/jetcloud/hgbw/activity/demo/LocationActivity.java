package com.jetcloud.hgbw.activity.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jetcloud.hgbw.R;

public class LocationActivity extends Activity {

	// 定位相关
	LocationClient mLocClient;
	private MyBroadcastReceiver receiver;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	boolean isFirstLoc = true; // 是否首次定位
	public MyLocationListenner myListener = new MyLocationListenner();

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

		//地图渲染完成时回调
		mBaiduMap.setOnMapRenderCallbadk(new BaiduMap.OnMapRenderCallback() {
			@Override
			public void onMapRenderFinished() {
				drawMark();
			}
		});

	}

	private void init() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		//描述地图将要发生的变化，使用工厂类MapStatusUpdateFactory创建，设置级别
		//为18，进去就是18了，默认是12
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.setMapStatus(mapStatusUpdate);
		//是否显示缩放按钮
		//mMapView.showZoomControls(false);
		//显示指南针
		mBaiduMap.getUiSettings().setCompassEnabled(true);
		//显示位置
		mBaiduMap.setCompassPosition(new Point(-1, -1));
	}

	BitmapDescriptor bitmap;

	// 绘制mark覆盖物
	private void drawMark() {
		MarkerOptions markerOptions = new MarkerOptions();
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka); // 描述图片
		markerOptions.position(new LatLng(30.5513090000, 104.0749530000)) // 设置位置
				.icon(bitmap) // 加载图片
				.draggable(false); // 不支持拖拽
		//把绘制的圆添加到百度地图上去
		mBaiduMap.addOverlay(markerOptions);
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
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}



}