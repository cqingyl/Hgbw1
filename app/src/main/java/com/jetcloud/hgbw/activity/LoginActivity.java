package com.jetcloud.hgbw.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.utils.Out;

@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity {
	private EditText username,password;
	private Button bt_loging;
	private MyEditeListener editeListener;
	private TextView tv_register,tv_forgetPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_login);

		super.onCreate(savedInstanceState);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		username=getView(R.id.et_username);
		password=getView(R.id.et_password);
		bt_loging = getViewWithClick(R.id.bt_loging);
		editeListener=new MyEditeListener();
		username.addTextChangedListener(editeListener);
		password.addTextChangedListener(editeListener);
		tv_register=getViewWithClick(R.id.tv_register);
		tv_forgetPwd=getViewWithClick(R.id.tv_forget_pwd);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		bt_loging.setEnabled(false);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		if (view==bt_loging) {
			Out.Toast(LoginActivity.this, "登录");
		}else if (view==tv_register) {
			startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
		}else if (view==tv_forgetPwd) {
			startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

		}
	}
	private class MyEditeListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
				bt_loging.setBackground(getResources().getDrawable(
						R.drawable.bt_loging_bg_no));
				bt_loging.setEnabled(false);
			} else {
				bt_loging.setBackground(getResources().getDrawable(
						R.drawable.bt_loging_bg));
				bt_loging.setEnabled(true);
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
