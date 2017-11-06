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
    private List<Object> mGroupList;

    public ExpandAdapter() {
        mList = new ArrayList<>();
        mGroupList = new ArrayList<>();
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

    protected abstract void onBindParentViewHolder(VH holder, Object object, boolean isExpaned,
            int position, int size);

    public abstract void onBindChildViewHolder(VH holder, ExpandItem item);

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final ExpandItem expandItem = mList.get(position);
        final List<ExpandItem> wrappedChildList = expandItem.getWrappedChildList();
        int size = 0;
        if (wrappedChildList != null) {
            size = wrappedChildList.size();
        }
        if (expandItem.isParent()) {
            onBindParentViewHolder(holder, expandItem.getParent().getParent(),
                                   expandItem.isExpanded(), position, size);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wrappedChildList != null && wrappedChildList.size() > 0) {
                        updateExpanded(expandItem, position);
                    }
                }
            });
        } else {
            onBindChildViewHolder(holder, expandItem);
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
        mGroupList.clear();
        if (groupList != null) {
            mGroupList.addAll(groupList);
            for (int i = 0; i < groupList.size(); i++) {
                Object o = groupList.get(i);
                List childList = groupMap.get(o);
                Parent parent = new Parent(o, childList);
                ExpandItem item = new ExpandItem(parent, null, true);
                this.mList.add(item);

                if (expandGroupIndex.contains(i)) {
                    item.setExpanded(true);
                    List<ExpandItem> wrappedChildList = item.getWrappedChildList();
                    if (wrappedChildList != null && wrappedChildList.size() > 0) {
                        mList.addAll(wrappedChildList);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private void updateExpanded(ExpandItem item, int position) {
        if (item.isExpanded()) {
            if (item.isParent()) {
                int index = mGroupList.indexOf(item.getParent().getParent());
                if (index != -1) {
                    expandGroupIndex.remove(Integer.valueOf(index));
                }
            }
            item.setExpanded(false);
            List<ExpandItem> wrappedChildList = item.getWrappedChildList();
            if (wrappedChildList != null) {
                mList.removeAll(wrappedChildList);
                notifyDataSetChanged();
            }
        } else {
            if (item.isParent()) {
                int index = mGroupList.indexOf(item.getParent().getParent());
                if (index != -1) {
                    expandGroupIndex.add(index);
                }
            }
            item.setExpanded(true);
            List<ExpandItem> wrappedChildList = item.getWrappedChildList();
            if (wrappedChildList != null && wrappedChildList.size() > 0) {
                mList.addAll(position + 1, wrappedChildList);
                notifyDataSetChanged();
            }
        }

    }

    private ArrayList<Integer> expandGroupIndex = new ArrayList<>();

}
