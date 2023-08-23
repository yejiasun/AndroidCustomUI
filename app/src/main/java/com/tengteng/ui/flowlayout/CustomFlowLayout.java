package com.tengteng.ui.flowlayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class CustomFlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private int mHorizontalSpacing = dp2px(16); //每个item横向间距
    private int mVerticalSpacing = dp2px(8); //每个item横向间距

    private List<List<View>> allLines = new ArrayList<>(); // 记录所有的行，一行一行的存储，用于layout
    List<Integer> lineHeights = new ArrayList<>(); // 记录每一行的行高，用于layout


    public CustomFlowLayout(Context context) {
        super(context);
//        initMeasureParams();
    }

    //反射
    public CustomFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initMeasureParams();
    }

    //主题style
    public CustomFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initMeasureParams();
    }
    //四个参数 自定义属性

    private void clearMeasureParams() {
        allLines.clear();
        lineHeights.clear();
    }

    //度量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        clearMeasureParams();//内存 抖动
        //先度量孩子
        int childCount = getChildCount();//子view的个数
        int paddingLeft = getPaddingLeft();//当前view左边的留白
        int paddingRight = getPaddingRight();//当前view右边的留白
        int paddingTop = getPaddingTop();//当前view上面的留白
        int paddingBottom = getPaddingBottom();//当前view下面的留白

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);  //当前view自身的宽，解析的父view给我的宽度 widthMeasureSpec
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec); // 当前view自身的高度，解析的父view给我的高度heightMeasureSpec

        List<View> lineViews = new ArrayList<>(); //保存一行中的所有的子view
        int lineWidthUsed = 0; //记录这行已经使用了多宽的size
        int lineHeight = 0; // 一行的行高

        int parentNeededWidth = 0;  // measure过程中，子View要求的父ViewGroup的宽
        int parentNeededHeight = 0; // measure过程中，子View要求的父ViewGroup的高

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);//获取子view

            LayoutParams childLP = childView.getLayoutParams();//获取子view的LP属性
            if (childView.getVisibility() != View.GONE) {
                //将layoutParams转变成为 measureSpec
                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLP.width);//子view真实的测绘宽度（其中的padding入参表示，去除已经被用掉了的那些边边角角宽度））
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLP.height);//子view真实的测绘高度（其中的padding入参表示，去除已经被用掉了的那些边边角角高度））
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);//子view测绘
                //获取子view的测量宽高
                int childMesauredWidth = childView.getMeasuredWidth();
                int childMeasuredHeight = childView.getMeasuredHeight();

                //如果需要换行
                if (childMesauredWidth + lineWidthUsed + mHorizontalSpacing > selfWidth) {

                    //一旦换行，我们就可以判断当前行需要的宽和高了，所以此时要记录下来
                    allLines.add(lineViews);
                    lineHeights.add(lineHeight);
                    parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);//当前view所需的宽度，总是两相比较选大的那个
                    parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;//当前view所需的高度，=叠加之前记录的+每行线的高度+上下空出来的高度
                    //清空
                    lineViews = new ArrayList<>();
                    lineWidthUsed = 0;
                    lineHeight = 0;
                }
                // view 是分行layout的，所以要记录每一行有哪些view，这样可以方便layout布局
                lineViews.add(childView);
                //每行都会有自己的宽和高
                lineWidthUsed = lineWidthUsed + childMesauredWidth + mHorizontalSpacing;
                lineHeight = Math.max(lineHeight, childMeasuredHeight);

                //处理最后一行数据（因为上面的parentNeededWidth和parentNeededHeight是在换行条件里进行的，最后一行没有）
                if (i == childCount - 1) {
                    allLines.add(lineViews);
                    lineHeights.add(lineHeight);
                    parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
                    parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                }

            }
        }


        //再度量自己,保存
        //根据子View的度量结果，来重新度量自己ViewGroup
        // 作为一个ViewGroup，它自己也是一个View,它的大小也需要根据它的父亲给它提供的宽高来度量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeededHeight;
        setMeasuredDimension(realWidth, realHeight);
    }

    //布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineCount = allLines.size();

        int curL = getPaddingLeft();
        int curT = getPaddingTop();

        for (int i = 0; i < lineCount; i++) {
            List<View> lineViews = allLines.get(i);

            int lineHeight = lineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                int left = curL;
                int top = curT;

//                int right = left + view.getWidth();
//                int bottom = top + view.getHeight();

                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                curL = right + mHorizontalSpacing;
            }
            curT = curT + lineHeight + mVerticalSpacing;
            curL = getPaddingLeft();
        }

    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
