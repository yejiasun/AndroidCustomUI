package com.tengteng.ui.recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.tengteng.ui.DpUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义分割线，可以实现展示头部并且吸顶
 *
 * @author yejiasun
 * @date Create on 12/14/22
 */

class CustomDecoration extends RecyclerView.ItemDecoration {
    private Context context;

    private int groupHeadHeight;

    private Paint headPaint;
    private Paint textPaint;

    public CustomDecoration(Context context, int groupHeadHeight) {
        this.context = context;
        this.groupHeadHeight = DpUtils.dp2px(context, groupHeadHeight);
        initPaint();
    }

    private void initPaint() {
        headPaint = new Paint();
        headPaint.setColor(Color.YELLOW);
        headPaint.setStyle(Paint.Style.FILL);
        headPaint.setStrokeWidth(3);
        textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(3);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //这里的布局是画在itemView的同一层
        if (parent.getAdapter() instanceof CustomAdapter) {
            CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
            //获得总的item个数
            int itemCount = adapter.getItemCount();
            //循环每个item，如果是group头，则添加一个头部区域，并绘制
            for (int i = 0; i < itemCount; i++) {
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                //获取第i个view
                View view = parent.getChildAt(i);
                //view所在的
                int position = parent.getChildLayoutPosition(view);
                System.out.println("tt position=" + position);
                if (position >= 0) {
                    if (adapter.isGroupHead(position)) {
                        c.drawRect(left, view.getTop() - groupHeadHeight, right, view.getTop(), headPaint);
                        String groupName = adapter.getGroupName(position);
                        System.out.println("tt groupName=" + groupName);
                        c.drawText(groupName, left + 20, view.getTop() - groupHeadHeight / 2, textPaint);
                    } else {
                        c.drawRect(left, view.getTop() - 1, right, view.getTop(), headPaint);
                    }
                }

            }
        }

    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //这里的布局是覆盖itemView的上面
        if (parent.getAdapter() instanceof CustomAdapter) {
            CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
            int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            String groupName = adapter.getGroupName(position);
            System.out.println("tt onDrawOver groupName=" + groupName);
            View view = parent.findViewHolderForAdapterPosition(position).itemView;
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = parent.getPaddingTop();
            if (adapter.isGroupHead(position + 1)) {
                int bottom = Math.min(groupHeadHeight, view.getBottom());
                c.drawRect(left, top, right, top+bottom, headPaint);
                c.drawText(groupName, left + 20, top+bottom - groupHeadHeight / 2, textPaint);
            } else {
                c.drawRect(left, top, right, top + groupHeadHeight, headPaint);
                c.drawText(groupName, left + 20, top + groupHeadHeight - groupHeadHeight / 2, textPaint);
            }
        }

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getAdapter() instanceof CustomAdapter) {
            //如果是grouphead 则预留头空间，否则展示一个分割线的空间大小
            outRect.set(0, ((CustomAdapter) parent.getAdapter()).isGroupHead(parent.getChildLayoutPosition(view)) ? groupHeadHeight : 1, 0, 0);

        }
    }
}
