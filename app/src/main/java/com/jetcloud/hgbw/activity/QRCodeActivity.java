package com.jetcloud.hgbw.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.app.HgbwApplication;
import com.jetcloud.hgbw.app.HgbwStaticString;
import com.jetcloud.hgbw.bean.FooddOutBean;
import com.jetcloud.hgbw.utils.SharedPreferenceUtils;
import com.jetcloud.hgbw.view.CustomProgressDialog;
import com.zxing.encoding.EncodingUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.jetcloud.hgbw.app.HgbwStaticString.MACHINE_NAME;
import static com.jetcloud.hgbw.app.HgbwStaticString.ORDER_NUM;

@ContentView(R.layout.activity_qrcode)
public class QRCodeActivity extends BaseActivity {
    private final static String TAG_LOG = QRCodeActivity.class.getSimpleName();

    @ViewInject(R.id.iv_qr_code)
    ImageView iv_qr_code;
    @ViewInject(R.id.activity_qrcode)
    LinearLayout activity_qrcode;

    private HgbwApplication app;
    private List<FooddOutBean> fooddOutBeanList;
    private String time;
    private CustomProgressDialog progress;
    private String orderNum;
    private String machineName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qrcode);
        x.view().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //设置屏幕亮度
        app = (HgbwApplication) getApplication();
        setWindowBrightness(255);
        topbar.setCenterText("我的二维码");
        Resources resources = this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.fanhui);
        topbar.setLeftDrawable(false, drawable);
        iv_qr_code = getView(R.id.iv_qr_code);
        WindowManager wm = (WindowManager) QRCodeActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = iv_qr_code.getLayoutParams();
        layoutParams.width = width / 3 * 2;
        layoutParams.height = width / 3 * 2;
        iv_qr_code.setLayoutParams(layoutParams);

        activity_qrcode = getView(R.id.activity_qrcode);
        activity_qrcode.setBackgroundResource(R.drawable.mine_bg);
    }

    @Override
    protected void loadData() {
        Intent i = getIntent();
        if (i.hasExtra(HgbwStaticString.FOOD_OUT) && i.hasExtra(ORDER_NUM) && i.hasExtra(MACHINE_NAME)) {
            fooddOutBeanList = (List<FooddOutBean>) i.getSerializableExtra
                    (HgbwStaticString.FOOD_OUT);
            orderNum = i.getStringExtra(ORDER_NUM);
            time = String.valueOf(System.currentTimeMillis());
            machineName = i.getStringExtra(MACHINE_NAME);
            make();
        }
    }


    //调整屏幕亮度
    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            if (bundle != null) {

                String result = bundle.getString("result");

//                tv_showResult.setText(result);

            }

        }


    }


    //生成二维码

    /**
     * identity
     * mechine_number
     * foodd_out
     * time
     * number
     **/
    public void make() {

        Gson gson = new Gson();
        String jsonData = gson.toJson(fooddOutBeanList);
        JSONObject fooddOut = null;
        try {
            fooddOut = new JSONObject();
            fooddOut.put("foodd_out", jsonData);
            fooddOut.put("identity", SharedPreferenceUtils.getIdentity());
            fooddOut.put("time", time);
            fooddOut.put("mechine_number", machineName);
            fooddOut.put("number", orderNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("log", "make: " + fooddOut.toString());

        Bitmap qrCode = EncodingUtils.createQRCode(fooddOut.toString(), 500, 500,

                null);

        iv_qr_code.setImageBitmap(qrCode);


    }
}
