//package com.models;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class AllVehicleModel implements Parcelable{
//
//    @SerializedName("VehicleID")
//    @Expose
//    private Integer vehicleID;
//    @SerializedName("Label")
//    @Expose
//    private String label;
//    @SerializedName("PlateNumber")
//    @Expose
//    private String plateNumber;
//    @SerializedName("SerialNumber")
//    @Expose
//    private String serialNumber;
//    @SerializedName("LastLocation")
//    @Expose
//    private LastLocation lastLocation;
//
//    private boolean selected = false;
//
//
//    protected AllVehicleModel(Parcel in) {
//        if (in.readByte() == 0) {
//            vehicleID = null;
//        } else {
//            vehicleID = in.readInt();
//        }
//        label = in.readString();
//        plateNumber = in.readString();
//        serialNumber = in.readString();
//        selected = in.readByte() != 0;
//    }
//
//    public static final Creator<AllVehicleModel> CREATOR = new Creator<AllVehicleModel>() {
//        @Override
//        public AllVehicleModel createFromParcel(Parcel in) {
//            return new AllVehicleModel(in);
//        }
//
//        @Override
//        public AllVehicleModel[] newArray(int size) {
//            return new AllVehicleModel[size];
//        }
//    };
//
//    public Integer getVehicleID() {
//        return vehicleID;
//    }
//
//    public void setVehicleID(Integer vehicleID) {
//        this.vehicleID = vehicleID;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public String getPlateNumber() {
//        return plateNumber;
//    }
//
//    public void setPlateNumber(String plateNumber) {
//        this.plateNumber = plateNumber;
//    }
//
//    public String getSerialNumber() {
//        return serialNumber;
//    }
//
//    public void setSerialNumber(String serialNumber) {
//        this.serialNumber = serialNumber;
//    }
//
//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//
//    public LastLocation getLastLocation() {
//        return lastLocation;
//    }
//
//    public void setLastLocation(LastLocation lastLocation) {
//        this.lastLocation = lastLocation;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (vehicleID == null) {
//            dest.writeByte((byte) 0);
//        } else {
//            dest.writeByte((byte) 1);
//            dest.writeInt(vehicleID);
//        }
//        dest.writeString(label);
//        dest.writeString(plateNumber);
//        dest.writeString(serialNumber);
//        dest.writeByte((byte) (selected ? 1 : 0));
//    }
//
//    public class LastLocation {
//
//        @SerializedName("VehicleID")
//        @Expose
//        private Integer vehicleID;
//        @SerializedName("Speed")
//        @Expose
//        private double speed;
//        @SerializedName("TotalMileage")
//        @Expose
//        private double totalMileage;
//        @SerializedName("TotalWorkingHours")
//        @Expose
//        private double totalWorkingHours;
//        @SerializedName("Direction")
//        @Expose
//        private double direction;
//        @SerializedName("Latitude")
//        @Expose
//        private double latitude;
//        @SerializedName("Longitude")
//        @Expose
//        private double longitude;
//        @SerializedName("Address")
//        @Expose
//        private String address;
//        @SerializedName("StreetSpeed")
//        @Expose
//        private Integer streetSpeed;
//        @SerializedName("VehicleStatus")
//        @Expose
//        private String vehicleStatus;
//        @SerializedName("RecordDateTime")
//        @Expose
//        private String recordDateTime;
//        @SerializedName("IsOnline")
//        @Expose
//        private Boolean isOnline;
//
//        public Integer getVehicleID() {
//            return vehicleID;
//        }
//
//        public void setVehicleID(Integer vehicleID) {
//            this.vehicleID = vehicleID;
//        }
//
//        public double getSpeed() {
//            return speed;
//        }
//
//        public void setSpeed(double speed) {
//            this.speed = speed;
//        }
//
//        public double getTotalMileage() {
//            return totalMileage;
//        }
//
//        public void setTotalMileage(double totalMileage) {
//            this.totalMileage = totalMileage;
//        }
//
//        public double getTotalWorkingHours() {
//            return totalWorkingHours;
//        }
//
//        public void setTotalWorkingHours(double totalWorkingHours) {
//            this.totalWorkingHours = totalWorkingHours;
//        }
//
//        public double getDirection() {
//            return direction;
//        }
//
//        public void setDirection(double direction) {
//            this.direction = direction;
//        }
//
//        public double getLatitude() {
//            return latitude;
//        }
//
//        public void setLatitude(double latitude) {
//            this.latitude = latitude;
//        }
//
//        public double getLatitude() {
//            return longitude;
//        }
//
//        public void setLongitude(double longitude) {
//            this.longitude = longitude;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public Integer getStreetSpeed() {
//            return streetSpeed;
//        }
//
//        public void setStreetSpeed(Integer streetSpeed) {
//            this.streetSpeed = streetSpeed;
//        }
//
//        public String getVehicleStatus() {
//            return vehicleStatus;
//        }
//
//        public void setVehicleStatus(String vehicleStatus) {
//            this.vehicleStatus = vehicleStatus;
//        }
//
//        public String getRecordDateTime() {
//            return recordDateTime;
//        }
//
//        public void setRecordDateTime(String recordDateTime) {
//            this.recordDateTime = recordDateTime;
//        }
//
//        public Boolean getIsOnline() {
//            return isOnline;
//        }
//
//        public void setIsOnline(Boolean isOnline) {
//            this.isOnline = isOnline;
//        }
//    }
//}
