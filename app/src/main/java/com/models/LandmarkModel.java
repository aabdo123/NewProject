package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LandmarkModel {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("POIName")
    @Expose
    private String pOIName;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Icon")
    @Expose
    private String icon;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPOIName() {
        return pOIName;
    }

    public void setPOIName(String pOIName) {
        this.pOIName = pOIName;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
