package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saferoad-Dev1 on 9/11/2017.
 */

public class AlarmNotificationModel {

    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Serial")
    @Expose
    private String serial;
    @SerializedName("NotificationText")
    @Expose
    private String notificationText;
    @SerializedName("Datetime")
    @Expose
    private String datetime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
