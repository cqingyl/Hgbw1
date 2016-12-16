package com.jetcloud.hgbw.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jetcloud.hgbw.utils.Out;
import com.jetcolud.hgbw.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RegisterActivity extends BaseActivity {
	private EditText et_username,et_vernumber,et_password,et_passwordag;
	private CheckBox cb_agree;
	private Button bt_register;
	private TextView tv_getver;
	private Timer mTimer;
	private TimerTask mTask;
	private int time = 60;
	private Handler mHandler = new Handler();
	private boolean isCheck = false;
	private MyEditeListener editeListener ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_register);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topbar.setCenterText("新用户注册");
		Resources resources = RegisterActivity.this.getResources();
		 Drawable drawable = resources.getDrawable(R.drawable.fanhui);
		topbar.setLeftDrawable(false, drawable);
		
		editeListener=new MyEditeListener();
		et_username=getView(R.id.et_username);
		et_username.addTextChangedListener(editeListener);
		et_vernumber=getView(R.id.et_vernumber);
		et_vernumber.addTextChangedListener(editeListener);
		et_password=getView(R.id.et_password);
		et_password.addTextChangedListener(editeListener);
		et_passwordag=getView(R.id.et_passwordag);
		et_passwordag.addTextChangedListener(editeListener);
		cb_agree=getView(R.id.cb_agree);
		bt_register=getView(R.id.bt_register);
		tv_getver=getViewWithClick(R.id.tv_getver);
		//chebox监听
		cb_agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				isCheck=arg1;
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(et_username.getText().toString()) ||
						TextUtils.isEmpty(et_password.getText().toString())||
						TextUtils.isEmpty(et_vernumber.getText().toString())||
						TextUtils.isEmpty(et_passwordag.getText().toString())||!isCheck) {
					bt_register.setBackground(getResources().getDrawable(
							R.drawable.bt_loging_bg_no));
					bt_register.setEnabled(false);
				} else {
					bt_register.setBackground(getResources().getDrawable(
							R.drawable.bt_loging_bg));
					bt_register.setEnabled(true);
				}
			}
		});
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		if (view==tv_getver) {
			getVer();
		}
	}
	//获取验证码
	private void getVer() {
		
		if (TextUtils.isEmpty(et_username.getText().toString())) {
			Out.Toast(context, "请输入手机号");
			return;
		}
		tv_getver.setEnabled(false);
		

		mTimer = new Timer();
		mTask = new TimerTask() {

			@Override
			public void run() {
				if (time > 0) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							tv_getver.setText("已发送验证码（" + time + "）");
						}
					});
				} else {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							tv_getver.setText("重新获取");
							tv_getver.setEnabled(true);
						}
					});
					time = 60;
					mTimer.cancel();
				}
				time--;
			}
		};
		mTimer.schedule(mTask, 0, 1000);
	}
	private class MyEditeListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(et_username.getText().toString()) ||
					TextUtils.isEmpty(et_password.getText().toString())||
					TextUtils.isEmpty(et_vernumber.getText().toString())||
					TextUtils.isEmpty(et_passwordag.getText().toString())||!isCheck) {
				bt_register.setBackground(getResources().getDrawable(
						R.drawable.bt_loging_bg_no));
				bt_register.setEnabled(false);
			} else {
				bt_register.setBackground(getResources().getDrawable(
						R.drawable.bt_loging_bg));
				bt_register.setEnabled(true);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}}
}
