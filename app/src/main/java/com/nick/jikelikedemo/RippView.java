package com.nick.jikelikedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

/**
 * Created by KangShuai on 2017/10/17.
 */

public class RippView extends View {

    private float mRadius;
    private Paint mPaint;

    public RippView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(243, 142, 112));
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.width));
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public RippView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);
    }

    public static Property<RippView, Float> RADIUS = new Property<RippView, Float>(Float.class, "radius") {
        @Override
        public Float get(RippView object) {
            return object.getRadius();
        }

        @Override
        public void set(RippView object, Float value) {
            object.setRadius(value);
        }
    };

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    public Float getRadius() {
        return mRadius;
    }
}
