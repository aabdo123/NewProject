package com.models;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfVehiclesSmallModel implements Parent<ListOfVehiclesSmallModel.Vehicles> {

    private String header;
    private List<ListOfVehiclesSmallModel.Vehicles> vehicleModel;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<ListOfVehiclesSmallModel.Vehicles> getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(List<ListOfVehiclesSmallModel.Vehicles> vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Override
    public List<ListOfVehiclesSmallModel.Vehicles> getChildList() {
        return vehicleModel;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public class Vehicles {

        @SerializedName("ID")
        @Expose
        private Integer iD;
        @SerializedName("Name")
        @Expose
        private String name;

        private boolean isSelected;

        public Integer getID() {
            return iD;
        }

        public void setID(Integer iD) {
            this.iD = iD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
