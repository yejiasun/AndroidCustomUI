package com.tengteng.ui.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 文字绘制，可以切换颜色，实质上是两个不同颜色的文字叠加，再结合动画实现的。
 * @author yejiasun
 * @date Create on 11/25/22
 */

class CustomTextView extends AppCompatTextView {
    private float mPercent = 0.0f;

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float percent) {
        this.mPercent = percent;
        System.out.println("tt" + "percent=" + percent);
        invalidate();
    }

    public CustomTextView(@NonNull Context context) {
        super(context);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBaseLineX(canvas);
        drawBaseLineY(canvas);
        drawText(canvas);
        drawText2(canvas);


    }

    private void drawBaseLineY(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2 + 3, getHeight(), paint);
    }

    private void drawBaseLineX(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2 + 3, paint);
    }

    private void drawText(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        String text = "TengTeng";
        float textWidth = paint.measureText(text);
        float x = getWidth() / 2 - textWidth / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float y = getHeight() / 2 - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;
        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }

    private void drawText2(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        String text = "TengTeng";
        float textWidth = paint.measureText(text);
        float x = getWidth() / 2 - textWidth / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float y = getHeight() / 2 - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;
        float move_x = x + mPercent * textWidth;
        System.out.println("tt" + "move_x=" + move_x);
        Rect rect = new Rect((int) x, 0, (int) move_x, getHeight());
        canvas.clipRect(rect);
        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }
}
