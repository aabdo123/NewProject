package com.models;

import com.google.android.gms.maps.model.LatLng;

public class LatLogModel {

    private LatLng latLng;
    private int itemViewType;
    private LocationLocateModel locationLocateModel;

    public LatLogModel(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public LocationLocateModel getLocationLocateModel() {
        return locationLocateModel;
    }

    public void setLocationLocateModel(LocationLocateModel locationLocateModel) {
        this.locationLocateModel = locationLocateModel;
    }
}
