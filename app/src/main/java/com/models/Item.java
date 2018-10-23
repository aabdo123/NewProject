package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.multilevelview.models.RecyclerViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Item extends RecyclerViewItem {

    String text = "";

    String secondText = "";
    private String ID = "";
    private String Name = "";
    private Item parent;
    private ArrayList<Item> Childs;
    private boolean isChecked;
    private boolean isClicked;
    private boolean isGroupChecked;
    private List<ListOfVehiclesModel.VehicleModel.LastLocation> lastLocationList;
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
    @SerializedName("VehicleDisplayName")
    @Expose
    private String VehicleDisplayName;
    @SerializedName("StreetSpeed")
    @Expose
    private Integer streetSpeed;
    @SerializedName("RecordDateTime")
    @Expose
    private String recordDateTime;
    @SerializedName("PlateNumber")
    @Expose
    private String plateNumber;
    @SerializedName("IsOnline")
    @Expose
    private Boolean isOnline;
    @SerializedName("Fuel")
    @Expose
    private String fuel;

    @SerializedName("SimCardNumber")
    @Expose
    private String simCardNumber;

    @SerializedName("VehicleStatus")
    @Expose
    private String VehicleStatus = "";

    @SerializedName("Mileage")
    @Expose
    private String mileage;
    @SerializedName("WorkingHours")
    @Expose
    private String workingHours;
    @SerializedName("Serial")
    @Expose
    private String serial;
    @SerializedName("EngineStatus")
    @Expose
    private Boolean engineStatus;

    @SerializedName("SeatBeltStatus")
    @Expose
    private String seatBeltStatus;
    @SerializedName("Temper")
    @Expose
    private String temper;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("Temperature")
    @Expose
    private String temperature;
    @SerializedName("DoorStatus")
    @Expose
    private String doorStatus;

    public boolean isGroupChecked() {
        return isGroupChecked;
    }

    public void setGroupChecked(boolean groupChecked) {
        isGroupChecked = groupChecked;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(String doorStatus) {
        this.doorStatus = doorStatus;
    }

    public String getSeatBeltStatus() {
        return seatBeltStatus;
    }

    public void setSeatBeltStatus(String seatBeltStatus) {
        this.seatBeltStatus = seatBeltStatus;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getSimCardNumber() {
        return simCardNumber;
    }

    public void setSimCardNumber(String simCardNumber) {
        this.simCardNumber = simCardNumber;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(Boolean engineStatus) {
        this.engineStatus = engineStatus;
    }

    public String getVehicleDisplayName() {
        return VehicleDisplayName;
    }

    public void setVehicleDisplayName(String vehicleDisplayName) {
        VehicleDisplayName = vehicleDisplayName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getVehicleID() {
        return vehicleID;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
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

    public String getRecordDateTime() {
        return recordDateTime;
    }

    public void setRecordDateTime(String recordDateTime) {
        this.recordDateTime = recordDateTime;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public List<ListOfVehiclesModel.VehicleModel.LastLocation> getLastLocationList() {
        return lastLocationList;
    }

    public void setLastLocationList(List<ListOfVehiclesModel.VehicleModel.LastLocation> lastLocationList) {
        this.lastLocationList = lastLocationList;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVehicleStatus() {
        return VehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        VehicleStatus = vehicleStatus;
    }

    public ArrayList<Item> getChilds() {
        return Childs;
    }

    public void setChilds(ArrayList<Item> childs) {
        Childs = childs;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Item parse(JSONObject data) throws JSONException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(String.valueOf(data), Item.class);
    }

    public static List<Item> parseArray(JSONArray data) throws JSONException {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Item>>() {
        }.getType();
        return gson.fromJson(String.valueOf(data), listType);
    }


    public Item(int level) {
        super(level);
    }
}

