package com.michen.olaxueyuan.common;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.michen.olaxueyuan.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * Created by tianxiaopeng on 16/1/15.
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int ringColor;

    /**
     * 圆环进度的颜色
     */
    private int ringProgressColor;

    /**
     * 设置圆心进度条中间的背景色
     */
    private int centreColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float ringWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 进度开始的角度数
     */
    private int startAngle;

    /**
     * 是否显示中间的数字进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar_custom);

        //获取自定义属性和默认值，第一个参数是从用户属性中得到的设置，如果用户没有设置，那么就用默认的属性，即：第二个参数
        //圆环的颜色
        ringColor = mTypedArray.getColor(R.styleable.RoundProgressBar_custom_ringColor_custom,0xff50c0e9);
        //圆环进度条的颜色
        ringProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_custom_ringProgressColor_custom, 0xffffc641);
        //文字的颜色
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_custom_centerTextColor_custom, 0xffff5f5f);
        //文字的大小
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_custom_textSize_custom, 25);
        //圆环的宽度
        ringWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_custom_ringWidth_custom, 10);
        //最大进度
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_custom_max_custom, 100);
        //当前进度
        progress = mTypedArray.getInt(R.styleable.RoundProgressBar_custom_progress_custom, 30);
        //是否显示中间的进度
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_custom_textIsDisplayable_custom, true);
        //进度的风格，实心或者空心
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_custom_style_custom, 0);
        //进度开始的角度数
        startAngle = mTypedArray.getInt(R.styleable.RoundProgressBar_custom_startAngle_custom, -90);
        //圆心的颜色
        centreColor = mTypedArray.getColor(R.styleable.RoundProgressBar_custom_centreColor_custom, 0);
        //回收资源
        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - ringWidth/2); //圆环的半径

        /**
         * 画中心的颜色
         */
        if (centreColor != 0) {
            paint.setAntiAlias(true);
            paint.setColor(centreColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centre, centre, radius, paint);
        }

        /**
         * 画最外层的大圆环
         */
        paint.setColor(ringColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(ringWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(ringWidth); //设置圆环的宽度
        paint.setColor(ringProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);

                /*第二个参数是进度开始的角度，-90表示从12点方向开始走进度，如果是0表示从三点钟方向走进度，依次类推
                 *public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
                    oval :指定圆弧的外轮廓矩形区域。
                    startAngle: 圆弧起始角度，单位为度。
                    sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
                    useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
                    paint: 绘制圆弧的画板属性，如颜色，是否填充等
                 *
                */
                canvas.drawArc(oval, startAngle, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    canvas.drawArc(oval, startAngle, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if(textIsDisplayable && percent != 0 && style == STROKE){
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
        }





    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }


    public int getCircleColor() {
        return ringColor;
    }

    public void setCircleColor(int CircleColor) {
        this.ringColor = CircleColor;
    }

    public int getCircleProgressColor() {
        return ringProgressColor;
    }

    public void setCircleProgressColor(int CircleProgressColor) {
        this.ringProgressColor = CircleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getringWidth() {
        return ringWidth;
    }

    public void setringWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

}