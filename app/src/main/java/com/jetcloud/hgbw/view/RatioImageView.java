package com.jetcloud.hgbw.view;







import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Ruby 等比例的图片
 */
public class RatioImageView extends ImageView {

	private float ratioW;// 按照W进行比例缩放
	private float ratioH;// 按照H进行比例缩放
	private String tag = "tag";
	 public static final int[] ratioImage = {
         0x7f010000, 0x7f010001
     };
	public RatioImageView(Context context) {
		super(context);
	}

	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getAttr(context, attrs);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttr(context, attrs);
	}

	private void getAttr(Context context, AttributeSet attrs) {
		TypedArray temp = context.obtainStyledAttributes(attrs,
				ratioImage);
		ratioW = temp.getFloat(0, 1f);
		ratioH = temp.getFloat(1, 1f);
		temp.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (ratioW != 1f) {
			setMeasuredDimension(width, (int) (width * ratioW + 0.5));
		} else {
			setMeasuredDimension((int) (height * ratioH + 0.5), height);
		}
	}

	public float getRatioW() {
		return ratioW;
	}

	public void setRatioW(float ratioW) {
		this.ratioW = ratioW;
	}

	public float getRatioH() {
		return ratioH; 
	}

	public void setRatioH(float ratioH) {
		this.ratioH = ratioH;
	}

}
