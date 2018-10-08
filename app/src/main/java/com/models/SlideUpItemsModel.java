package com.models;

import android.graphics.drawable.Drawable;

public class SlideUpItemsModel {

    private int id;
    private boolean addButtonVisible;
    private boolean showButtonVisible;
    private int color;
    private String title;
    private String des;
    private Drawable drawableIcon;
    private Drawable drawableButton;
    private boolean isShowClicked;

    public SlideUpItemsModel(int id, boolean addButtonVisible, boolean showButtonVisible,int color, String title, String des, Drawable drawableIcon, Drawable drawableButton) {
        this.id = id;
        this.addButtonVisible = addButtonVisible;
        this.showButtonVisible = showButtonVisible;
        this.color = color;
        this.title = title;
        this.des = des;
        this.drawableIcon = drawableIcon;
        this.drawableButton = drawableButton;
    }

    public int getId() {
        return id;
    }

    public boolean isAddButtonVisible() {
        return addButtonVisible;
    }

    public boolean isShowButtonVisible() {
        return showButtonVisible;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public Drawable getDrawableIcon() {
        return drawableIcon;
    }

    public Drawable getDrawableButton() {
        return drawableButton;
    }

    public boolean isShowClicked() {
        return isShowClicked;
    }

    public void setShowClicked(boolean showClicked) {
        isShowClicked = showClicked;
    }
}
