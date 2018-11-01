package com.models;

import com.google.android.gms.maps.model.LatLng;

public class AnimationHistoricalRouteModel {


    private Double latitude;
    private Double longitude;
    private String vehicleStatus;
    private LatLng latLng;

    public Double getLatitude() {
        return latitude;
    }

    public AnimationHistoricalRouteModel(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AnimationHistoricalRouteModel(Double latitude, Double longitude, String vehicleStatus) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicleStatus = vehicleStatus != null ? vehicleStatus : "0";
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getVehicleStatus() {
        return vehicleStatus != null ? vehicleStatus : "0";
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
