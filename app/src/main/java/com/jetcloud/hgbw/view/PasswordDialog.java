package com.jetcloud.hgbw.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.utils.Out;

import com.jetcloud.hgbw.view.passwordinputview.PasswordInputView;

import static com.jetcloud.hgbw.utils.Out.isActivityClosed;

/*
 * 自定义消息框
 * */

public class PasswordDialog {

    public Context mContext;
    private Dialog dialog;
    private String money;
//    private String num;
    private PasswordInputView passwordInputView;

    private TextView password_negative_btn, password_positive_btn, tv_card_info, tv_money;

    public PasswordDialog(Context context, String money) {
        this.money = money;
//        this.num = num;
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_passwordinputview);
        mContext = context;
        intView(dialog);
    }

    public void intView(Dialog dialog) {
        tv_money = (TextView) dialog.findViewById(R.id.tv_money);
        passwordInputView = (PasswordInputView) dialog.findViewById(R.id.passwordInputView);
        password_negative_btn = (TextView) dialog.findViewById(R.id.password_negative_btn);
        password_positive_btn = (TextView) dialog.findViewById(R.id.password_positive_btn);
        if (money == null) {
            tv_money.setText("");
        } else {
            tv_money.setText("金额:" + money);
        }
//		if (num!=null) {
//			tv_card_info.setText(num);
//
//		}

    }

    public Editable getPasswordInputView() {
        return passwordInputView.getText();
    }

    public void setPositiveButton(String text,
                                  View.OnClickListener positiveListener) {
        if (!TextUtils.isEmpty(text)) {
            password_positive_btn.setText(text);
        }
        if (positiveListener != null) {
            password_positive_btn.setOnClickListener(positiveListener);
        }
    }

    public void setNegativeButton(String text,
                                  View.OnClickListener negativeListener) {
        if (!TextUtils.isEmpty(text)) {
            password_negative_btn.setText(text);
        }
        if (negativeListener != null) {
            password_negative_btn.setOnClickListener(negativeListener);
        }
    }

    public void dismiss() {
        if (isActivityClosed(mContext)) {
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
