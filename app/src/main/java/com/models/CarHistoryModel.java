package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saferoad-Dev1 on 9/17/2017.
 */

public class CarHistoryModel {

    @SerializedName("VehicleID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Speed")
    @Expose
    private float speed;
    @SerializedName("TotalMileage")
    @Expose
    private Double totalMileage;
    @SerializedName("TotalWorkingHours")
    @Expose
    private Double totalWorkingHours;
    @SerializedName("Direction")
    @Expose
    private Integer direction;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
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
    @SerializedName("EngineStatus")
    @Expose
    private Boolean engineStatus;
    @SerializedName("RecordTime")
    @Expose
    private Object recordTime;
    @SerializedName("Status")
    @Expose
    private Object status;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Double getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(Double totalMileage) {
        this.totalMileage = totalMileage;
    }

    public Double getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(Double totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    public Boolean getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(Boolean engineStatus) {
        this.engineStatus = engineStatus;
    }

    public Object getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Object recordTime) {
        this.recordTime = recordTime;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
