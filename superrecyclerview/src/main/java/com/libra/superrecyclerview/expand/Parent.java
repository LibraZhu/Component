package com.libra.superrecyclerview.expand;

import java.util.List;

/**
 * Created by libra on 2017/7/1.
 */

public class Parent {
    private Object parent;
    private List childList;
    private boolean expanded;

    public Parent(Object parent, List childrens) {
        this.parent = parent;
        this.childList = childrens;
    }

    public Object getParent() {
        return parent;
    }

    public List getChildList() {
        return childList;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public void setChildList(List childList) {
        this.childList = childList;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
