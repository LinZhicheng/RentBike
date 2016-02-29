package com.android.davidlin.rentbike.usercontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Reference http://blog.csdn.net/liu1164316159/article/details/42914511
 * Created by David Lin on 2015/11/7.
 */
public class CircleTextView extends TextView {

    private Paint mBgPaint = new Paint();
    private PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);
        setTextColor(Color.WHITE);
    }

    public CircleTextView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max, max);
    }

//    @Override
//    public void setBackgroundColor(int color) {
//        mBgPaint.setColor(color);
//    }

    /**
     * 设置通知个数显示
     *
     * @param text 消息数量
     */
    public void setNotifiText(int text) {
        if (text > 99) {
            String s = "99+";
            setText(s);
            return;
        }
        setText(String.valueOf(text));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(pfd);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
        super.draw(canvas);
    }

}
