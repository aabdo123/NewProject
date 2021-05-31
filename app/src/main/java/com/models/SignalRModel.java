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



    public class A{


        public boolean isOffline() {
            return isOffline;
        }

        public void setOffline(boolean offline) {
            isOffline = offline;
        }

        public String getrPM() {
            return rPM;
        }

        public void setrPM(String rPM) {
            this.rPM = rPM;
        }

        private boolean isOffline = false;
        @SerializedName("Address")
        private String address ="";

        @SerializedName("ActualWeight")
        private String actualWeight="";

        @SerializedName("EventType")
        private String eventType="";

        @SerializedName("ParsingDate")
        private String parsingDate="";

        @SerializedName("Latitude")
        private String latitude="";

        @SerializedName("CoolantTemp")
        private String coolantTemp="";

        @SerializedName("MileageMeter")
        private String mileageMeter="";

        @SerializedName("BatteryVoltage")
        private String batteryVoltage="";

        @SerializedName("SeatBelt")
        private String seatBelt="";

        @SerializedName("Temp4")
        private String temp4="";

        @SerializedName("RecordDateTime")
        private String recordDateTime="";

        @SerializedName("IsSOSHighjack")
        private String isSOSHighjack="";

        @SerializedName("AccelPedalPosition")
        private String accelPedalPosition="";

        @SerializedName("SeatBeltStatus")
        private String seatBeltStatus="";

        @SerializedName("DeviceTypeID")
        private String deviceTypeID="";

        @SerializedName("IsSOSHighJack")
        private String isSOSHighJack="";

        @SerializedName("Mileage")
        private String mileage="";

        @SerializedName("Battery3")
        private String battery3="";

        @SerializedName("Battery4")
        private String battery4="";

        @SerializedName("Battery1")
        private String battery1="";

        @SerializedName("IsPowerCutOff")
        private String isPowerCutOff="";

        @SerializedName("Battery2")
        private String battery2="";

        @SerializedName("Longitude")
        private String longitude="";

        @SerializedName("IngestionDate")
        private String ingestionDate="";

        @SerializedName("StreetSpeed")
        private String streetSpeed="";

        @SerializedName("TotalMileage")
        private String totalMileage="";

        @SerializedName("SerialNumber")
        private String serialNumber="";

        @SerializedName("IsPowerFromBettary")
        private String isPowerFromBettary="";

        @SerializedName("IsFuelCutOff")
        private String isFuelCutOff="";

        @SerializedName("VehicleStatus")
        private String vehicleStatus="";

        @SerializedName("InstantFuelConsum")
        private String instantFuelConsum;

        @SerializedName("StoppedTime")
        private String stoppedTime="";

        @SerializedName("IdleTime")
        private String idleTime="";

        @SerializedName("WeightVolt")
        private String weightVolt="";

        @SerializedName("Direction")
        private String direction="";

        @SerializedName("RPM")
        private String rPM="";

        @SerializedName("FuelLevelPer")
        private String fuelLevelPer="";

        @SerializedName("EngineTotalRunTime")
        private String engineTotalRunTime="";

        @SerializedName("HarshAcceleration")
        private String harshAcceleration="";

        @SerializedName("Speed")
        private String speed="";

        @SerializedName("WeightSensorReading")
        private String weightSensorReading="";

        @SerializedName("IsValidRecord")
        private String isValidRecord="";

        @SerializedName("IsOverSpeed")
        private String isOverSpeed="";

        @SerializedName("TotalFuelConsum")
        private String totalFuelConsum="";

        @SerializedName("DoorStatus")
        private String doorStatus="";

        @SerializedName("SleepStatus")
        private String sleepStatus="";

        @SerializedName("Hum2")
        private String hum2="";

        @SerializedName("Temp3")
        private String temp3="";

        @SerializedName("WorkingHours")
        private String workingHours="";

        @SerializedName("Hum3")
        private String hum3="";

        @SerializedName("Temp2")
        private String temp2="";

        @SerializedName("Temp1")
        private String temp1="";

        @SerializedName("HarshBreaking")
        private String harshBreaking="";

        @SerializedName("Hum1")
        private String hum1="";

        @SerializedName("IsLowPower")
        private String isLowPower="";

        @SerializedName("Hum4")
        private String hum4="";

        @SerializedName("EngineStatus")
        private String engineStatus="";

        @SerializedName("CargoWeight")
        private String cargoWeight="";

        public void setAddress(String address){
            this.address = address;
        }

        public String getAddress(){
            return address;
        }

        public void setActualWeight(String actualWeight){
            this.actualWeight = actualWeight;
        }

        public String getActualWeight(){
            return actualWeight;
        }

        public void setEventType(String eventType){
            this.eventType = eventType;
        }

        public String getEventType(){
            return eventType;
        }

        public void setParsingDate(String parsingDate){
            this.parsingDate = parsingDate;
        }

        public String getParsingDate(){
            return parsingDate;
        }

        public void setLatitude(String latitude){
            this.latitude = latitude;
        }

        public String getLatitude(){
            return latitude;
        }

        public void setCoolantTemp(String coolantTemp){
            this.coolantTemp = coolantTemp;
        }

        public String getCoolantTemp(){
            return coolantTemp;
        }

        public void setMileageMeter(String mileageMeter){
            this.mileageMeter = mileageMeter;
        }

        public String getMileageMeter(){
            return mileageMeter;
        }

        public void setBatteryVoltage(String batteryVoltage){
            this.batteryVoltage = batteryVoltage;
        }

        public String getBatteryVoltage(){
            return batteryVoltage;
        }

        public void setSeatBelt(String seatBelt){
            this.seatBelt = seatBelt;
        }

        public String getSeatBelt(){
            return seatBelt;
        }

        public void setTemp4(String temp4){
            this.temp4 = temp4;
        }

        public String getTemp4(){
            return temp4;
        }

        public void setRecordDateTime(String recordDateTime){
            this.recordDateTime = recordDateTime;
        }

        public String getRecordDateTime(){
            return recordDateTime;
        }

        public void setIsSOSHighjack(String isSOSHighjack){
            this.isSOSHighjack = isSOSHighjack;
        }

        public String getIsSOSHighjack(){
            return isSOSHighjack;
        }

        public void setAccelPedalPosition(String accelPedalPosition){
            this.accelPedalPosition = accelPedalPosition;
        }

        public String getAccelPedalPosition(){
            return accelPedalPosition;
        }

        public void setSeatBeltStatus(String seatBeltStatus){
            this.seatBeltStatus = seatBeltStatus;
        }

        public String getSeatBeltStatus(){
            return seatBeltStatus;
        }

        public void setDeviceTypeID(String deviceTypeID){
            this.deviceTypeID = deviceTypeID;
        }

        public String getDeviceTypeID(){
            return deviceTypeID;
        }

        public void setIsSOSHighJack(String isSOSHighJack){
            this.isSOSHighJack = isSOSHighJack;
        }

        public String getIsSOSHighJack(){
            return isSOSHighJack;
        }

        public void setMileage(String mileage){
            this.mileage = mileage;
        }

        public String getMileage(){
            return mileage;
        }

        public void setBattery3(String battery3){
            this.battery3 = battery3;
        }

        public String getBattery3(){
            return battery3;
        }

        public void setBattery4(String battery4){
            this.battery4 = battery4;
        }

        public String getBattery4(){
            return battery4;
        }

        public void setBattery1(String battery1){
            this.battery1 = battery1;
        }

        public String getBattery1(){
            return battery1;
        }

        public void setIsPowerCutOff(String isPowerCutOff){
            this.isPowerCutOff = isPowerCutOff;
        }

        public String getIsPowerCutOff(){
            return isPowerCutOff;
        }

        public void setBattery2(String battery2){
            this.battery2 = battery2;
        }

        public String getBattery2(){
            return battery2;
        }

        public void setLongitude(String longitude){
            this.longitude = longitude;
        }

        public String getLongitude(){
            return longitude;
        }

        public void setIngestionDate(String ingestionDate){
            this.ingestionDate = ingestionDate;
        }

        public String getIngestionDate(){
            return ingestionDate;
        }

        public void setStreetSpeed(String streetSpeed){
            this.streetSpeed = streetSpeed;
        }

        public String getStreetSpeed(){
            return streetSpeed;
        }

        public void setTotalMileage(String totalMileage){
            this.totalMileage = totalMileage;
        }

        public String getTotalMileage(){
            return totalMileage;
        }

        public void setSerialNumber(String serialNumber){
            this.serialNumber = serialNumber;
        }

        public String getSerialNumber(){
            return serialNumber;
        }

        public void setIsPowerFromBettary(String isPowerFromBettary){
            this.isPowerFromBettary = isPowerFromBettary;
        }

        public String getIsPowerFromBettary(){
            return isPowerFromBettary;
        }

        public void setIsFuelCutOff(String isFuelCutOff){
            this.isFuelCutOff = isFuelCutOff;
        }

        public String getIsFuelCutOff(){
            return isFuelCutOff;
        }

        public void setVehicleStatus(String vehicleStatus){
            this.vehicleStatus = vehicleStatus;
        }

        public String getVehicleStatus(){
            return vehicleStatus;
        }

        public void setInstantFuelConsum(String instantFuelConsum){
            this.instantFuelConsum = instantFuelConsum;
        }

        public String getInstantFuelConsum(){
            return instantFuelConsum;
        }

        public void setStoppedTime(String stoppedTime){
            this.stoppedTime = stoppedTime;
        }

        public String getStoppedTime(){
            return stoppedTime;
        }

        public void setIdleTime(String idleTime){
            this.idleTime = idleTime;
        }

        public String getIdleTime(){
            return idleTime;
        }

        public void setWeightVolt(String weightVolt){
            this.weightVolt = weightVolt;
        }

        public String getWeightVolt(){
            return weightVolt;
        }

        public void setDirection(String direction){
            this.direction = direction;
        }

        public String getDirection(){
            return direction;
        }

        public void setRPM(String rPM){
            this.rPM = rPM;
        }

        public String getRPM(){
            return rPM;
        }

        public void setFuelLevelPer(String fuelLevelPer){
            this.fuelLevelPer = fuelLevelPer;
        }

        public String getFuelLevelPer(){
            return fuelLevelPer;
        }

        public void setEngineTotalRunTime(String engineTotalRunTime){
            this.engineTotalRunTime = engineTotalRunTime;
        }

        public String getEngineTotalRunTime(){
            return engineTotalRunTime;
        }

        public void setHarshAcceleration(String harshAcceleration){
            this.harshAcceleration = harshAcceleration;
        }

        public String getHarshAcceleration(){
            return harshAcceleration;
        }

        public void setSpeed(String speed){
            this.speed = speed;
        }

        public String getSpeed(){
            return speed;
        }

        public void setWeightSensorReading(String weightSensorReading){
            this.weightSensorReading = weightSensorReading;
        }

        public String getWeightSensorReading(){
            return weightSensorReading;
        }

        public void setIsValidRecord(String isValidRecord){
            this.isValidRecord = isValidRecord;
        }

        public String getIsValidRecord(){
            return isValidRecord;
        }

        public void setIsOverSpeed(String isOverSpeed){
            this.isOverSpeed = isOverSpeed;
        }

        public String getIsOverSpeed(){
            return isOverSpeed;
        }

        public void setTotalFuelConsum(String totalFuelConsum){
            this.totalFuelConsum = totalFuelConsum;
        }

        public String getTotalFuelConsum(){
            return totalFuelConsum;
        }

        public void setDoorStatus(String doorStatus){
            this.doorStatus = doorStatus;
        }

        public String getDoorStatus(){
            return doorStatus;
        }

        public void setSleepStatus(String sleepStatus){
            this.sleepStatus = sleepStatus;
        }

        public String getSleepStatus(){
            return sleepStatus;
        }

        public void setHum2(String hum2){
            this.hum2 = hum2;
        }

        public String getHum2(){
            return hum2;
        }

        public void setTemp3(String temp3){
            this.temp3 = temp3;
        }

        public String getTemp3(){
            return temp3;
        }

        public void setWorkingHours(String workingHours){
            this.workingHours = workingHours;
        }

        public String getWorkingHours(){
            return workingHours;
        }

        public void setHum3(String hum3){
            this.hum3 = hum3;
        }

        public String getHum3(){
            return hum3;
        }

        public void setTemp2(String temp2){
            this.temp2 = temp2;
        }

        public String getTemp2(){
            return temp2;
        }

        public void setTemp1(String temp1){
            this.temp1 = temp1;
        }

        public String getTemp1(){
            return temp1;
        }

        public void setHarshBreaking(String harshBreaking){
            this.harshBreaking = harshBreaking;
        }

        public String getHarshBreaking(){
            return harshBreaking;
        }

        public void setHum1(String hum1){
            this.hum1 = hum1;
        }

        public String getHum1(){
            return hum1;
        }

        public void setIsLowPower(String isLowPower){
            this.isLowPower = isLowPower;
        }

        public String getIsLowPower(){
            return isLowPower;
        }

        public void setHum4(String hum4){
            this.hum4 = hum4;
        }

        public String getHum4(){
            return hum4;
        }

        public void setEngineStatus(String engineStatus){
            this.engineStatus = engineStatus;
        }

        public String getEngineStatus(){
            return engineStatus;
        }

        public void setCargoWeight(String cargoWeight){
            this.cargoWeight = cargoWeight;
        }

        public String getCargoWeight(){
            return cargoWeight;
        }
    }}