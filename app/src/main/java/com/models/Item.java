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

public class Item  extends RecyclerViewItem {

    String text = "";

    String secondText = "";
    private String ID = "";
    private String Name = "";
    private String VehicleStatus = "";
    private ArrayList<Item> Childs;
    private boolean isChecked;
    private boolean isClicked;
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

