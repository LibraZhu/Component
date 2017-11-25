package com.libra.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.libra.superrecyclerview.WrapperAdapter;

/**
 * Created by libra on 2017/6/21.
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mDividerHeight;

    public DividerGridItemDecoration(Context context, Drawable divider, int dividerHeight) {
        mDivider = divider;
        mDividerHeight = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getLayoutManager().getItemCount();
        int spanCount = getSpanCount(parent);
        if (parent.getAdapter() instanceof WrapperAdapter) {
            for (int i = 1; i < childCount - 1; i++) {
                final View child = parent.getLayoutManager().findViewByPosition(i);
                if (child==null){
                    continue;
                }
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin + mDividerHeight;
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDividerHeight;
                if (!isLastRaw(parent, i - 1, spanCount, childCount - 2)) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getLayoutManager().findViewByPosition(i);
                if (child==null){
                    continue;
                }
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right =
                        child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDividerHeight;
                if (!isLastRaw(parent, i, spanCount, childCount)) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getLayoutManager().getItemCount();
        int spanCount = getSpanCount(parent);
        if (parent.getAdapter() instanceof WrapperAdapter) {
            for (int i = 1; i < childCount - 1; i++) {
                final View child = parent.getLayoutManager().findViewByPosition(i);
                if (child==null){
                    continue;
                }
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDividerHeight;
                if (!isLastColum(parent, i - 1, spanCount, childCount - 2)) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getLayoutManager().findViewByPosition(i);
                if (child==null){
                    continue;
                }
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDividerHeight;
                if (!isLastColum(parent, i, spanCount, childCount)) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }

    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int rows = childCount % spanCount == 0 ? childCount / spanCount
                    : childCount / spanCount + 1;
            if (pos >= (rows - 1) * spanCount)// 如果是最后一行，则不需要绘制底部
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        int index = holder.getLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (parent.getAdapter() instanceof WrapperAdapter) {
            if (index > 0 && index < childCount - 1) {
                outRect.bottom = mDividerHeight;
                outRect.right = mDividerHeight;
                if (isLastRaw(parent, index - 1, spanCount, childCount - 2)) {
                    outRect.bottom = 0;
                }
                if (isLastColum(parent, index - 1, spanCount, childCount - 2)) {
                    outRect.right = 0;
                }
            }
        } else {
            outRect.bottom = mDividerHeight;
            outRect.right = mDividerHeight;
            if (isLastRaw(parent, index, spanCount, childCount)) {
                outRect.bottom = 0;
            }
            if (isLastColum(parent, index, spanCount, childCount)) {
                outRect.right = 0;
            }
        }

    }

}
