package com.tengteng.ui.fish;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author yejiasun
 * @date Create on 12/1/22
 */

class CustomFishDrawable extends Drawable {
    /**
     * 绘制的路径
     */
    private Path mPath;
    //我的画笔
    private Paint mPaint;
    /**
     * 鱼的透明度
     */
    private static final int FISH_ALPHA = 110;
    /**
     * 鱼的中心点，确保鱼在这个点绘制之后，能360旋转不被遮挡
     */
    private PointF middlePoint;
    /**
     * 鱼头的中心点，鱼头的半径R作为计量单位，其余各处的Point的位置都是从这个点来出发计算的
     */
    private PointF headPoint;
    /**
     * 鱼头的半径
     */
    private static final float FISH_HEAD_RADIUS = 100;
    /**
     * 鱼身长度
     */
    private float FISH_BODY_LENGTH = 3.2f * FISH_HEAD_RADIUS;
    /**
     * 鱼鳍的长度
     */
    private static final float FISH_FINS_LENGTH = 0.9f * FISH_HEAD_RADIUS;
    /**
     * 节肢大圆的半径
     */
    private float BIG_CIRCLE_RADIUS = 0.7f * FISH_HEAD_RADIUS;
    /**
     * 节肢中间那个圆的大小
     */
    private float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;
    /**
     * 节肢小圆半径
     */
    private float SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS;
    /**
     * 节肢大圆圆心到中圆圆心的距离
     */
    private final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1);
    /**
     * 节肢中圆圆心到小圆圆心的距离
     */
    private final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    /**
     * 中圆圆心到大三角的底部中心点
     */
    private final float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 3.0f;
    /**
     * 鱼的朝向
     */
    private int fishMainAngle = 0;
    /**
     * 当前获取到的鱼的属性动画值
     */
    private float currentValue = 0;

    public CustomFishDrawable() {
        init();
    }

    /**
     * 初始化paint 和 path  和 middlePoint
     */
    private void init() {
        //设置画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.argb(FISH_ALPHA, 162, 205, 90));
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        //设置绘制路径
        mPath = new Path();
        //设置middlePoint
        middlePoint = new PointF(4.19f * FISH_HEAD_RADIUS, 4.19f * FISH_HEAD_RADIUS);
        //帮助鱼摆动起来
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 3600f);
        valueAnimator.setDuration(16*1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                invalidateSelf();
                System.out.println("tt currentValue="+currentValue);
            }
        });
        valueAnimator.start();
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        int fishAngle = fishMainAngle;
        float segmentAngleSegment1 = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 15);
        float segmentAngleSegment2 = (float) (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5 )) * 35);

        //画脑袋（画圆=中心点+半径）
        headPoint = calculatePoint(middlePoint, FISH_BODY_LENGTH / 2, segmentAngleSegment1);
        canvas.drawCircle(headPoint.x, headPoint.y, FISH_HEAD_RADIUS, mPaint);

        //画鱼鳍（二阶贝塞尔曲线= 起点，控制点，终点）
        drawFins(canvas, headPoint, segmentAngleSegment1, true);
        drawFins(canvas, headPoint, segmentAngleSegment1, false);

        //画节肢=大圆+小圆+每两个圆切线四个点做成的等腰梯形x2+头和大圆之间的身体部分
        //找到每个圆心点，画圆
        PointF circleBigPoint = calculatePoint(headPoint, FISH_BODY_LENGTH, segmentAngleSegment1 - 180);
        PointF circleMiddlePoint = calculatePoint(circleBigPoint, FIND_MIDDLE_CIRCLE_LENGTH, segmentAngleSegment1 - 180);
        PointF circleSmallPoint = calculatePoint(circleMiddlePoint, FIND_SMALL_CIRCLE_LENGTH, segmentAngleSegment2 - 180);
        canvas.drawCircle(circleBigPoint.x, circleBigPoint.y, BIG_CIRCLE_RADIUS, mPaint);
        canvas.drawCircle(circleMiddlePoint.x, circleMiddlePoint.y, MIDDLE_CIRCLE_RADIUS, mPaint);
        canvas.drawCircle(circleSmallPoint.x, circleSmallPoint.y, SMALL_CIRCLE_RADIUS, mPaint);

        //头和大圆之间身体部分
        drawBody(canvas, headPoint, circleBigPoint, FISH_HEAD_RADIUS, BIG_CIRCLE_RADIUS, fishAngle);
        //大圆和中圆之间的等腰梯形
        drawIsoscelesTrapezoidal(canvas, circleBigPoint, circleMiddlePoint, BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS, segmentAngleSegment1);
        //中圆和小圆之间的等腰梯形
        drawIsoscelesTrapezoidal(canvas, circleMiddlePoint, circleSmallPoint, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS, segmentAngleSegment2);
        //画尾巴
        drawTriangle(canvas, circleMiddlePoint, FIND_TRIANGLE_LENGTH, segmentAngleSegment2);


    }

    /**
     * 画等腰三角形
     *
     * @param canvas
     * @param circleCenterPoint
     * @param lenght
     * @param fishAngle
     */
    private void drawTriangle(Canvas canvas, PointF circleCenterPoint, float lenght, float fishAngle) {
        PointF tailCenterPointLeft = calculatePoint(circleCenterPoint, lenght, fishAngle + 135);
        PointF tailCenterPointRight = calculatePoint(circleCenterPoint, lenght, fishAngle - 135);
        mPath.reset();
        mPath.moveTo(circleCenterPoint.x, circleCenterPoint.y);
        mPath.lineTo(tailCenterPointLeft.x, tailCenterPointLeft.y);
        mPath.lineTo(tailCenterPointRight.x, tailCenterPointRight.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 画等腰梯形
     *
     * @param canvas
     * @param circlePoint1
     * @param circlePoint2
     * @param circleRadius1
     * @param circleRadius2
     * @param fishAngle
     */
    private void drawIsoscelesTrapezoidal(Canvas canvas, PointF circlePoint1, PointF circlePoint2, float circleRadius1, float circleRadius2, float fishAngle) {
        PointF circlePoint1Left = calculatePoint(circlePoint1, circleRadius1, fishAngle + 90);
        PointF circlePoint1Right = calculatePoint(circlePoint1, circleRadius1, fishAngle - 90);
        PointF circlePoint2Left = calculatePoint(circlePoint2, circleRadius2, fishAngle + 90);
        PointF circlePoint2Right = calculatePoint(circlePoint2, circleRadius2, fishAngle - 90);
        mPath.reset();
        mPath.moveTo(circlePoint1Left.x, circlePoint1Left.y);
        mPath.lineTo(circlePoint1Right.x, circlePoint1Right.y);
        mPath.lineTo(circlePoint2Right.x, circlePoint2Right.y);
        mPath.lineTo(circlePoint2Left.x, circlePoint2Left.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * @param canvas
     * @param circlePoint1
     * @param circlePoint2
     * @param circleRadius1
     * @param circleRadius2
     * @param fishAngle
     */
    private void drawBody(Canvas canvas, PointF circlePoint1, PointF circlePoint2, float circleRadius1, float circleRadius2, int fishAngle) {
        PointF circlePoint1Left = calculatePoint(circlePoint1, circleRadius1, fishAngle + 90);
        PointF circlePoint1Right = calculatePoint(circlePoint1, circleRadius1, fishAngle - 90);
        PointF circlePoint2Left = calculatePoint(circlePoint2, circleRadius2, fishAngle + 90);
        PointF circlePoint2Right = calculatePoint(circlePoint2, circleRadius2, fishAngle - 90);
        PointF circlePointControlLeft = calculatePoint(circlePoint1, FISH_BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF circlePointControlRight = calculatePoint(circlePoint1, FISH_BODY_LENGTH * 0.56f, fishAngle - 130);
        mPath.reset();
        mPath.moveTo(circlePoint1Left.x, circlePoint1Left.y);
        mPath.quadTo(circlePointControlLeft.x, circlePointControlLeft.y, circlePoint2Left.x, circlePoint2Left.y);
        mPath.lineTo(circlePoint2Right.x, circlePoint2Right.y);
        mPath.quadTo(circlePointControlRight.x, circlePointControlRight.y, circlePoint1Right.x, circlePoint1Right.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制鱼鳍
     *
     * @param canvas     画布
     * @param headPoint  参照点
     * @param fishAngle  鱼变换的角度
     * @param isRightFin 是否为右侧鱼鳍
     */
    private void drawFins(Canvas canvas, PointF headPoint, float fishAngle, boolean isRightFin) {
        //起点
        PointF finsStart = calculatePoint(headPoint, FISH_FINS_LENGTH, isRightFin ? fishAngle - 110 : fishAngle + 110);
        PointF finsControl = calculatePoint(finsStart, FISH_FINS_LENGTH * 1.8f, isRightFin ? fishAngle - 115 : fishAngle + 115);
        PointF finsEnd = calculatePoint(finsStart, FISH_FINS_LENGTH, isRightFin ? fishAngle - 180 : fishAngle + 180);
        //首先要清除下原来的路径，这样才能保证绘制路径干净
        mPath.reset();
        mPath.moveTo(finsStart.x, finsStart.y);
        mPath.quadTo(finsControl.x, finsControl.y, finsEnd.x, finsEnd.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 用到的是sin和cos 三角函数
     * 已知一个点的坐标，两点之间的直线距离，两个点的夹角（x轴向右为正，逆时针旋转为旋转方向）
     *
     * @param startPoint 起点
     * @param length     线长度
     * @param angle      当前鱼的朝向
     * @return
     */
    private PointF calculatePoint(PointF startPoint, float length, float angle) {
        // x坐标
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * length);
        // y坐标
        float deltaY = (float) (Math.sin(Math.toRadians(angle - 180)) * length);
        //起始点的坐标+相对位移=新的坐标点
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    /**
     * @param alpha 设置画笔的alpha值
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * 设置颜色
     *
     * @param colorFilter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * 不透明度
     *
     * @return
     */
    @Override
    public int getOpacity() {
        //设置为半透明状态
        return PixelFormat.TRANSLUCENT;
    }
}
