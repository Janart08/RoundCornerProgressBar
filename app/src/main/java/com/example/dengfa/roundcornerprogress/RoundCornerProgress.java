package com.example.dengfa.roundcornerprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by dengfa on 17/3/16.
 */
public class RoundCornerProgress extends View {

    private static final String TAG = "RoundCornerProgress";

    protected final static int DEFAULT_MAX_PROGRESS       = 100;
    protected final static int DEFAULT_PROGRESS           = 0;
    protected final static int DEFAULT_SECONDARY_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS    = 30;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;
    public static final    int DEFAULT_WIDTH              = 400;
    public static final    int DEFAULT_HEIGHT             = 50;

    private int   radius;
    private int   padding;
    private float max;
    private float progress;
    private float secondaryProgress;
    private int   colorBackground;
    private int   colorProgress;
    private int   colorSecondaryProgress;

    private RectF    mRect  = new RectF();
    private Path     mPath  = new Path();
    private Paint    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Xfermode mMode  = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private int mWidth;
    private int mHeight;

    public RoundCornerProgress(Context context) {
        this(context, null);
    }

    public RoundCornerProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);
        radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));
        max = typedArray.getFloat(R.styleable.RoundCornerProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);
        int colorBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorBackground = typedArray.getColor(R.styleable.RoundCornerProgress_rcBackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_progress_default);
        colorProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcProgressColor, colorProgressDefault);
        int colorSecondaryProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
        colorSecondaryProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcSecondaryProgressColor, colorSecondaryProgressDefault);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = DEFAULT_WIDTH;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = DEFAULT_HEIGHT;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        Log.d(TAG, "onLayout: " + "mWidth - " + mWidth + " mHeight - " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        Log.d(TAG, "progress: " + progress);
        //绘制背景
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorBackground);
        mRect.set(0, 0, mWidth, mHeight);
        /* mPath.reset();
        float radii[] = new float[]{mRoundCorner, mRoundCorner, mRoundCorner, mRoundCorner, mRoundCorner, mRoundCorner, mRoundCorner, mRoundCorner};
        mPath.addRoundRect(mRect, radii, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);*/
        canvas.drawRoundRect(mRect, radius, radius, mPaint);

        // 绘制progress层
        int sc = canvas.saveLayer(0 + padding, 0 + padding, progress / max * mWidth - padding, mHeight - padding,
                null, Canvas.ALL_SAVE_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorProgress);
        mRect.set(0 + padding, 0 + padding, mWidth - padding, mHeight - padding);
        canvas.drawRoundRect(mRect, radius, radius, mPaint);

        // 设置遮挡属性
        mPaint.setColor(0xffff0000);
        mPaint.setXfermode(mMode);
        // 绘制遮挡
        mRect.set(0 + padding, 0 + padding, progress / max * mWidth - padding, mHeight - padding);
        float radii[] = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        mPath.reset();
        mPath.addRoundRect(mRect, radii, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

        // 绘制secondProgress层
        int sc2 = canvas.saveLayer(0 + padding, 0 + padding, secondaryProgress / max * mWidth - padding, mHeight - padding,
                null, Canvas.ALL_SAVE_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorSecondaryProgress);
        mRect.set(0 + padding, 0 + padding, mWidth - padding, mHeight - padding);
        canvas.drawRoundRect(mRect, radius, radius, mPaint);

        // 设置遮挡属性
        mPaint.setColor(0xffff0000);
        mPaint.setXfermode(mMode);
        // 绘制遮挡
        mRect.set(0 + padding, 0 + padding, secondaryProgress / max * mWidth - padding, mHeight - padding);
        float radii2[] = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        mPath.reset();
        mPath.addRoundRect(mRect, radii2, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc2);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius >= 0)
            this.radius = radius;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        if (padding >= 0)
            this.padding = padding;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0)
            this.max = max;
        if (this.progress > max)
            this.progress = max;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress < 0)
            this.progress = 0;
        else if (progress > max)
            this.progress = max;
        else
            this.progress = progress;
        invalidate();
    }

    public float getSecondaryProgress() {
        return secondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0)
            this.secondaryProgress = 0;
        else if (secondaryProgress > max)
            this.secondaryProgress = max;
        else
            this.secondaryProgress = secondaryProgress;
        invalidate();
    }

    public int getProgressBackgroundColor() {
        return colorBackground;
    }

    public void setProgressBackgroundColor(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public int getProgressColor() {
        return colorProgress;
    }

    public void setProgressColor(int colorProgress) {
        this.colorProgress = colorProgress;
    }

    public int getSecondaryProgressColor() {
        return colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(int colorSecondaryProgress) {
        this.colorSecondaryProgress = colorSecondaryProgress;
    }

    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}