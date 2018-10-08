package com.models;

import android.graphics.drawable.Drawable;

public class MapStyleModel {

    private int id;
    private String name;
    private Drawable icon;
    private boolean isSelected;

    public MapStyleModel(int id, String name, Drawable icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
