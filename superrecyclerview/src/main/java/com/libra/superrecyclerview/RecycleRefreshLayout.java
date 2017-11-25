package com.libra.superrecyclerview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by libra on 2017/11/10.
 */

public class RecycleRefreshLayout extends SwipeRefreshLayout {
    public RecycleRefreshLayout(Context context) {
        super(context);
    }

    public RecycleRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        return canChildScrollUp(this);
    }

    public boolean canChildScrollUp(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View temp = v.getChildAt(i);
            if (temp instanceof RecyclerView) {
                return canRecycleViewScroll((RecyclerView) temp);
            }
            return ViewCompat.canScrollVertically(temp, -1);
        }
        return false;
    }

    public boolean canRecycleViewScroll(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() != 0;

    }
}
