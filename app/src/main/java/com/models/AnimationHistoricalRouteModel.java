package com.models;

import com.google.android.gms.maps.model.LatLng;

public class AnimationHistoricalRouteModel {


    private Double latitude;
    private Double longitude;
    private String vehicleStatus;
    private LatLng latLng;
    private Double speed;
    private String dateTime;

    public Double getLatitude() {
        return latitude;
    }

    public AnimationHistoricalRouteModel(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AnimationHistoricalRouteModel(Double latitude, Double longitude, String vehicleStatus,Double speed, String dateTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicleStatus = vehicleStatus;
        this.speed = speed;
        this.dateTime = dateTime;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getSpeed() {
        return speed;
    }

    public String getDateTime() {
        return dateTime;
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
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
