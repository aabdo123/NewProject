package com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllVehiclesInHashModel {

    private int vehicleId;
    private Marker marker;
    private AllVehicleModel allVehicleModel;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public AllVehicleModel getAllVehicleModel() {
        return allVehicleModel;
    }

    public void setAllVehicleModel(AllVehicleModel allVehicleModel) {
        this.allVehicleModel = allVehicleModel;
    }

    public static class AllVehicleModel implements Parcelable {
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


        public AllVehicleModel(Parcel in) {
            if (in.readByte() == 0) {
                vehicleID = null;
            } else {
                vehicleID = in.readInt();
            }
            label = in.readString();
            plateNumber = in.readString();
            serialNumber = in.readString();
        }

        public final Creator<AllVehicleModel> CREATOR = new Creator<AllVehicleModel>() {
            @Override
            public AllVehicleModel createFromParcel(Parcel in) {
                return new AllVehicleModel(in);
            }

            @Override
            public AllVehicleModel[] newArray(int size) {
                return new AllVehicleModel[size];
            }
        };

        public AllVehicleModel() {

        }

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
        }

        public class LastLocation implements Parcelable {

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
            @SerializedName("PlateNumber")
            @Expose
            private String plateNumber;
            @SerializedName("Fuel")
            @Expose
            private String fuel;
            @SerializedName("StreetSpeed")
            @Expose
            private Integer streetSpeed;
            @SerializedName("SimCardNumber")
            @Expose
            private String simCardNumber;
            @SerializedName("VehicleDisplayName")
            @Expose
            private String VehicleDisplayName;
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
            @SerializedName("DoorStatus")
            @Expose
            private Boolean doorStatus;


            protected LastLocation(Parcel in) {
                if (in.readByte() == 0) {
                    vehicleID = null;
                } else {
                    vehicleID = in.readInt();
                }
                speed = in.readDouble();
                totalMileage = in.readDouble();
                totalWorkingHours = in.readDouble();
                direction = in.readDouble();
                latitude = in.readDouble();
                longitude = in.readDouble();
                address = in.readString();
                plateNumber = in.readString();
                fuel = in.readString();
                if (in.readByte() == 0) {
                    streetSpeed = null;
                } else {
                    streetSpeed = in.readInt();
                }
                simCardNumber = in.readString();
                VehicleDisplayName = in.readString();
                vehicleStatus = in.readString();
                recordDateTime = in.readString();
                byte tmpIsOnline = in.readByte();
                isOnline = tmpIsOnline == 0 ? null : tmpIsOnline == 1;
                byte tmpEngineStatus = in.readByte();
                engineStatus = tmpEngineStatus == 0 ? null : tmpEngineStatus == 1;
                byte tmpDoorStatus = in.readByte();
                doorStatus = tmpDoorStatus == 0 ? null : tmpDoorStatus == 1;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                if (vehicleID == null) {
                    dest.writeByte((byte) 0);
                } else {
                    dest.writeByte((byte) 1);
                    dest.writeInt(vehicleID);
                }
                dest.writeDouble(speed);
                dest.writeDouble(totalMileage);
                dest.writeDouble(totalWorkingHours);
                dest.writeDouble(direction);
                dest.writeDouble(latitude);
                dest.writeDouble(longitude);
                dest.writeString(address);
                dest.writeString(plateNumber);
                dest.writeString(fuel);
                if (streetSpeed == null) {
                    dest.writeByte((byte) 0);
                } else {
                    dest.writeByte((byte) 1);
                    dest.writeInt(streetSpeed);
                }
                dest.writeString(simCardNumber);
                dest.writeString(VehicleDisplayName);
                dest.writeString(vehicleStatus);
                dest.writeString(recordDateTime);
                dest.writeByte((byte) (isOnline == null ? 0 : isOnline ? 1 : 2));
                dest.writeByte((byte) (engineStatus == null ? 0 : engineStatus ? 1 : 2));
                dest.writeByte((byte) (doorStatus == null ? 0 : doorStatus ? 1 : 2));
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public String getVehicleDisplayName() {
                return VehicleDisplayName;
            }

            public void setVehicleDisplayName(String vehicleDisplayName) {
                VehicleDisplayName = vehicleDisplayName;
            }

            public final Creator<LastLocation> CREATOR = new Creator<LastLocation>() {
                @Override
                public LastLocation createFromParcel(Parcel in) {
                    return new LastLocation(in);
                }

                @Override
                public LastLocation[] newArray(int size) {
                    return new LastLocation[size];
                }
            };

            public String getPlateNumber() {
                return plateNumber;
            }

            public void setPlateNumber(String plateNumber) {
                this.plateNumber = plateNumber;
            }

            public Boolean getEngineStatus() {
                return engineStatus;
            }

            public void setEngineStatus(Boolean engineStatus) {
                this.engineStatus = engineStatus;
            }

            public Boolean getDoorStatus() {
                return doorStatus;
            }

            public void setDoorStatus(Boolean doorStatus) {
                this.doorStatus = doorStatus;
            }

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

            public String getFuel() {
                return fuel;
            }

            public void setFuel(String fuel) {
                this.fuel = fuel;
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

            public String getSimCardNumber() {
                return simCardNumber;
            }

            public void setSimCardNumber(String simCardNumber) {
                this.simCardNumber = simCardNumber;
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