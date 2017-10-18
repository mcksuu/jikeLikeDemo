package com.nick.jikelikedemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;

import java.util.List;

/**
 * Created by nick on 2017/10/16.
 */

public class TextScrollView extends android.support.v7.widget.AppCompatTextView {
    /**
     * 上滚的paint
     */
    private Paint mLowPaint;
    /**
     * 不滚的paint
     */
    private Paint mFixedPaint;
    /**
     * 下滚的paint
     */
    private Paint mHeightPaint;
    /**
     * 文字缩放的倍数
     */
    private float mAlpha = 1.2f;
    /**
     * 字数大小的两个y值
     */
    private float mSmallY;
    /**
     * 字数大的y值
     */
    private float mHeightY;
    /**
     * 固定不变的y值
     */
    private float mFixedY;
    /**
     * 高位滚动的位置
     */
    private int mHeightPosition;
    /**
     * 固定的位置
     */
    private int mFixedPosition;
    /**
     * 文字的高度
     */
    private float mTextHeight;
    /**
     * 前一次的文字
     */
    private String mPreText;
    /**
     * 滚动状态 上和下
     */
    private static final int STATE_UP = 1;
    private static final int STATE_DOWN = STATE_UP << 1;
    /**
     * 初始化状态
     */
    private static final int INIT = 0;
    /**
     * 状态值
     */
    private int state;

    public TextScrollView(Context context) {
        this(context, null);

    }

    public TextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLowPaint = new Paint();
        mLowPaint.setAntiAlias(true);
        mLowPaint.setDither(true);
        mLowPaint.setColor(Color.rgb(205, 209, 209));
        mLowPaint.setTextSize(getTextSize());
        mHeightPaint = new Paint();
        mHeightPaint.setAntiAlias(true);
        mHeightPaint.setDither(true);
        mHeightPaint.setColor(Color.rgb(205, 209, 209));
        mHeightPaint.setAlpha(0);
        mHeightPaint.setTextSize(getTextSize());
        mFixedPaint = new Paint();
        mFixedPaint.setAntiAlias(true);
        mFixedPaint.setDither(true);
        mFixedPaint.setColor(Color.rgb(205, 209, 209));
        mFixedPaint.setTextSize(getTextSize());
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (TextUtils.isEmpty(getText())) {
            return;
        }
        //计算文字高度以及固定不变的位置
        Paint.FontMetricsInt fontMetrics = mLowPaint.getFontMetricsInt();
        mSmallY = getMeasuredHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        mFixedY = getMeasuredHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        mTextHeight = Math.abs(fontMetrics.bottom - fontMetrics.top);
        mHeightY = getMeasuredHeight() / 2 + mFixedY + mTextHeight;
    }

    /**
     * 计算上滚的时候滚动的数字个数
     *
     * @return
     */
    public int calculateHeightPosition() {

        if (TextUtils.isEmpty(getText())) {
            return 0;
        }

        String content = getText().toString();
        boolean isUp = false;
        mHeightPosition = 1;
        int length = content.length();

        if (content.charAt(length - 1) - 48 + 1 == 10) {
            mHeightPosition++;
            isUp = true;
        }
        if (isUp) {
            for (int i = 1; i < length; i++) {
                char c = content.charAt(length - 1 - i);
                int temp = c - 48;
                if (temp + 1 == 10) {
                    mHeightPosition++;
                } else {
                    break;
                }
            }
        }

        return mHeightPosition;
    }

    /**
     * 计算下滚是滚动文字的数量
     *
     * @return
     */
    public int calculateDownHeightPosition() {

        if (TextUtils.isEmpty(getText())) {
            return 0;
        }

        String content = getText().toString();
        boolean isUp = false;
        mHeightPosition = 0;
        int length = content.length();

        if (content.charAt(length - 1) - 48 == 0) {
            mHeightPosition++;
            isUp = true;
        }
        if (isUp) {
            for (int i = 1; i < length; i++) {
                char c = content.charAt(length - 1 - i);
                int temp = c - 48;
                if (temp == 0) {
                    mHeightPosition++;
                } else {
                    break;
                }
            }
        }
        return mHeightPosition;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mPreText = getText().toString();
        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(getText())) {
            return;
        }
        //整体思路：一共三个画笔，分别画不变的位置，数字小的画笔以及数字大的画笔
        //通过不断改变 mSmallY这个值来同时改变mHeightY这个值进而产生动画效果，当然中间也包含了alpha动画
        String content = getText().toString();
        int position;
        switch (state) {
            case STATE_UP:
                position = mPreText.length() - mHeightPosition < 0 ? 0 : mPreText.length() - mHeightPosition;
                canvas.drawText(content, 0, content.length() - mHeightPosition < 0 ? 0 : content.length() - mHeightPosition, 0, mFixedY, mFixedPaint);
                canvas.drawText(mPreText, position < 0 ? 0 : position, mPreText.length(), mLowPaint.measureText(mPreText.substring(0, position)), mSmallY, mLowPaint);
                canvas.drawText(content, position, content.length(), mLowPaint.measureText(content.substring(0, position)), mHeightY, mHeightPaint);
                break;
            case STATE_DOWN:
                position = mPreText.length() - mHeightPosition - 1 < 0 ? 0 : mPreText.length() - mHeightPosition - 1;
                canvas.drawText(content, 0, content.length() - mFixedPosition < 0 ? 0 : content.length() - mFixedPosition, 0, mFixedY, mFixedPaint);
                canvas.drawText(content, position, content.length(), mLowPaint.measureText(content.substring(0, position)), mSmallY, mLowPaint);
                canvas.drawText(mPreText, position, mPreText.length(), mLowPaint.measureText(mPreText.substring(0, position)), mHeightY, mHeightPaint);
                break;
            default:
                canvas.drawText(content, 0, mFixedY, mFixedPaint);
                break;
        }
    }

    /**
     * 改变位置动画属性
     */
    public final static Property<TextScrollView, Float> SMALL_Y = new Property<TextScrollView, Float>(Float.class, "smallY") {
        @Override
        public Float get(TextScrollView view) {
            return view.getSmallY();
        }

        @Override
        public void set(TextScrollView view, Float value) {
            view.setSmallY(value);
        }
    };
    /**
     * 改变文字透明度动画属性
     */
    public final static Property<TextScrollView, Float> TEXT_ALPHA = new Property<TextScrollView, Float>(Float.class, "textAlpha") {
        @Override
        public Float get(TextScrollView view) {
            return view.getTextAlpha();
        }

        @Override
        public void set(TextScrollView view, Float value) {
            view.setTextAlpha(value);
        }
    };

    public Float getSmallY() {
        return mSmallY;
    }

    public void setSmallY(Float smallY) {
        this.mSmallY = smallY;
        this.mHeightY = mFixedY + (getMeasuredHeight() / 2 + mTextHeight) * smallY / mFixedY;
        invalidate();
    }

    public Float getTextAlpha() {
        return mAlpha;
    }

    public void setTextAlpha(Float scale) {
        this.mAlpha = scale;
        mLowPaint.setAlpha((int) (255 * mAlpha));
        mHeightPaint.setAlpha((int) (255 * (1 - mAlpha)));
        invalidate();
    }

    public List<Animator> upAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, SMALL_Y, mFixedY, 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, TEXT_ALPHA, 1f, 0f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(objectAnimator, objectAnimator2);
        return animationSet.getChildAnimations();
    }

    public void up() {
        state = STATE_UP;
        calculateHeightPosition();
        mFixedPosition = mHeightPosition;
        setText(Integer.valueOf(getText().toString()) + 1 + "");
    }

    public List<Animator> downAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, SMALL_Y, 0, mFixedY);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, TEXT_ALPHA, 0, 1f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(objectAnimator, objectAnimator2);
        return animationSet.getChildAnimations();
    }

    public void down() {
        state = STATE_DOWN;
        calculateDownHeightPosition();
        mFixedPosition = mHeightPosition + 1;
        setText(Integer.valueOf(getText().toString()) - 1 + "");
    }

    public void setInitState() {
        state = INIT;
    }
}
