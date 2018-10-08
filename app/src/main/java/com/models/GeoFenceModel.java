package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saferoad-Dev1 on 10/24/2017.
 */

public class GeoFenceModel {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("GeofenceName")
    @Expose
    private String geofenceName;
    @SerializedName("GeofencePath")
    @Expose
    private Object geofencePath;
    @SerializedName("GeofenceCenterPoint")
    @Expose
    private String geofenceCenterPoint;
    @SerializedName("GeofenceRadius")
    @Expose
    private Integer geofenceRadius;
    @SerializedName("GeofenceBounds")
    @Expose
    private Object geofenceBounds;
    @SerializedName("UserID")
    @Expose
    private Object userID;
    @SerializedName("Speed")
    @Expose
    private Object speed;
    @SerializedName("IsIn")
    @Expose
    private Object isIn;
    @SerializedName("IsOut")
    @Expose
    private Object isOut;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    public Object getGeofencePath() {
        return geofencePath;
    }

    public void setGeofencePath(Object geofencePath) {
        this.geofencePath = geofencePath;
    }

    public String getGeofenceCenterPoint() {
        return geofenceCenterPoint;
    }

    public void setGeofenceCenterPoint(String geofenceCenterPoint) {
        this.geofenceCenterPoint = geofenceCenterPoint;
    }

    public Integer getGeofenceRadius() {
        return geofenceRadius;
    }

    public void setGeofenceRadius(Integer geofenceRadius) {
        this.geofenceRadius = geofenceRadius;
    }

    public Object getGeofenceBounds() {
        return geofenceBounds;
    }

    public void setGeofenceBounds(Object geofenceBounds) {
        this.geofenceBounds = geofenceBounds;
    }

    public Object getUserID() {
        return userID;
    }

    public void setUserID(Object userID) {
        this.userID = userID;
    }

    public Object getSpeed() {
        return speed;
    }

    public void setSpeed(Object speed) {
        this.speed = speed;
    }

    public Object getIsIn() {
        return isIn;
    }

    public void setIsIn(Object isIn) {
        this.isIn = isIn;
    }

    public Object getIsOut() {
        return isOut;
    }

    public void setIsOut(Object isOut) {
        this.isOut = isOut;
    }
}
