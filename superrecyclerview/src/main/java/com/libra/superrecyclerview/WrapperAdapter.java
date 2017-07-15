package com.libra.superrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.libra.superrecyclerview.helper.ItemTouchHelperAdapter;

public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {
    protected static final int HEADER = Integer.MIN_VALUE;
    protected static final int FOOTER = Integer.MAX_VALUE;
    private Context context;
    private RecyclerView.Adapter mAdapter;
    private LinearLayout mHeaderContainer;
    private LinearLayout mFooterContainer;
    private ItemTouchHelperAdapter itemTouchHelperAdapter;

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            WrapperAdapter.this.notifyDataSetChanged();
        }


        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            WrapperAdapter.this.notifyItemRangeChanged(positionStart + 1, itemCount);
        }


        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            WrapperAdapter.this.notifyItemRangeChanged(positionStart + 1, itemCount, payload);
        }


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            WrapperAdapter.this.notifyItemRangeInserted(positionStart + 1, itemCount);
        }


        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            WrapperAdapter.this.notifyItemRangeRemoved(positionStart + 1, itemCount);
        }


        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            WrapperAdapter.this.notifyItemMoved(fromPosition, toPosition);
        }
    };


    public WrapperAdapter(Context context, RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        this.context = context;

        mAdapter.registerAdapterDataObserver(mObserver);
        ensureHeaderViewContainer();
        ensureFooterViewContainer();
    }

    public LinearLayout getHeaderContainer() {
        ensureHeaderViewContainer();
        return mHeaderContainer;
    }


    public LinearLayout getFooterContainer() {
        ensureFooterViewContainer();
        return mFooterContainer;
    }

    /**
     * 添加列表头部视图
     */
    public void addHeaderView(View headerView) {
        ensureHeaderViewContainer();
        mHeaderContainer.addView(headerView);
        notifyItemChanged(0);
    }

    /**
     * 添加列表底部视图
     */
    public void addFooterView(View footerView) {
        ensureFooterViewContainer();
        mFooterContainer.addView(footerView);
        notifyItemChanged(getItemCount() - 1);
    }

    private void ensureHeaderViewContainer() {
        if (mHeaderContainer == null) {
            mHeaderContainer = new LinearLayout(context);
            mHeaderContainer.setTag("header");
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                  ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    private void ensureFooterViewContainer() {
        if (mFooterContainer == null) {
            mFooterContainer = new LinearLayout(context);
            mFooterContainer.setTag("footer");
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                  ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(wrapperAdapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp =
                        (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }


    private boolean isFullSpanType(int type) {
        return type == HEADER || type == FOOTER;
    }


    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (0 < position && position < mAdapter.getItemCount() + 1) {
            return mAdapter.getItemViewType(position - 1);
        } else if (position == mAdapter.getItemCount() + 1) {
            return FOOTER;
        }

        throw new IllegalArgumentException("Wrong type! Position = " + position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderContainerViewHolder(mHeaderContainer);
        } else if (viewType == FOOTER) {
            return new FooterContainerViewHolder(mFooterContainer);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (0 < position && position < mAdapter.getItemCount() + 1) {
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == 0 || toPosition == getItemCount() - 1) {
            return false;
        }
        if (itemTouchHelperAdapter != null) {
            boolean flag = itemTouchHelperAdapter.onItemMove(fromPosition - 1, toPosition - 1);
            notifyItemMoved(fromPosition, toPosition);
            return flag;
        }
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        if (position == 0 || position == getItemCount() - 1) {
            return;
        }
        if (itemTouchHelperAdapter != null) {
            itemTouchHelperAdapter.onItemDismiss(position - 1);
        }
    }

    public void setItemTouchHelperAdapter(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    public static class HeaderContainerViewHolder extends RecyclerView.ViewHolder {

        public HeaderContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterContainerViewHolder extends RecyclerView.ViewHolder {

        public FooterContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

}
