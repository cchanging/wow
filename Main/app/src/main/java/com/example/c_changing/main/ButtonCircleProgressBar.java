package com.example.c_changing.main;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class ButtonCircleProgressBar  extends ProgressBar {
    private static final int DEFAULT_TEXT_COLOR = 0XFF2F4F4F;
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0;
    private static final int DEFAULT_COLOR_YE = 0XFF2F4F4F;
    private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;

    /**
     * The status of this view currently;
     */
    private Status mStatus = Status.End;
    public int State = -1;

    /**
     * painter of all drawing things
     */
    protected Paint mPaint = new Paint();


    /**
     * height of reached progress bar
     */
    protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

    /**
     * color of reached bar
     */
    protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
    /**
     * color of unreached bar
     */
    protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
    /**
     * height of unreached progress bar
     */
    protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);

    /**
     * the length of triangle
     */
    private int triangleLength;

    /**
     * use path to draw triangle
     */
    private Path mPath;

    /**
     * mRadius of view
     */
    private int mRadius = dp2px(30);


    public ButtonCircleProgressBar(Context context) {
        this(context, null);
    }

    public ButtonCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init values from custom attributes
        final TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.ButtonCircleProgressBar);

        mReachedBarColor = attributes
                .getColor(
                        R.styleable.ButtonCircleProgressBar_progress_reached_color,
                        DEFAULT_COLOR_YE);
        mUnReachedBarColor = attributes
                .getColor(
                        R.styleable.ButtonCircleProgressBar_progress_unreached_color,
                        DEFAULT_COLOR_UNREACHED_COLOR);
        mReachedProgressBarHeight = (int) attributes
                .getDimension(
                        R.styleable.ButtonCircleProgressBar_progress_reached_bar_height,
                        mReachedProgressBarHeight);
        mUnReachedProgressBarHeight = (int) attributes
                .getDimension(
                        R.styleable.ButtonCircleProgressBar_progress_unreached_bar_height,
                        mUnReachedProgressBarHeight);

        mRadius = (int) attributes.getDimension(
                R.styleable.ButtonCircleProgressBar_radius, mRadius) -10;
        triangleLength = mRadius;
        attributes.recycle();


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();//need path to draw triangle
    }


    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        invalidate();
    }

    public enum Status {
        End,
        Starting
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int paintWidth = Math.max(mReachedProgressBarHeight,
                mUnReachedProgressBarHeight);

        if (heightMode != MeasureSpec.EXACTLY) {

            int exceptHeight = (int) (getPaddingTop() + getPaddingBottom()
                    + mRadius * 2 + paintWidth);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight,
                    MeasureSpec.EXACTLY);
        }
        if (widthMode != MeasureSpec.EXACTLY) {
            int exceptWidth = (int) (getPaddingLeft() + getPaddingRight()
                    + mRadius * 2 + paintWidth);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth,
                    MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(State != -1) {
            canvas.save();
            //   CanvasWidth/2-ViewWidth/2
            canvas.translate(55, 23);
            mPaint.setStyle(Paint.Style.STROKE);
            // draw unreaded bar
            mPaint.setColor(mUnReachedBarColor);
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
            // draw reached bar
            mPaint.setColor(mReachedBarColor);
            mPaint.setStrokeWidth(mReachedProgressBarHeight);
            float sweepAngle = getProgress() * 1.0f / getMax() * 360;
            canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0,
                    sweepAngle, false, mPaint);

            mPath = new Path();//need path to draw triangle

            triangleLength = mRadius;
            float leftX = (float) ((2 * mRadius - Math.sqrt(3.0) / 2 * triangleLength) / 2);
            float realX = (float) (leftX + leftX * 0.2);
            mPath.moveTo(realX, mRadius - (triangleLength / 2));
            mPath.lineTo(realX, mRadius + (triangleLength / 2));
            mPath.lineTo((float) (realX + Math.sqrt(3.0) / 2 * triangleLength), mRadius);
            mPath.lineTo(realX, mRadius - (triangleLength / 2));

            if (mStatus == Status.End) {//未开始状态，画笔填充
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(mPath, mPaint);//直接drawPath
            } else {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(dp2px(5));
                canvas.drawLine(mRadius * 2 / 3, mRadius * 2 / 3, mRadius * 2 / 3, 2 * mRadius * 2 / 3, mPaint);
                canvas.drawLine(2 * mRadius - (mRadius * 2 / 3), mRadius * 2 / 3, 2 * mRadius - (mRadius * 2 / 3), 2 * mRadius * 2 / 3, mPaint);
            }
            canvas.restore();
        }
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*public static int dp2px(float dpValue) {
        Context context = MyApplication.getApplication();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }*/
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}