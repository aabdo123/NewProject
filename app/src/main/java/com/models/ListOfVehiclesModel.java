package com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saferoad-Dev1 on 8/30/2017.
 */

public class ListOfVehiclesModel implements Parent<ListOfVehiclesModel.VehicleModel>, Parcelable {

    private String header;
    private List<VehicleModel> vehicleModel;

    public ListOfVehiclesModel() {
    }

    protected ListOfVehiclesModel(Parcel in) {
        header = in.readString();
        vehicleModel = in.createTypedArrayList(VehicleModel.CREATOR);
    }

    public static final Creator<ListOfVehiclesModel> CREATOR = new Creator<ListOfVehiclesModel>() {
        @Override
        public ListOfVehiclesModel createFromParcel(Parcel in) {
            return new ListOfVehiclesModel(in);
        }

        @Override
        public ListOfVehiclesModel[] newArray(int size) {
            return new ListOfVehiclesModel[size];
        }
    };

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<VehicleModel> getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(List<VehicleModel> vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Override
    public List<VehicleModel> getChildList() {
        return vehicleModel;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeTypedList(vehicleModel);
    }

    public static class VehicleModel implements Parcelable {

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

        private boolean selected = false;

        protected VehicleModel(Parcel in) {
            if (in.readByte() == 0) {
                vehicleID = null;
            } else {
                vehicleID = in.readInt();
            }
            label = in.readString();
            plateNumber = in.readString();
            serialNumber = in.readString();
            selected = in.readByte() != 0;
        }

        public static final Creator<VehicleModel> CREATOR = new Creator<VehicleModel>() {
            @Override
            public VehicleModel createFromParcel(Parcel in) {
                return new VehicleModel(in);
            }

            @Override
            public VehicleModel[] newArray(int size) {
                return new VehicleModel[size];
            }
        };

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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public LastLocation getLastLocation() {
            return lastLocation;
        }

        public void setLastLocation(LastLocation lastLocation) {
            this.lastLocation = lastLocation;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (vehicleID == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(vehicleID);
            }
            dest.writeString(label);
            dest.writeString(plateNumber);
            dest.writeString(serialNumber);
            dest.writeByte((byte) (selected ? 1 : 0));
        }

        public class LastLocation {

            @SerializedName("VehicleID")
            @Expose
            private Integer vehicleID;
            @SerializedName("Speed")
            @Expose
            private double speed;
            @SerializedName("TotalMileage")
            @Expose
            private double totalMileage;
            @SerializedName("TotalWorkingHours")
            @Expose
            private double totalWorkingHours;
            @SerializedName("Direction")
            @Expose
            private double direction;
            @SerializedName("Latitude")
            @Expose
            private double latitude;
            @SerializedName("Longitude")
            @Expose
            private double longitude;
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

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }

            public double getTotalMileage() {
                return totalMileage;
            }

            public void setTotalMileage(double totalMileage) {
                this.totalMileage = totalMileage;
            }

            public double getTotalWorkingHours() {
                return totalWorkingHours;
            }

            public void setTotalWorkingHours(double totalWorkingHours) {
                this.totalWorkingHours = totalWorkingHours;
            }

            public double getDirection() {
                return direction;
            }

            public void setDirection(double direction) {
                this.direction = direction;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
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
    }
}