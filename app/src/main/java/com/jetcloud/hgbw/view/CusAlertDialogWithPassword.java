package com.jetcloud.hgbw.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.utils.Out;

/**
 * Created by Cqing on 2017/1/13.
 */

public class CusAlertDialogWithPassword {
    public Context mContext;
    private Dialog dialog;
    private TextView tv_negative_btn, tv_positive_btn;
    private TextView title;
    private EditText dialog_pwd;
    private LinearLayout ll_two_btn_layout;
    private LinearLayout ll_one_btn_layout;
    private TextView tv_ok_btn;
    public static final String ONE_BTN = "one_btn";
    public static final String TWO_BTN = "two_btn";
    public CusAlertDialogWithPassword(Context context) {
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.custom_alert_dialog_with_password);
        mContext = context;
        intView(dialog);

    }
    public void intView(Dialog dialog){
        tv_negative_btn = (TextView) dialog.findViewById(R.id.tv_negative_btn);
        tv_positive_btn = (TextView) dialog.findViewById(R.id.tv_positive_btn);
        title = (TextView) dialog.findViewById(R.id.dialog_title);
        dialog_pwd = (EditText) dialog.findViewById(R.id.dialog_pwd);
        ll_two_btn_layout = (LinearLayout) dialog.findViewById(R.id.ll_two_btn_layout);
        ll_one_btn_layout = (LinearLayout) dialog.findViewById(R.id.ll_one_btn_layout);
        tv_ok_btn = (TextView) dialog.findViewById(R.id.tv_ok_btn);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.title.setText(title);
        };
    }

    public String getPassword () {
        return dialog_pwd.getText().toString();
    }

    public void setPositiveButton(String text,
                                  View.OnClickListener positiveListener) {
        if (!TextUtils.isEmpty(text)) {
            tv_positive_btn.setText(text);
        }
        if (positiveListener != null) {
            tv_positive_btn.setOnClickListener(positiveListener);
        }
    }
    public void setNegativeButton(String text,
                                  View.OnClickListener positiveListener) {
        if (!TextUtils.isEmpty(text)) {
            tv_negative_btn.setText(text);
        }
        if (positiveListener != null) {
            tv_negative_btn.setOnClickListener(positiveListener);
        }
    }
    public void dismiss() {
        if (Out.isActivityClosed(mContext)) {
            return;
        }
        dialog.dismiss();
    }

    public void show() {
        if (Out.isActivityClosed(mContext)) {
            return;
        }
        dialog.show();
    }

}
