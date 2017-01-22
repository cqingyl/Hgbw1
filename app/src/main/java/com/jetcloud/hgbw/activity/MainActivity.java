package com.jetcloud.hgbw.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.fragment.HomeFragment;
import com.jetcloud.hgbw.fragment.MineFragment;
import com.jetcloud.hgbw.fragment.ShopCarFragment;
import com.jetcloud.hgbw.fragment.TakeFoodFragment;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.view.CusAlertDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private LinearLayout home, takefood, mine, shopcar;
    public static ImageView INSHOPCAR;
    private TextView hometxt, takefoodtxt, shopcattxt, minetxt, tvCorner;
    private ImageView homeimg, takefoodimg, shopimg, mineimg;
    public static RelativeLayout main_bg;
    public static MainActivity mainActivity;

    /* 请求码 */
    public static final int PAY_SUCCESS_REQUEST = 0;

    /**
     * 当前选中导航
     */
    private int currentSelect;
    /**
     * 退出标记
     */
    private boolean isExit;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() != KeyEvent.ACTION_UP) {
            int sum = manager.getBackStackEntryCount();
            if (sum <= 0) {
                if (!isExit) {
                    Out.Toast(this, "再按一次返回键退出程序 ~");
                    isExit = true;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = false;
                        }
                    }, 2 * 1000);
                } else {
                    application.removeAll();
                    System.exit(0);
                }
                return true;
            } else if (sum == 1) {
                currentFragment = firstFragment;
                manager.beginTransaction().show(currentFragment)
                        .commitAllowingStateLoss();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        mainActivity = this;
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        initWindow();
        requestPermission();
        home = getViewWithClick(R.id.home);
        takefood = getViewWithClick(R.id.takefood);
        shopcar = getViewWithClick(R.id.shopcar);
        mine = getViewWithClick(R.id.mine);
        hometxt = getView(R.id.hometxt);
        takefoodtxt = getView(R.id.takefoodtxt);
        shopcattxt = getView(R.id.shopcartxt);
        minetxt = getView(R.id.minetxt);
        homeimg = getView(R.id.homeimg);
        takefoodimg = getView(R.id.takefoodimg);
        shopimg = getView(R.id.shopcarimg);
        mineimg = getView(R.id.mineimg);
        INSHOPCAR = getView(R.id.shopcarimg);
        tvCorner = getView(R.id.tv_corner);
        main_bg = getView(R.id.main_bg);

        MainActivity.main_bg.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {
        //如果来自 支付成功
        String jumpResource = getIntent().getStringExtra(HgbwStaticString.JUMP_RESOURCE);
        if (jumpResource == null) {
            // 初始化首个fragment
            if (HgbwApplication.getHomeFragment() == null) {
                HgbwApplication.setHomeFragment(HomeFragment.newInstance());
            }
            manager.beginTransaction()
                    .add(R.id.fl_content, HgbwApplication.getHomeFragment())
                    .commit();
            currentFragment = HgbwApplication.getHomeFragment();
            currentSelect = 1;


        } else if (jumpResource.equals(PayNextActivity.class.getSimpleName())) {
            if (HgbwApplication.getTakeFoodFragment() == null) {
                HgbwApplication.setTakeFoodFragment(TakeFoodFragment.newInstance());
            }
            manager.beginTransaction()
                    .add(R.id.fl_content, HgbwApplication.getTakeFoodFragment())
                    .commit();
            currentFragment = HgbwApplication.getTakeFoodFragment();
            currentSelect = 1;
            changeCurrent(2);
        }


    }

    private static final String SCHEME = "package";
    private static boolean isPermissionRequested = false;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//					permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//					Out.Toast(mainActivity, "请打开定位权限");

                PackageManager pm = mainActivity.getPackageManager();
                CusAlertDialog alert;
//						Out.Toast(mainActivity, "没有这个权限");
                alert = new CusAlertDialog(mainActivity);
                alert.setTitle("操作提示");
                alert.setContent("请打开权限后再尝试");

                alert.setPositiveButton("去开启",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts(SCHEME, getApplication().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                finish();
                            }
                        });
                alert.show();
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }

    }

    @Override
    public void onClick(View view) {
        // 点击事件
        if (view == home) {
            if (HgbwApplication.getHomeFragment() == null) {
                HgbwApplication.setHomeFragment(HomeFragment.newInstance());
            }
            switchContent(HgbwApplication.getHomeFragment(), R.id.fl_content);
            changeCurrent(1);
        } else if (view == takefood) {
            if (HgbwApplication.getTakeFoodFragment() == null) {
                HgbwApplication.setTakeFoodFragment(TakeFoodFragment.newInstance());

            }

            switchContent(HgbwApplication.getTakeFoodFragment(), R.id.fl_content);
            changeCurrent(2);
        } else if (view == shopcar) {
            if (HgbwApplication.getCarFragment() == null) {
                HgbwApplication.setCarFragment(ShopCarFragment.newInstance());
            }
            switchContent(HgbwApplication.getCarFragment(), R.id.fl_content);
            changeCurrent(3);
        } else if (view == mine) {
            if (HgbwApplication.getMineFragment() == null) {
                HgbwApplication.setMineFragment(MineFragment.newInstance());
            }
            switchContent(HgbwApplication.getMineFragment(), R.id.fl_content);
            changeCurrent(4);
        }
    }


    /**
     * 替换
     *
     * @param select
     */
    @SuppressWarnings("deprecation")
    public void changeCurrent(int select) {
        if (currentSelect != select) {

            switch (currentSelect) {
                case 1:
                    home.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    hometxt.setTextColor(getResources().getColor(R.color.bottom_nav_text_color));
                    homeimg.setImageResource(R.drawable.home);
                    break;
                case 2:
                    takefood.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    takefoodtxt.setTextColor(getResources().getColor(R.color.bottom_nav_text_color));
                    takefoodimg.setImageResource(R.drawable.takefood);
                    break;
                case 3:
                    shopcar.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    shopcattxt.setTextColor(getResources().getColor(R.color.bottom_nav_text_color));
                    shopimg.setImageResource(R.drawable.shop);
                    break;
                case 4:
                    mine.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    minetxt.setTextColor(getResources().getColor(R.color.bottom_nav_text_color));
                    mineimg.setImageResource(R.drawable.mine);
                    break;
            }

            currentSelect = select;

            switch (currentSelect) {
                case 1:
                    home.setBackgroundColor(getResources().getColor(
                            R.color.main_bg));
                    hometxt.setTextColor(getResources().getColor(
                            R.color.bottom_nav_text_color_check));
                    homeimg.setImageResource(R.drawable.homec);
                    break;
                case 2:
                    takefood.setBackgroundColor(getResources().getColor(
                            R.color.main_bg));
                    takefoodtxt.setTextColor(getResources().getColor(
                            R.color.bottom_nav_text_color_check));
                    takefoodimg.setImageResource(R.drawable.takefoodc);
                    break;
                case 3:
                    shopcar.setBackgroundColor(getResources().getColor(
                            R.color.main_bg));
                    shopcattxt.setTextColor(getResources().getColor(
                            R.color.bottom_nav_text_color_check));
                    shopimg.setImageResource(R.drawable.shopc);
                    break;
                case 4:
                    mine.setBackgroundColor(getResources().getColor(
                            R.color.main_bg));
                    minetxt.setTextColor(getResources().getColor(
                            R.color.bottom_nav_text_color_check));
                    mineimg.setImageResource(R.drawable.minec);
                    break;
            }
        }
    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
