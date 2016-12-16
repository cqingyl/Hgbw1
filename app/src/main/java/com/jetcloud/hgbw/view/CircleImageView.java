package com.jetcloud.hgbw.view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	// 锟斤拷前锟介建锟侥匡拷锟斤拷锟竭讹拷
	private int viewWidth, viewHeigth;

	// 锟斤拷前锟介建锟斤拷围圆锟斤拷锟侥匡拷龋锟侥拷锟斤拷锟斤拷锟斤拷锟截ｏ拷
	private int arcWidth = 5;
	// 锟斤拷前锟介建锟斤拷围圆锟斤拷锟斤拷锟斤拷色锟斤拷默锟斤拷锟斤拷锟斤拷锟斤拷兀锟�
	private int arcColor = Color.parseColor("#eeeeee");
	// 锟斤拷锟斤拷圆锟斤拷锟侥伙拷锟斤拷
	private Paint arcPaint;
	// 圆锟斤拷锟侥半径
	private int arcRadius;
	// 头锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private RectF imgRect;

	public CircleImageView(Context context) {
		this(context, null);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {
		arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		imgRect = new RectF();
	}

	// 锟斤拷锟斤拷默锟较碉拷锟斤拷围圆锟斤拷锟斤拷锟�
	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}

	// 锟斤拷锟斤拷默锟较碉拷锟斤拷围圆锟斤拷锟斤拷锟斤拷锟缴�
	public void setArcColor(int arcColor) {
		this.arcColor = arcColor;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = getMeasuredWidth();
		viewHeigth = getMeasuredHeight();
		// 圆锟斤拷锟诫径锟角碉拷前锟介建锟斤拷锟斤拷锟竭度碉拷锟斤拷小值
		arcRadius = Math.min(viewWidth, viewHeigth);

		// 锟斤拷锟矫碉拷前头锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		ViewGroup.LayoutParams params = getLayoutParams();
		params.width = arcRadius;
		params.height = arcRadius;
		requestLayout();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		arcPaint.setStyle(Paint.Style.STROKE);
		arcPaint.setStrokeWidth(arcWidth);
		arcPaint.setColor(arcColor);
		arcPaint.setAntiAlias(true);

		/*canvas.drawColor(Color.TRANSPARENT);*/ // 透锟斤拷锟斤拷锟斤拷
		imgRect.left = arcWidth / 2;
		imgRect.top = arcWidth / 2;
		imgRect.right = arcRadius - arcWidth / 2;
		imgRect.bottom = arcRadius - arcWidth / 2;
		canvas.drawOval(imgRect, arcPaint);
	}

	// 锟斤拷取锟斤拷锟矫碉拷图片
	@Override
	public void setImageBitmap(Bitmap bm) {
		setBackgroundDrawable(null);
		Bitmap circleBitmap = drawCircleBitmap(bm);

		super.setImageBitmap(circleBitmap);
	}

	// 锟斤拷取圆锟斤拷图片
	private Bitmap drawCircleBitmap(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int baseWidth = Math.min(width, height);

		Bitmap circleBitmap = Bitmap.createBitmap(baseWidth, baseWidth,
				Bitmap.Config.ARGB_8888);

		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		int color = 0xff424242;
		p.setColor(color);
		p.setStyle(Paint.Style.FILL);
		p.setAntiAlias(true);

		Canvas tempCanvas = new Canvas(circleBitmap);
		tempCanvas.drawARGB(0, 0, 0, 0);

		Rect src = new Rect(0, 0, baseWidth, baseWidth);
		RectF rectF = new RectF(src);
		tempCanvas.drawRoundRect(rectF, baseWidth / 2, baseWidth / 2, p);
		p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		Rect targ = new Rect(0, 0, baseWidth, baseWidth);
		tempCanvas.drawBitmap(bitmap, src, targ, p);

		return circleBitmap;
	}
}
