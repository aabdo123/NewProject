package com.models;

import android.graphics.drawable.Drawable;

public class LandMarkerTypeModel {

    private int addId;
    private int landmarkId;
    private String name;
    private Drawable icon;
    private boolean isSelected;

    public LandMarkerTypeModel(int addId, int landmarkId,String name, Drawable icon) {
        this.addId = addId;
        this.landmarkId = landmarkId;
        this.name = name;
        this.icon = icon;
    }

    public int getAddId() {
        return addId;
    }

    public int getLandmarkId() {
        return landmarkId;
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
