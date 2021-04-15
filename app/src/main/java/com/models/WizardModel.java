package com.models;

import com.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class WizardModel {

    private List<ReportsTypeModel> reportsType = new ArrayList<>();
    private List<String> reportsFrequency = new ArrayList<>();
    private boolean typeSelected;
    private boolean hourSelected;
    private String reportsFrequencyID;
    private List<ListOfVehiclesSmallModel.Vehicles> vehicleList = new ArrayList<>();
    private List<ListOfUsersModel> usersList = new ArrayList<>();
    private List<String> emails = new ArrayList<>();
    private boolean notifyBySms;
    private List<String> phones = new ArrayList<>();
    private List<String> scheduleFrequency = new ArrayList<>();
    private String description;
    private String fromDate;
    private String toDate;
    private List<Item> itemList = new ArrayList<>();
    private String fromTime;
    private String toTime;
    private String duration;
    private String minimumSpeed;
    private String selectedType;

    public String getMinimumSpeed() {
        return minimumSpeed;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public void setMinimumSpeed(String minimumSpeed) {
        this.minimumSpeed = minimumSpeed;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void addReportsType(ReportsTypeModel reportsModel) {
        reportsType.add(reportsModel);
    }

    public void removeReportsType(ReportsTypeModel reportsModel) {
        reportsType.remove(reportsModel);
    }

    public List<ReportsTypeModel> getReportsType() {
        return reportsType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getReportsFrequencyID() {
        return reportsFrequencyID;
    }

    public void setReportsFrequencyID(String reportsFrequencyID) {
        this.reportsFrequencyID = reportsFrequencyID;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public List<String> getReportsFrequency() {
        return reportsFrequency;
    }

    public void addReportsFrequency(String reportsFrequency) {
        this.reportsFrequency.add(reportsFrequency);
    }

    public void removeReportsFrequency(String reportsFrequency) {
        this.reportsFrequency.remove(reportsFrequency);
    }

    public void clearReportsFrequency() {
        this.reportsFrequency.clear();
    }

    public boolean isTypeSelected() {
        return typeSelected;
    }

    public void setTypeSelected(boolean typeSelected) {
        this.typeSelected = typeSelected;
    }

    public boolean isHourSelected() {
        return hourSelected;
    }

    public void setHourSelected(boolean hourSelected) {
        this.hourSelected = hourSelected;
    }

    public void addVehicles(ListOfVehiclesSmallModel.Vehicles vehicleModel) {
        vehicleList.add(vehicleModel);
    }

    public void removeVehicles(ListOfVehiclesSmallModel.Vehicles vehicleModel) {
        vehicleList.remove(vehicleModel);
    }

    public List<ListOfVehiclesSmallModel.Vehicles> getVehicleList() {
        return vehicleList;
    }

    public void addUsers(ListOfUsersModel usersModel) {
        usersList.add(usersModel);
    }

    public void removeUsers(ListOfUsersModel usersModel) {
        usersList.remove(usersModel);
    }

    public List<ListOfUsersModel> getUsersList() {
        return usersList;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public void removeEmail(String email) {
        emails.remove(email);
    }

    public boolean isNotifyBySms() {
        return notifyBySms;
    }

    public void setNotifyBySms(boolean notifyBySms) {
        this.notifyBySms = notifyBySms;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void addPhones(String phone) {
        phones.add(phone);
    }

    public void removePhones(String phone) {
        phones.remove(phone);
    }

    public List<String> getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void addScheduleFrequency(String frequency) {
        scheduleFrequency.add(frequency);
    }

    public void removeScheduleFrequency(String frequency) {
        scheduleFrequency.remove(frequency);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
