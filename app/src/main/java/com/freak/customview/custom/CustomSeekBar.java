package com.freak.customview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.freak.customview.R;

import java.text.DecimalFormat;

/**
 * 自定义步骤
 * 构造函数------------view初始化
 * onMeasure（）-------测量view的大小
 * onSizeChanged（）---确定view的大小
 * onLayout（）--------确定子view布局（包含子view时使用）
 * \-------------------------------------------------\
 * \                                                 \ invalidate()
 * onDraw（）----------实际绘制内容                    \
 * 视图状态改变--------用户操作引起视图变化------------\
 *
 * @author Freak
 * @date 2019/8/22.
 */
public class CustomSeekBar extends View {
    /**
     * 最小宽度 单位dp
     */
    private static int MIN_WIDTH = 50;
    /**
     * 最小高度
     */
    private static int MIN_HEIGHT = 50;
    /**
     * 默认模式
     */
    private static int MODE_DEFAULT = 0;
    /**
     * 笔画模式
     */
    private static int MODE_STROKE = 0;
    /**
     * 填充模式
     */
    private static int MODE_FILL = 1;
    /**
     * 填充模式和笔画模式
     */
    private static int MODE_FILL_AND_STROKE = 2;
    /**
     * 进度格式化默认值
     */
    private static String PROGRESS_FORMAT_DEFAULT = "##0.0";
    /**
     * 进度默认最大值
     */
    private static float MAX_PROGRESS_DEFAULT = 100f;
    /**
     * 开始位置角度默认值
     */
    private static final float START_ANGLE_DEFAULT = 0f;
    /**
     * 刷新滑动速度默认值
     */
    private static final float VELOCITY_DEFAULT = 3.0f;
    /**
     * 文字大小默认值,单位为sp
     */
    private static final float TEXT_SIZE_DEFAULT = 10.0f;
    /**
     * 默认文字颜色
     */
    private static final int TEXT_COLOR_DEFAULT = 0xffbf5252;
    /**
     * 进度条边框宽度默认值,单位为dp
     */
    private static final float PROGRESS_WIDTH_DEFAULT = 5.0f;
    /**
     * 默认进度颜色
     */
    private static final int PROGRESS_COLOR_DEFAULT = 0xff3d85c6;
    /**
     * 进度条底色默认值，单位为dp
     */
    private static final float S_PROGRESS_WIDTH_DEFAULT = 2.0f;
    /**
     * 默认进度颜色
     */
    private static final int S_PROGRESS_COLOR_DEFAULT = 0xffdddddd;
    private Context mContext;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mProgressPaint;
    private Paint mSProgressPaint;
    /**
     * 进度模式
     */
    private int mMode;
    /**
     * 最大进度
     */
    private float mMaxProgress;
    /**
     * 是否显示文字
     */
    private boolean mShowText;
    /**
     * 起始角度
     */
    private float mStartAngle;
    /**
     * 速度
     */
    private float mVelocity;
    /**
     * 字体大小
     */
    private float mTextSize;
    /**
     * 字体颜色
     */
    private int mTextColor;
    /**
     * 进度条宽度
     */
    private float mProgressStrokeWidth;
    /**
     * 进度颜色
     */
    private int mProgressColor;
    /**
     * 二级进度宽度
     */
    private float mSProgressStrokeWidth;
    /**
     * 二级进度颜色
     */
    private int mSProgressColor;
    /**
     * 是否开启淡入淡出效果
     */
    private boolean mFadeEnable;
    /**
     * 开始透明度,0~255
     */
    private int mStartAlpha;
    /**
     * 结束透明度,0~255
     */
    private int mEndAlpha;
    /**
     * 二级进度缩放
     */
    private boolean mZoomEnable;
    /**
     * 进度条首尾是否圆角
     */
    private boolean mCapRound;

    private RectF mProgressRect;
    private RectF mSProgressRect;
    private Rect mTextBounds;
    /**
     * 当前角度
     */
    private float mCurrentAngle;
    /**
     * 目标角度
     */
    private float mTargetAngle;
    /**
     * 是否从中心绘制
     */
    private boolean mUseCenter;
    /**
     * 格式化数值
     */
    private DecimalFormat mFormat;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
            mMode = typedArray.getInt(R.styleable.CustomSeekBar_mode, MODE_DEFAULT);
            mMaxProgress = typedArray.getFloat(R.styleable.CustomSeekBar_maxProgress, MAX_PROGRESS_DEFAULT);
            mShowText = typedArray.getBoolean(R.styleable.CustomSeekBar_showText,
                    true);
            mStartAngle = typedArray.getFloat(R.styleable.CustomSeekBar_startAngle,
                    START_ANGLE_DEFAULT);
            mVelocity = typedArray.getFloat(R.styleable.CustomSeekBar_velocity,
                    VELOCITY_DEFAULT);
            mTextSize = typedArray.getDimension(R.styleable.CustomSeekBar_textSize,
                    DimenUtils.dip2px(mContext, TEXT_SIZE_DEFAULT));
            mTextColor = typedArray.getColor(R.styleable.CustomSeekBar_textColor,
                    TEXT_COLOR_DEFAULT);
            mProgressStrokeWidth = typedArray.getDimension(
                    R.styleable.CustomSeekBar_progressWidth,
                    DimenUtils.dip2px(mContext, PROGRESS_WIDTH_DEFAULT));
            mProgressColor = typedArray.getColor(
                    R.styleable.CustomSeekBar_progressColor,
                    PROGRESS_COLOR_DEFAULT);
            //第二进度宽度
            mSProgressStrokeWidth = typedArray.getDimension(
                    R.styleable.CustomSeekBar_sProgressWidth,
                    DimenUtils.dip2px(mContext, S_PROGRESS_WIDTH_DEFAULT));
            //第二进度颜色
            mSProgressColor = typedArray.getColor(
                    R.styleable.CustomSeekBar_sProgressColor,
                    S_PROGRESS_COLOR_DEFAULT);
            //是否开启淡入淡出效果
            mFadeEnable = typedArray.getBoolean(R.styleable.CustomSeekBar_fadeEnable,
                    false);
            mStartAlpha = typedArray
                    .getInt(R.styleable.CustomSeekBar_startAlpha, 255);
            mEndAlpha = typedArray.getInt(R.styleable.CustomSeekBar_endAlpha, 255);
            mZoomEnable = typedArray.getBoolean(R.styleable.CustomSeekBar_zoomEnable,
                    false);
            mCapRound = typedArray.getBoolean(R.styleable.CustomSeekBar_capRound,
                    true);
            float progress = typedArray.getFloat(R.styleable.CustomSeekBar_progress,
                    0);
            progress = progress > mMaxProgress || progress < 0f ? 0f : progress;
            //目标角度
            mTargetAngle = progress / mMaxProgress * 360f;
            //当前角度
            mCurrentAngle = mTargetAngle;
            typedArray.recycle();
        } else {
            //设置默认值
            mMode = MODE_DEFAULT;
            mMaxProgress = MAX_PROGRESS_DEFAULT;
            mStartAngle = START_ANGLE_DEFAULT;
            mVelocity = VELOCITY_DEFAULT;
            mTextSize = TEXT_SIZE_DEFAULT;
            mTextColor = TEXT_COLOR_DEFAULT;
            mProgressStrokeWidth = PROGRESS_WIDTH_DEFAULT;
            mProgressColor = PROGRESS_COLOR_DEFAULT;
            mSProgressStrokeWidth = S_PROGRESS_WIDTH_DEFAULT;
            mSProgressColor = S_PROGRESS_COLOR_DEFAULT;
            mTargetAngle = 0f;
            mCurrentAngle = 0f;
            mStartAlpha = 255;
            mEndAlpha = 255;
            mZoomEnable = false;
            mCapRound = true;
        }
        //初始化画笔
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //文字画笔
        mTextPaint = new Paint(mPaint);
        mTextPaint.setTextSize(mTextSize);
        mProgressPaint = new Paint(mPaint);
        //设置画笔颜色
        mProgressPaint.setColor(mProgressColor);
        //设置画笔宽度
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mSProgressPaint = new Paint(mPaint);
        mSProgressPaint.setColor(mSProgressColor);
        mSProgressPaint.setStrokeWidth(mSProgressStrokeWidth);
        //进度条首尾是否圆角
        if (mCapRound) {
            mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        //填充模式和笔画模式
        if (mMode == MODE_FILL_AND_STROKE) {
            mProgressPaint.setStyle(Paint.Style.FILL);
            mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            //是否从中心绘制
            mUseCenter = true;
        } else if (mMode == MODE_FILL) {
            mProgressPaint.setStyle(Paint.Style.FILL);
            mProgressPaint.setStyle(Paint.Style.FILL);
            //是否从中心绘制
            mUseCenter = true;
        } else {
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mSProgressPaint.setStyle(Paint.Style.STROKE);
            mUseCenter = false;
        }
        //矩形
        mProgressRect = new RectF();
        mSProgressRect = new RectF();
        mTextBounds = new Rect();
        //进度格式化
        mFormat = new DecimalFormat(PROGRESS_FORMAT_DEFAULT);
    }

    /**
     * 测量view的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算控件宽度与高度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //	AT_MOST
        //测量规范模式：子项可以达到指定大小所需的大小。
        //EXACTLY
        //测量规范模式：父母已确定孩子的确切大小。
        //UNSPECIFIED
        //测量规范模式：父母没有对孩子施加任何约束。
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //左+右+最小宽度
            int desired = (int) (getPaddingLeft() + DimenUtils.dip2px(mContext, MIN_WIDTH) + getPaddingRight());
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + DimenUtils.dip2px(mContext, MIN_HEIGHT) + getPaddingBottom());
            height = desired;
        }
        //设置测量的宽和高
        setMeasuredDimension(width, height);
        //计算进度显示的矩形框
        float radius = width > height ? height >> 1 : width >> 1;
        //计算进度的最大宽度
        float maxStrokeWidth = mProgressStrokeWidth > mSProgressStrokeWidth ? mProgressStrokeWidth : mSProgressStrokeWidth;
        radius = radius - getMaxPadding() - maxStrokeWidth;
        int centerX = width >> 1;
        int centerY = height >> 1;
        mProgressRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        mSProgressRect = new RectF(mProgressRect);
    }

    /**
     * 开始回绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //判断当前角度偏移方向
        if (mCurrentAngle > mTargetAngle) {
            mCurrentAngle = mCurrentAngle - mTargetAngle;
            if (mCurrentAngle < mTargetAngle) {
                mCurrentAngle = mTargetAngle;
            }
        } else if (mCurrentAngle < mTargetAngle) {
            mCurrentAngle = mCurrentAngle + mTargetAngle;
            if (mCurrentAngle > mTargetAngle) {
                mCurrentAngle = mTargetAngle;
            }
        }
        //缩放比例
        float ratio = mCurrentAngle / 360f;
        if (mFadeEnable) {
            int alpha = (int) ((mEndAlpha - mStartAlpha) * ratio);
            mProgressPaint.setAlpha(alpha);
        }
        //设置二级进度缩放效果
        if (mZoomEnable) {
            zoomSProgressRect(ratio);
        }
        //绘制耳机进度条
        canvas.drawArc(mProgressRect, mStartAngle, mCurrentAngle, mUseCenter, mProgressPaint);
        canvas.drawArc(mSProgressRect, 0, 360f, false, mSProgressPaint);
        //绘制字体
        if (mShowText) {
            String text = formatProgress(ratio * mMaxProgress);
            mTextPaint.getTextBounds(text, 0, text.length(), mTextBounds);
            //>> 右移运算符
            // 数学意义：
            //右移一位相当于除2，右移n位相当于除以2的n次方。
            //<<  左移运算符
            //数学意义：
            //在数字没有溢出的前提下，对于正数和负数，左移一位都相当于乘以2的1次方，左移n位就相当于乘以2的n次方。
            // 参数分别为 (文本 基线x 基线y 画笔)
//            canvas.drawText(text, (getWidth() - mTextBounds.width()) >> 1, (getHeight() + mTextBounds.height()) >> 1, mTextPaint);
            //根据paint的文字大小获得Paint.FontMetricsInt
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();

            //根据Paint.FontMetricsInt计算baseline
            canvas.drawText(text, (getWidth() >> 1) - mTextPaint.measureText(text) / 2,
                    (getHeight() >> 1) + ((fm.bottom - fm.top) >> 1) - fm.bottom, mTextPaint);
        }
        //如果当前进度不等于目标进度，继续绘制
        if (mCurrentAngle != mTargetAngle) {
            invalidate();
        }
    }

    /**
     * 第二进度缩放
     *
     * @param ratio
     */
    private void zoomSProgressRect(float ratio) {
        float width = mProgressRect.width();
        float height = mProgressRect.height();
        float centerX = mProgressRect.centerX();
        float centerY = mProgressRect.centerY();
        float offsetY = width * 0.5f * ratio;
        float offsetX = height * 0.5f * ratio;
        float left = centerX - offsetX;
        float right = centerX + offsetX;
        float top = centerY - offsetY;
        float bottom = centerY + offsetY;
        mSProgressRect.set(left, top, right, bottom);
    }

    /**
     * 格式化进度
     *
     * @param progress
     * @return
     */
    private String formatProgress(float progress) {
        return mFormat.format(progress);
    }

    /**
     * 获取内边距最大值
     *
     * @return
     */
    private int getMaxPadding() {
        int maxPadding = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        if (maxPadding < paddingRight) {
            maxPadding = paddingRight;
        }
        if (maxPadding < paddingTop) {
            maxPadding = paddingTop;
        }
        if (maxPadding < paddingBottom) {
            maxPadding = paddingBottom;
        }
        return maxPadding;
    }

    @Override
    protected void onDisplayHint(int hint) {
        if (hint == View.VISIBLE) {
            mCurrentAngle = 0;
            invalidate();
        }
        super.onDisplayHint(hint);
    }

    /**
     * 设置目标进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        progress = progress > mMaxProgress || progress < 0f ? 0f : progress;
        mTargetAngle = progress / mMaxProgress * 360f;
        postInvalidate();
    }

    /**
     * 设置目标进度
     *
     * @param progress 进度值
     * @param isAnim   是否有动画
     */
    public void setProgressWithAnim(float progress, boolean isAnim) {
        if (isAnim) {
            setProgress(progress);
        } else {
            progress = progress > mMaxProgress || progress < 0f ? 0f : progress;
            mCurrentAngle = progress / mMaxProgress * 360f;
            mTargetAngle = mCurrentAngle;
            postInvalidate();
        }
    }

    /**
     * 设置进度画笔着色方式
     *
     * @param shader
     */
    public void setProgressShader(Shader shader) {
        this.mProgressPaint.setShader(shader);
        invalidate();
    }

    /**
     * 设置二级进度画笔着色方式
     *
     * @param shader
     */
    public void setSProgressShader(Shader shader) {
        this.mSProgressPaint.setShader(shader);
        invalidate();
    }

    public void setMaxProgress(float max) {
        this.mMaxProgress = max;
    }

    public float getMaxProgress() {
        return mMaxProgress;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mMode) {
        this.mMode = mMode;
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    public float getVelocity() {
        return mVelocity;
    }

    public void setVelocity(float mVelocity) {
        this.mVelocity = mVelocity;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public float getProgressStrokeWidth() {
        return mProgressStrokeWidth;
    }

    public void setProgressStrokeWidth(float mProgressStrokeWidth) {
        this.mProgressStrokeWidth = mProgressStrokeWidth;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public float getSProgressStrokeWidth() {
        return mSProgressStrokeWidth;
    }

    public void setSProgressStrokeWidth(float mSProgressStrokeWidth) {
        this.mSProgressStrokeWidth = mSProgressStrokeWidth;
    }

    public int getSProgressColor() {
        return mSProgressColor;
    }

    public void setSProgressColor(int mSProgressColor) {
        this.mSProgressColor = mSProgressColor;
    }

    public boolean isFadeEnable() {
        return mFadeEnable;
    }

    public void setFadeEnable(boolean mFadeEnable) {
        this.mFadeEnable = mFadeEnable;
    }

    public int getStartAlpha() {
        return mStartAlpha;
    }

    public void setStartAlpha(int mStartAlpha) {
        this.mStartAlpha = mStartAlpha;
    }

    public int getEndAlpha() {
        return mEndAlpha;
    }

    public void setEndAlpha(int mEndAlpha) {
        this.mEndAlpha = mEndAlpha;
    }

    public boolean isZoomEnable() {
        return mZoomEnable;
    }

    public void setZoomEnable(boolean mZoomEnable) {
        this.mZoomEnable = mZoomEnable;
    }

}
