package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saferoad-Dev1 on 8/30/2017.
 */

class _VehiclesModel {

    @SerializedName("VehicleID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Speed")
    @Expose
    private Integer speed;
    @SerializedName("TotalMileage")
    @Expose
    private Integer totalMileage;
    @SerializedName("TotalWorkingHours")
    @Expose
    private Integer totalWorkingHours;
    @SerializedName("Direction")
    @Expose
    private Integer direction;
    @SerializedName("Latitude")
    @Expose
    private Integer latitude;
    @SerializedName("Longitude")
    @Expose
    private Integer longitude;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("StreetSpeed")
    @Expose
    private Integer streetSpeed;
    @SerializedName("VehicleStatus")
    @Expose
    private String vehicleStatus;
    @SerializedName("RecordDateTime")
    @Expose
    private String recordDateTime;
    @SerializedName("IsOnline")
    @Expose
    private Boolean isOnline;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(Integer totalMileage) {
        this.totalMileage = totalMileage;
    }

    public Integer getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(Integer totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStreetSpeed() {
        return streetSpeed;
    }

    public void setStreetSpeed(Integer streetSpeed) {
        this.streetSpeed = streetSpeed;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getRecordDateTime() {
        return recordDateTime;
    }

    public void setRecordDateTime(String recordDateTime) {
        this.recordDateTime = recordDateTime;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

}
