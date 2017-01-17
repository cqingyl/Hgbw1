package com.jetcloud.hgbw.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jetcloud.hgbw.R;
import com.jetcloud.hgbw.utils.Out;

/**
 * Created by Cqing on 2017/1/13.
 */

public class CusAlertDialogWithTwoBtn {
    public Context mContext;
    private Dialog dialog;
    private TextView tv_negative_btn, tv_positive_btn;
    private TextView title;
    private TextView content;
    private LinearLayout ll_two_btn_layout;
    private LinearLayout ll_one_btn_layout;
    private TextView tv_ok_btn;
    public static final String ONE_BTN = "one_btn";
    public static final String TWO_BTN = "two_btn";
    public CusAlertDialogWithTwoBtn(Context context) {
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.custom_alert_dialog);
        mContext = context;
        intView(dialog);
        ll_one_btn_layout.setVisibility(View.GONE);
        ll_two_btn_layout.setVisibility(View.VISIBLE);
    }
    public void intView(Dialog dialog){
        tv_negative_btn = (TextView) dialog.findViewById(R.id.tv_negative_btn);
        tv_positive_btn = (TextView) dialog.findViewById(R.id.tv_positive_btn);
        title = (TextView) dialog.findViewById(R.id.dialog_title);
        content = (TextView) dialog.findViewById(R.id.dialog_content);
        ll_two_btn_layout = (LinearLayout) dialog.findViewById(R.id.ll_two_btn_layout);
        ll_one_btn_layout = (LinearLayout) dialog.findViewById(R.id.ll_one_btn_layout);
        tv_ok_btn = (TextView) dialog.findViewById(R.id.tv_ok_btn);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.title.setText(title);
        };
    }

    public void setContent(String content) {
        this.content.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(content)) {
            this.content.setText(content);
        }
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
