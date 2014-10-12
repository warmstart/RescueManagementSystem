package net.ut11.ccmp.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import rms.rescuemanagementsystem.R;


public class CountdownView extends View {

	private int maxValue = 100;
	private int value = 50;

	private Paint textPaint;
	private Paint grayArcPaint;
	private Paint blueArcPaint;
	private Paint circlePaint;

	private RectF circleBounds;
	private int circleRadius;
	private int smallCircleRadius;

	public CountdownView(Context context) {
		super(context);
	}

	public CountdownView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		blueArcPaint = new Paint();
		blueArcPaint.setAntiAlias(true);
		blueArcPaint.setColor(Color.BLUE);
		blueArcPaint.setStyle(Paint.Style.STROKE);
		blueArcPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.countdown_view_stroke_width));

		grayArcPaint = new Paint(blueArcPaint);
		grayArcPaint.setColor(Color.LTGRAY);

		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.BLUE);
		textPaint.setTextAlign(Paint.Align.CENTER);

		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.BLUE);

		circleBounds = new RectF();

		smallCircleRadius = getResources().getDimensionPixelSize(R.dimen.countdown_view_circle_radius);
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		invalidate();
	}

	public void setValue(int value) {
		this.value = value;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();
		final int sqSize = Math.min(width, height) - smallCircleRadius * 2;
		final int tl = sqSize / -2;
		final int rb = sqSize / 2;
		circleRadius = rb;

		textPaint.setTextSize(sqSize / 3);
		circleBounds.set(tl, tl, rb, rb);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();
		final int currentAngle = (maxValue - value) * 360 / maxValue;
		final int remainingAngle = 360 - currentAngle;

		canvas.translate(width / 2, height / 2);

		canvas.drawArc(circleBounds, -90 + currentAngle, remainingAngle, false, grayArcPaint);
		canvas.drawArc(circleBounds, -90, currentAngle, false, blueArcPaint);

		double rad = Math.toRadians(currentAngle);
		float x = (float) (circleRadius * Math.sin(rad));
		float y = (float) (circleRadius * -1 * Math.cos(rad));
		canvas.drawCircle(x, y, smallCircleRadius, circlePaint);

		int yPos = (int) (0 - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
		canvas.drawText(String.valueOf(value), 0, yPos, textPaint);
	}
}
