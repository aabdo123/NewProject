package com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class DashboardModel implements Parcelable {

    @SerializedName("TotalVehiclesNumber")
    @Expose
    private Integer totalVehiclesNumber;
    @SerializedName("TotalOnlineNumber")
    @Expose
    private Integer totalOnlineNumber;
    @SerializedName("TotalVehiclesOnlinePercentage")
    @Expose
    private Double totalVehiclesOnlinePercentage;
    @SerializedName("TotalOfflineNumber")
    @Expose
    private Integer totalOfflineNumber;
    @SerializedName("TotalAlarmsCount")
    @Expose
    private Integer totalAlarmsCount;
    @SerializedName("TotalLongtimeOffline")
    @Expose
    private Integer totalLongtimeOffline;
    @SerializedName("TotleExpiringUsers")
    @Expose
    private Integer totleExpiringUsers;
    @SerializedName("LastVehicleLocation")
    @Expose
    private LastLocation.LastVehicleLocation lastVehicleLocation;

    protected DashboardModel(Parcel in) {
    }

    public static final Creator<DashboardModel> CREATOR = new Creator<DashboardModel>() {
        @Override
        public DashboardModel createFromParcel(Parcel in) {
            return new DashboardModel(in);
        }

        @Override
        public DashboardModel[] newArray(int size) {
            return new DashboardModel[size];
        }
    };

    public Integer getTotalVehiclesNumber() {
        return totalVehiclesNumber;
    }

    public void setTotalVehiclesNumber(Integer totalVehiclesNumber) {
        this.totalVehiclesNumber = totalVehiclesNumber;
    }

    public Integer getTotalOnlineNumber() {
        return totalOnlineNumber;
    }

    public void setTotalOnlineNumber(Integer totalOnlineNumber) {
        this.totalOnlineNumber = totalOnlineNumber;
    }

    public Double getTotalVehiclesOnlinePercentage() {
        return totalVehiclesOnlinePercentage;
    }

    public void setTotalVehiclesOnlinePercentage(Double totalVehiclesOnlinePercentage) {
        this.totalVehiclesOnlinePercentage = totalVehiclesOnlinePercentage;
    }

    public Integer getTotalOfflineNumber() {
        return totalOfflineNumber;
    }

    public void setTotalOfflineNumber(Integer totalOfflineNumber) {
        this.totalOfflineNumber = totalOfflineNumber;
    }

    public Integer getTotalAlarmsCount() {
        return totalAlarmsCount;
    }

    public void setTotalAlarmsCount(Integer totalAlarmsCount) {
        this.totalAlarmsCount = totalAlarmsCount;
    }

    public Integer getTotalLongtimeOffline() {
        return totalLongtimeOffline;
    }

    public void setTotalLongtimeOffline(Integer totalLongtimeOffline) {
        this.totalLongtimeOffline = totalLongtimeOffline;
    }

    public Integer getTotleExpiringUsers() {
        return totleExpiringUsers;
    }

    public void setTotleExpiringUsers(Integer totleExpiringUsers) {
        this.totleExpiringUsers = totleExpiringUsers;
    }

    public LastLocation.LastVehicleLocation getLastVehicleLocation() {
        return lastVehicleLocation;
    }

    public void setLastVehicleLocation(LastLocation.LastVehicleLocation lastVehicleLocation) {
        this.lastVehicleLocation = lastVehicleLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public class LastLocation {

        @SerializedName("VehicleID")
        @Expose
        private Integer vehicleID;
        @SerializedName("Speed")
        @Expose
        private String speed;
        @SerializedName("TotalMileage")
        @Expose
        private Double totalMileage;
        @SerializedName("TotalWorkingHours")
        @Expose
        private Object totalWorkingHours;
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

        public Integer getVehicleID() {
            return vehicleID;
        }

        public void setVehicleID(Integer vehicleID) {
            this.vehicleID = vehicleID;
        }

        public Integer getSpeed() {
            double d = Double.valueOf(speed);
            return (int) d;
        }

        public void setSpeed(Integer speed) {
            this.speed = String.valueOf(speed);
        }

        public Double getTotalMileage() {
            return totalMileage;
        }

        public void setTotalMileage(Double totalMileage) {
            this.totalMileage = totalMileage;
        }

        public Object getTotalWorkingHours() {
            return totalWorkingHours;
        }

        public void setTotalWorkingHours(Object totalWorkingHours) {
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

        public class LastVehicleLocation {

            @SerializedName("VehicleID")
            @Expose
            private Integer vehicleID;
            @SerializedName("Label")
            @Expose
            private String label;
            @SerializedName("PlateNumber")
            @Expose
            private String plateNumber;
            @SerializedName("SerialNumber")
            @Expose
            private String serialNumber;
            @SerializedName("LastLocation")
            @Expose
            private LastLocation lastLocation;

            public Integer getVehicleID() {
                return vehicleID;
            }

            public void setVehicleID(Integer vehicleID) {
                this.vehicleID = vehicleID;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getPlateNumber() {
                return plateNumber;
            }

            public void setPlateNumber(String plateNumber) {
                this.plateNumber = plateNumber;
            }

            public String getSerialNumber() {
                return serialNumber;
            }

            public void setSerialNumber(String serialNumber) {
                this.serialNumber = serialNumber;
            }

            public LastLocation getLastLocation() {
                return lastLocation;
            }

            public void setLastLocation(LastLocation lastLocation) {
                this.lastLocation = lastLocation;
            }
        }
    }
}