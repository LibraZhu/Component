package com.libra.superrecyclerview.expand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libra on 2017/7/1.
 */

public class ExpandItem {
    private Parent parent;
    private Object child;
    private boolean expanded;
    private boolean isParent;
    private boolean isFirstChild = false;
    private boolean isLastChild = false ;

    private List<ExpandItem> mWrappedChildList;

    public ExpandItem(Parent parent, Object child) {
        this.parent = parent;
        this.child = child;
        this.isParent = false;
    }

    public ExpandItem(Parent parent, Object child, boolean isParent) {
        this.parent = parent;
        this.child = child;
        this.isParent = isParent;
        mWrappedChildList = generateChildItemList(parent);
    }

    public Parent getParent() {
        return parent;
    }

    public Object getChild() {
        return child;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
        mWrappedChildList = generateChildItemList(parent);
    }

    public void setChild(Object child) {
        this.child = child;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    private List<ExpandItem> generateChildItemList(Parent parent) {
        List<ExpandItem> childItemList = new ArrayList<>();

        for (Object child : parent.getChildList()) {
            childItemList.add(new ExpandItem(parent, child));
        }
        if(!childItemList.isEmpty()) {
            childItemList.get(0).isFirstChild = true;
            childItemList.get(childItemList.size() - 1).isLastChild = true;
        }


        return childItemList;
    }

    public List<ExpandItem> getWrappedChildList() {
        return mWrappedChildList;
    }

    public boolean isFirstChild() {
        return isFirstChild;
    }

    public boolean isLastChild() {
        return isLastChild;
    }
}
