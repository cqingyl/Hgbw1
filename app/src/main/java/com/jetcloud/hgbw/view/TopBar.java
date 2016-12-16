package com.jetcloud.hgbw.view;

import com.jetcolud.hgbw.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TopBar extends RelativeLayout {
	private TextView mLeft, mRight, mCenter;

	private Context context;
	private ITopBarClickListener topBarClickListener;

	public TopBar(Context context) {
		this(context, null, 0);
	}

	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
		setListener();
	}

	private void init(AttributeSet attrs) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.top_bar_struct, this, true);

		mLeft = (TextView) findViewById(R.id.tv_top_left);
		mRight = (TextView) findViewById(R.id.tv_top_right);
		mCenter = (TextView) findViewById(R.id.tv_top_search);
	}

	private void setListener() {
		mLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topBarClickListener != null) {
					topBarClickListener.leftClick();
				}
			}
		});

		mRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topBarClickListener != null) {
					topBarClickListener.rightClick();
				}
			}
		});

		mCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});
	}

	public void hideLeftDrawable() {
		mLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	public void setLeftDrawable(boolean isRight, Drawable drawable) {
		if (isRight) {
			mLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable,
					null);
		} else {
			mLeft.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null,
					null);
		}
	}

	public TextView getmLeft() {
		return mLeft;
	}

	public TextView getmRight() {
		return mRight;
	}

	public void hideRightDrawable() {
		mRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	/**
	 * 设置头部右边
	 * 
	 * @param param
	 *            文字内容
	 * @param leftDrawable
	 *            图片内容
	 * @param flag
	 *            true图片左边显示，false图片右边显示
	 */
	public void setRightText(String param, Drawable leftDrawable, Boolean flag) {
		mRight.setText(param);
		if (leftDrawable != null) {
			if (flag) {
				mRight.setCompoundDrawablesWithIntrinsicBounds(leftDrawable,
						null, null, null);
			} else {
				mRight.setCompoundDrawablesWithIntrinsicBounds(null, null,
						leftDrawable, null);
			}
		}

	}

	/**
	 * 设置头部右边
	 * 
	 * @param param
	 *            文字内容
	 * @param leftDrawable
	 *            图片内容
	 * @param flag
	 *            true图片左边显示，false图片右边显示
	 */
	public void setRightText(String param) {
		mRight.setText(param);

	}

	/**
	 * 设置头部左边
	 * 
	 * @param param
	 *            文字内容
	 * @param leftDrawable
	 *            图片内容
	 * @param flag
	 *            true图片左边显示，false图片右边显示
	 */
	public void setLeftText(String param, Drawable leftDrawable, Boolean flag) {
		mLeft.setText(param);
		if (leftDrawable != null) {
			if (flag) {
				mLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable,
						null, null, null);
			} else {
				mLeft.setCompoundDrawablesWithIntrinsicBounds(null, null,
						leftDrawable, null);
			}
		}

	}

	public void setRightVisible(int flag) {
		mRight.setVisibility(flag);
	}

	public void setLeftVisible(int flag) {
		mLeft.setVisibility(flag);
	}

	public void setTopBarClickListener(ITopBarClickListener topBarClickListener) {
		this.topBarClickListener = topBarClickListener;
	}

	public void setCenterText(String centerText) {
		mCenter.setEllipsize(TruncateAt.valueOf("END"));
		android.view.ViewGroup.LayoutParams layoutParams = mCenter.getLayoutParams();
		layoutParams.width=300;
		mCenter.setLayoutParams(layoutParams);
		mCenter.setSingleLine(true); 
		mCenter.setText(centerText);
	}

	public void setCenterText(int resid) {
		mCenter.setText(resid);
	}

	public interface ITopBarClickListener {

		void leftClick();

		void rightClick();
	}
}
