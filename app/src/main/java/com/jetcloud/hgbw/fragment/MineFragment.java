package com.jetcloud.hgbw.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.activity.EditAccountActivity;
import com.jetcloud.hgbw.activity.LoginActivity;
import com.jetcloud.hgbw.activity.MainActivity;
import com.jetcloud.hgbw.activity.MyOrderActivity;
import com.jetcloud.hgbw.activity.MyTicketActivity;
import com.jetcloud.hgbw.activity.MyWalletActivity;
import com.jetcloud.hgbw.activity.RegisterActivity;
import com.jetcloud.hgbw.app.HgbwUrl;
import com.jetcloud.hgbw.utils.ImageLoaderCfg;
import com.jetcloud.hgbw.utils.ImagePath;
import com.jetcloud.hgbw.utils.Out;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CircleImageView;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;



@SuppressLint("ResourceAsColor")
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {
    private final static String TAG_LOG = MineFragment.class.getSimpleName();
    private static MineFragment mineFragment;
    @ViewInject(R.id.ll_login_layout)
    LinearLayout ll_login_layout;
    @ViewInject(R.id.tv_login)
    TextView tv_login;
    @ViewInject(R.id.tv_register)
    TextView tv_register;
    @ViewInject(R.id.ll_logout_layout)
    LinearLayout ll_logout_layout;
    @ViewInject(R.id.takefood)
    LinearLayout takefood;
    @ViewInject(R.id.myorder)
    LinearLayout myorder;
    @ViewInject(R.id.myticket)
    LinearLayout myticket;
    @ViewInject(R.id.mypoint)
    LinearLayout mypoint;
    @ViewInject(R.id.mymoney)
    LinearLayout mymoney;
    @ViewInject(R.id.aboutus)
    LinearLayout aboutus;
    @ViewInject(R.id.civ_head)
    CircleImageView civ_head;
    @ViewInject(R.id.tv_nick)
    TextView tv_nick;
    @ViewInject(R.id.ll_logout)
    LinearLayout ll_logout;

    boolean isFirst = true;
    private AlertDialog dialog2;
    private CustomProgressDialog progress;
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int SELECT_PIC_KITKAT = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = SharedPreferenceUtils.getIdentity();

    public static MineFragment newInstance() {

        if (mineFragment == null) {
            mineFragment = new MineFragment();
        }
        return mineFragment;
    }

    @Override
    public View initRootView(LayoutInflater inflater, ViewGroup container) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void initView() {
//        SharedPreferenceUtils.setIdentity(SharedPreferenceUtils.WITHOUT_LOGIN);
        // TODO Auto-generated method stub
        MainActivity.main_bg.setBackgroundResource(R.drawable.mine_bg);
        initWindow();
        takefood.setOnClickListener(this);
        myorder.setOnClickListener(this);
        myticket.setOnClickListener(this);
        mypoint.setOnClickListener(this);
        mymoney.setOnClickListener(this);
        aboutus.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        civ_head.setOnClickListener(this);
        tv_nick.setOnClickListener(this);
        ll_logout.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        isFirst = false;
        if (!isHideBtnResiterAndLogin())
            getPicAndTitle();
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG_LOG, "H onHiddenChanged: " + hidden);
        if (!hidden && !isFirst) {
            if (!isHideBtnResiterAndLogin())
            getPicAndTitle();
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * 显示“登录/注册”与否
     * */
    public boolean isHideBtnResiterAndLogin () {
        if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)) {
            ll_login_layout.setVisibility(View.GONE);
            ll_logout_layout.setVisibility(View.VISIBLE);
            ll_logout.setVisibility(View.GONE);
            return true;
        } else {
            ll_login_layout.setVisibility(View.VISIBLE);
            ll_logout_layout.setVisibility(View.GONE);
            ll_logout.setVisibility(View.VISIBLE);
            return false;
        }
    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        super.onClick(view);
        if (view == takefood) {
            Out.Toast(getActivity(), "订餐须知");
        } else if (view == myorder) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
            }
        } else if (view == myticket) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyTicketActivity.class));

            }
        } else if (view == mypoint) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                Out.Toast(getActivity(), "我的卡积分");
            }
        } else if (view == mymoney) {
            if (SharedPreferenceUtils.getIdentity().equals(SharedPreferenceUtils.WITHOUT_LOGIN)){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
            }
        } else if (view == aboutus) {
            Out.Toast(getActivity(), "关于我们");
        } else if (view == tv_login) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (view == tv_register) {
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        } else if (view == ll_logout) {
            getLogout();
//            startActivity(new Intent(getActivity(), SettingActivity.class));
        } else if (view == tv_nick) {
            startActivity(new Intent(getActivity(), EditAccountActivity.class));
        } else if (view == civ_head) {

            PackageManager pm = getActivity().getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.jetcloud.hgbw"));
            if (!permission) {
                Out.Toast(getActivity(), "请在设置→应用管理→VR9钱包→打开存储权限");
            }else {
                //照片选择
                LinearLayout lin_dailog_item = (LinearLayout) View.inflate(
                        getActivity(), R.layout.dialog_choosephoto, null);
                dialog2 = new AlertDialog.Builder(getActivity()).create();
                dialog2.show();
                WindowManager.LayoutParams params = dialog2.getWindow()
                        .getAttributes();
                WindowManager wm = (WindowManager) getActivity()
                        .getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight() / 4;
                int weith = wm.getDefaultDisplay().getWidth() / 4 * 3;
                params.width = weith;
                params.height = height;
                dialog2.getWindow().setContentView(lin_dailog_item);
                dialog2.getWindow().setAttributes(params);

                //切换到全屏
                dialog2.getWindow()
                        .clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog2.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                RadioGroup rg_photo = (RadioGroup) lin_dailog_item
                        .findViewById(R.id.rg_photo);
                rg_photo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.fromcamera:
                                PackageManager pm = getActivity().getPackageManager();
                                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                        pm.checkPermission("android.permission.CAMERA", "com.jetcloud.hgbw"));
                                if (permission) {
                                    Intent intentFromCapture = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 判断存储卡是否可以用，可用进行存储


                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));

                                    startActivityForResult(intentFromCapture,
                                            CAMERA_REQUEST_CODE);

                                    dialog2.dismiss();
                                }else {
                                    Out.Toast(getActivity(), "请在设置→应用管理→VR9钱包→打开相机权限");
                                }

                                break;
                            case R.id.fromphoto:

                                Intent intent1 = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                                intent1.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent1,
                                            SELECT_PIC_KITKAT);
                                } else {
                                    startActivityForResult(intent1,
                                            IMAGE_REQUEST_CODE);
                                }
                                dialog2.dismiss();
                                break;

                            default:
                                break;
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:

                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));

                    break;
                case RESULT_REQUEST_CODE:
                    setImageToView(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = ImagePath.getImageAbsolutePath(getActivity(), uri);
            System.out.println(url);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
			/* Bitmap roundBitmap=ImageUtil.toRoundBitmap(photo); */
            String bitmapPath = saveBitmap(photo);
            System.out.println("上传图片"+bitmapPath);
            File f = new File(bitmapPath);
            uploadPicOrTitle(f);
        }
    }
    /**
     * 上传头像
    * */
    private void uploadPicOrTitle(File file) {
        final RequestParams params = new RequestParams(HgbwUrl.PIC_AND_NICK_URL);
        //缓存时间
        params.addBodyParameter("pic", file);
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
        params.setCacheMaxAge(1000 * 60);

        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CacheCallback<String>() {

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
                            Log.i(TAG_LOG, "upload onFinished: " + result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.has("pic") ){
                                    ImageLoader.getInstance().displayImage(
                                            URLDecoder.decode(HgbwUrl.HOME_URL + jsonObject.getString("pic")),
                                            civ_head,
                                            ImageLoaderCfg.options2);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    public String saveBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory(), "face"
                + ".jpg");
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getPicAndTitle() {
        final RequestParams params = new RequestParams(HgbwUrl.PIC_AND_NICK_URL);
        //缓存时间
        params.addQueryStringParameter("identity", SharedPreferenceUtils.getIdentity());
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
                        return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
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
                        if (!hasError && result != null) {
                            Log.i(TAG_LOG, "get nick and pic onFinished: " + result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.i(TAG_LOG, "onFinished: "  + jsonObject.getString("nickname") + "\n" + jsonObject.getString("pic"));
                                tv_nick.setText(jsonObject.getString("nickname"));
                                ImageLoader.getInstance().displayImage(
                                        URLDecoder.decode(HgbwUrl.HOME_URL + jsonObject.getString("pic")),
                                        civ_head,
                                        ImageLoaderCfg.options2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }
        });

    }

    /***
     * 注销请求
     */
    private void getLogout() {
        final RequestParams params = new RequestParams(HgbwUrl.LOGOUT_URL);
        //缓存时间
        params.addBodyParameter("identity", SharedPreferenceUtils.getIdentity());
        params.setCacheMaxAge(1000 * 60);
        x.task().run(new Runnable() {
            @Override
            public void run() {
                x.http().post(params, new Callback.CacheCallback<String>() {

                    private boolean hasError = false;
                    private String result = null;

                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
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
                            Log.i(TAG_LOG, "onFinished: " + result);
                            try {
                                getDataFromJson(result);
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

    /**
     * 处理json数据
     */
    private void getDataFromJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        Out.Toast(getActivity(), jsonObject.getString("status"));
        if (jsonObject.has("status") && jsonObject.getString("status").equals("success")) {
            SharedPreferenceUtils.setIdentity(SharedPreferenceUtils.WITHOUT_LOGIN);
            SharedPreferenceUtils.setBindStatus(SharedPreferenceUtils.UNBINDING_STATE);
            SharedPreferenceUtils.setMyAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
            SharedPreferenceUtils.setTradeAccount(SharedPreferenceUtils.WITHOUT_LOGIN);
            isHideBtnResiterAndLogin();
        }
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate adssadas fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        x.view().inject(this, rootView);
        return rootView;
    }

}
