package com.libra.superrecyclerview.expand;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by libra on 2017/7/1.
 */

public abstract class ExpandAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;
    private List<ExpandItem> mList;

    public ExpandAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isParentViewType(viewType)) {
            VH pvh = onCreateParentViewHolder(parent, viewType);
            return pvh;
        } else {
            VH cvh = onCreateChildViewHolder(parent, viewType);
            return cvh;
        }
    }

    protected abstract VH onCreateChildViewHolder(ViewGroup parent, int viewType);

    protected abstract VH onCreateParentViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindParentViewHolder(VH holder, Object object, boolean isExpaned);

    protected abstract void onBindChildViewHolder(VH holder, Object object);

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final ExpandItem expandItem = mList.get(position);
        if (expandItem.isParent()) {
            onBindParentViewHolder(holder, expandItem.getParent().getParent(),
                                   expandItem.isExpanded());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateExpanded(expandItem, position);
                }
            });
        } else {
            onBindChildViewHolder(holder, expandItem.getChild());
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public boolean isParentViewType(int viewType) {
        return viewType == TYPE_PARENT;
    }

    @Override
    public int getItemViewType(int position) {
        final ExpandItem expandItem = mList.get(position);
        return expandItem.isParent() ? TYPE_PARENT : TYPE_CHILD;
    }

    public void setList(List groupList, Map<Object, List> groupMap) {
        this.mList.clear();
        for (int i = 0; i < groupList.size(); i++) {
            Object o = groupList.get(i);
            List childList = groupMap.get(o);
            Parent parent = new Parent(o, childList);
            ExpandItem item = new ExpandItem(parent, null, true);
            this.mList.add(item);
        }
        notifyDataSetChanged();
    }

    private void updateExpanded(ExpandItem item, int position) {
        if (item.isExpanded()) {
            item.setExpanded(false);
            List<ExpandItem> wrappedChildList = item.getWrappedChildList();
            if (wrappedChildList != null) {
                mList.removeAll(wrappedChildList);
                notifyDataSetChanged();
            }
        } else {
            item.setExpanded(true);
            List<ExpandItem> wrappedChildList = item.getWrappedChildList();
            if (wrappedChildList != null) {
                mList.addAll(position + 1, wrappedChildList);
                notifyDataSetChanged();
            }
        }

    }

}
