package com.models;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyClusterItem implements ClusterItem {

    public final String name;
    public final float direction;
    public final BitmapDescriptor markerIcon;
    private final LatLng mPosition;

    public MyClusterItem(LatLng position, String name, float direction, BitmapDescriptor markerIcon) {
        this.name = name;
        this.direction = direction;
        this.markerIcon = markerIcon;
        mPosition = position;
    }

    public BitmapDescriptor getMarkerIcon() {
        return markerIcon;
    }

    public float getDirection(){return direction;};

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}