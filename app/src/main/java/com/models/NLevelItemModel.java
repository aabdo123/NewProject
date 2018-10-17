package com.models;

/**
 * Created by sadra on 7/29/17.
 */


import android.view.View;

import com.views.NLevelListItem;
import com.views.NLevelView;

public class NLevelItemModel implements NLevelListItem {

    private Object wrappedObject;
    private NLevelItemModel parent;
    private NLevelView nLevelView;
    private boolean isExpanded = false;
    private boolean isChecked = false;
    private int position;

    public NLevelItemModel(int position, Object wrappedObject, NLevelItemModel parent, NLevelView nLevelView) {
        this.wrappedObject = wrappedObject;
        this.parent = parent;
        this.nLevelView = nLevelView;
        this.position = position;
    }

    public Object getWrappedObject() {
        return wrappedObject;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }
    @Override
    public NLevelListItem getParent() {
        return parent;
    }
    @Override
    public View getView() {
        return nLevelView.getView(this);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void makeItState(boolean isExpanded, boolean isChecked) {
       this.isExpanded = isExpanded;
        this.isChecked = isChecked;
    }

    @Override
    public void toggle() {
        isExpanded = !isExpanded;
        isChecked = !isChecked;
    }
}