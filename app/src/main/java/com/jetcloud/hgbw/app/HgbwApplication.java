package com.jetcloud.hgbw.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.jetcloud.hgbw.bean.MachineInfo;
import com.jetcloud.hgbw.bean.ShopCarInfo;
import com.jetcloud.hgbw.fragment.HomeFragment;
import com.jetcloud.hgbw.fragment.MineFragment;
import com.jetcloud.hgbw.fragment.ShopCarFragment;
import com.jetcloud.hgbw.fragment.TakeFoodFragment;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.utils.XUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class HgbwApplication extends Application{
	private static Context context;
	private LinkedList<Activity> activityList;
	private static HomeFragment homeFragment;
	private static TakeFoodFragment takeFoodFragment;
	private static ShopCarFragment carFragment;
	private static MineFragment mineFragment;
	public DbManager.DaoConfig daoConfig;
	public DbManager db;

	public List<MachineInfo> groups;//被选中的机器
	public Map<String, List<ShopCarInfo>> children;//被选中的商品
	public double totalPrice;//被选中的商品的总人民币价格
	public double totalGcb;//被选中的商品总gcb价格
	public String type;//被选中的商品的支付类型
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		activityList = new LinkedList<Activity>();
		context = getApplicationContext();
		//初始化imageloader
		initImageLoader(context);
		//初始化xutils3
		x.Ext.init(this);
		daoConfig = XUtil.getDaoConfig();
		db = x.getDb(daoConfig);
		//初始化SharePreference
		SharedPreferenceUtils.initData(getApplicationContext());
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public static Context getContext() {
		return context;
	}

	public void removeAll() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
	public static HomeFragment getHomeFragment() {
		return homeFragment;
	}

	public static void setHomeFragment(HomeFragment homeFragment) {
		HgbwApplication.homeFragment = homeFragment;
	}

	public static TakeFoodFragment getTakeFoodFragment() {
		return takeFoodFragment;
	}

	public static void setTakeFoodFragment(TakeFoodFragment takeFoodFragment) {
		HgbwApplication.takeFoodFragment = takeFoodFragment;
	}

	public static ShopCarFragment getCarFragment() {
		return carFragment;
	}

	public static void setCarFragment(ShopCarFragment carFragment) {
		HgbwApplication.carFragment = carFragment;
	}

	public static MineFragment getMineFragment() {
		return mineFragment;
	}

	public static void setMineFragment(MineFragment mineFragment) {
		HgbwApplication.mineFragment = mineFragment;
	}

	public Map<String, List<ShopCarInfo>> getChildren() {
		return children;
	}

	public void setChildren(Map<String, List<ShopCarInfo>> children) {
		this.children = children;
	}

	public List<MachineInfo> getGroups() {
		return groups;
	}

	public void setGroups(List<MachineInfo> groups) {
		this.groups = groups;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getTotalGcb() {
		return totalGcb;
	}

	public void setTotalGcb(double totalGcb) {
		this.totalGcb = totalGcb;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	//配置imageloader属性
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		config.diskCache(new UnlimitedDiskCache(getDiskCacheDir("img-load")));// 缓存位置
		// Initialize ImageLoader with configuration.

		ImageLoader.getInstance().init(config.build());
	}
	//设置ImageLoader缓存位置
	@SuppressLint("NewApi")
	private File getDiskCacheDir(String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		System.out.println("========imageload:"+cachePath + File.separator + uniqueName);
		return new File(cachePath + File.separator + uniqueName);
	}
}
