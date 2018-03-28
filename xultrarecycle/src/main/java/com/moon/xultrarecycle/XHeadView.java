package com.moon.xultrarecycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;


import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * author: moon
 * created on: 17/10/18 下午12:55
 * description:  仿微信小程序三个点的头部刷新动画
 */
public class XHeadView extends View implements PtrUIHandler, Runnable {

    private Paint mPaint; //


    private static final float RADIUS = 10;

    private static final int SIZE = 3;

    public boolean isExecutingAnimation ;

    private int index;

    private int limitHeadViewMarginLeftDimen;

    private int headViewHeight;

    public XHeadView(Context context) {
        super(context);
        init();
    }

    public XHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    /**
     * 初始化 ** 等
     */
    private void init() {
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        limitHeadViewMarginLeftDimen = (wm.getDefaultDisplay().getWidth() - 100) / 2;

        headViewHeight = (int) getContext().getResources().getDimension(R.dimen.xheadview_height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centX = limitHeadViewMarginLeftDimen ;

        float centY =  getContext().getResources().getDimension(R.dimen.xheadview_centy);

        int darkPosX = 0;

        mPaint.setColor(Color.GRAY);
        for (int i = 0; i < SIZE; i++) {
            if (index % 3 == i) {
                darkPosX = centX;
            }
            canvas.drawCircle(centX, centY, RADIUS, mPaint);
            centX += 50;
        }

        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(darkPosX, centY, RADIUS, mPaint);

        index++;
    }

    @Override
    public void run() {
        while (isExecutingAnimation){
            postInvalidate();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以下四个方法执行顺序  prepare -> begin -> complete -> reset ********************************
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

        // 准备刷新的UI。 手指拖住，下拉，不松手的时候会触发， 一次
        // TODO: 18/1/8
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        // 开始刷新的UI动画。
        isExecutingAnimation = true;

        new Thread(this).start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        // 刷新完成，停止刷新动画。
        isExecutingAnimation = false;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        // 重置头View的动画状态，一般停止刷新动画。
        isExecutingAnimation = false;
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        // 手指下拉的时候的状态，我们的下拉动画的控制就是通过这个方法：
        // frame是刷新的root layout。
        // isUnderTouch是手指是否按下，因为还有自动刷新，手指肯定是松开状态。
        // status是现在的加载状态，准备、加载中、完成：PREPARE、LOADING、COMPLETE。
        // ptrIndicator是一些下拉偏移量的参数封装。

        int headerHeight = ptrIndicator.getHeaderHeight();//头部的高度
        int lastPosY = ptrIndicator.getLastPosY();//上一次滑动的Y偏移值
        int offsetToRefresh = ptrIndicator.getOffsetToRefresh();//舒心需要滑动的偏移量
        float offsetY = ptrIndicator.getOffsetY();//当前与上一次滑动处理的偏移值
        int currentPosY = ptrIndicator.getCurrentPosY();//当前系统偏移值

        if (isUnderTouch&&status== PtrFrameLayout.PTR_STATUS_PREPARE) {

            if(currentPosY<offsetToRefresh&&lastPosY >= offsetToRefresh){
                //表示不刷新了
//                mDescText.setText("下拉加载数据");

                isExecutingAnimation = false;

            }else if(currentPosY>offsetToRefresh&&lastPosY<=offsetToRefresh){
//                mDescText.setText("松开加载更多");

            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureDimension(100, widthMeasureSpec);
        int height = measureDimension(headViewHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
