package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saferoad-Dev1 on 9/13/2017.
 */

public class SignalRModel {

    @SerializedName("H")
    @Expose
    private String h;
    @SerializedName("M")
    @Expose
    private String m;
    @SerializedName("A")
    @Expose
    private List<A> a = null;

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public List<A> getA() {
        return a;
    }

    public void setA(List<A> a) {
        this.a = a;
    }

    public class A {
        @SerializedName("VehicleDisplayName")
        @Expose
        private String vehicleDisplayName;
        @SerializedName("PlateNumber")
        @Expose
        private String plateNumber;
        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("DriverName")
        @Expose
        private String driverName;
        @SerializedName("SimCardNumber")
        @Expose
        private String simCardNumber;
        @SerializedName("Temperature")
        @Expose
        private String temperature;
        @SerializedName("LocationID")
        @Expose
        private Integer locationID;
        @SerializedName("VehicleID")
        @Expose
        private Integer vehicleID;
        @SerializedName("RecordDateTime")
        @Expose
        private String recordDateTime;
        @SerializedName("Longitude")
        @Expose
        private Double longitude;
        @SerializedName("Latitude")
        @Expose
        private Double latitude;
        @SerializedName("Speed")
        @Expose
        private Double speed;
        @SerializedName("Direction")
        @Expose
        private Double direction;
        @SerializedName("Serial")
        @Expose
        private String serial;
        @SerializedName("IsValidRecord")
        @Expose
        private Boolean isValidRecord;
        @SerializedName("Mileage")
        @Expose
        private Double mileage;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("StreetSpeed")
        @Expose
        private Integer streetSpeed;
        @SerializedName("VehicleStatus")
        @Expose
        private Integer vehicleStatus;
        @SerializedName("VehicleStatusText")
        @Expose
        private String vehicleStatusText;
        @SerializedName("WorkingHours")
        @Expose
        private Double workingHours;
        @SerializedName("Fuel")
        @Expose
        private String fuel;
        @SerializedName("EngineStatus")
        @Expose
        private Boolean engineStatus;

        public String getVehicleDisplayName() {
            return vehicleDisplayName;
        }

        public void setVehicleDisplayName(String vehicleDisplayName) {
            this.vehicleDisplayName = vehicleDisplayName;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getSimCardNumber() {
            return simCardNumber;
        }

        public void setSimCardNumber(String simCardNumber) {
            this.simCardNumber = simCardNumber;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public Integer getLocationID() {
            return locationID;
        }

        public void setLocationID(Integer locationID) {
            this.locationID = locationID;
        }

        public Integer getVehicleID() {
            return vehicleID;
        }

        public void setVehicleID(Integer vehicleID) {
            this.vehicleID = vehicleID;
        }

        public String getRecordDateTime() {
            return recordDateTime;
        }

        public void setRecordDateTime(String recordDateTime) {
            this.recordDateTime = recordDateTime;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Double getDirection() {
            return direction;
        }

        public void setDirection(Double direction) {
            this.direction = direction;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public Boolean getIsValidRecord() {
            return isValidRecord;
        }

        public void setIsValidRecord(Boolean isValidRecord) {
            this.isValidRecord = isValidRecord;
        }

        public Double getMileage() {
            return mileage;
        }

        public void setMileage(Double mileage) {
            this.mileage = mileage;
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

        public Integer getVehicleStatus() {
            return vehicleStatus;
        }

        public void setVehicleStatus(Integer vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }

        public String getVehicleStatusText() {
            return vehicleStatusText;
        }

        public void setVehicleStatusText(String vehicleStatusText) {
            this.vehicleStatusText = vehicleStatusText;
        }

        public Double getWorkingHours() {
            return workingHours;
        }

        public void setWorkingHours(Double workingHours) {
            this.workingHours = workingHours;
        }

        public String getFuel() {
            return fuel;
        }

        public void setFuel(String fuel) {
            this.fuel = fuel;
        }

        public Boolean getEngineStatus() {
            return engineStatus;
        }

        public void setEngineStatus(Boolean engineStatus) {
            this.engineStatus = engineStatus;
        }
    }
}
