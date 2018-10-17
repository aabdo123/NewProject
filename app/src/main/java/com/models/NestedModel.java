package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadra on 7/29/17.
 */

public class NestedModel {
    public String title;
    private ArrayList<NestedModel> children;
    private Integer vehicleID;
    private String Label;
    private String PlateNumber;
    private String SerialNumber;
    private ListOfVehiclesModel.VehicleModel.LastLocation LastLocation;
    private String serialNumber;
    private boolean isChecked;


    public NestedModel(String title) {
        this.title = title;
    }

    public NestedModel() {

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public ListOfVehiclesModel.VehicleModel.LastLocation getLastLocation() {
        return LastLocation;
    }

    public void setLastLocation(ListOfVehiclesModel.VehicleModel.LastLocation lastLocation) {
        LastLocation = lastLocation;
    }

    public void setName(String title) {
        this.title = title;
    }

    public ArrayList<NestedModel> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<NestedModel> children) {
        this.children = children;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public static NestedModel parse(JSONObject data) throws JSONException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(String.valueOf(data), NestedModel.class);
    }

    public static ArrayList<NestedModel> parseArray(JSONArray data) throws JSONException {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<NestedModel>>() {
        }.getType();
        return gson.fromJson(String.valueOf(data), listType);
    }

    public String getName() {
        return title;
    }
}
