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

    public class A {
        @SerializedName("AccelPedalPosition")
        @Expose
        private Double accelPedalPosition;
        @SerializedName("ActualWeight")
        @Expose
        private Double actualWeight;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Battery1")
        @Expose
        private Double battery1;
        @SerializedName("Battery2")
        @Expose
        private Double battery2;
        @SerializedName("Battery3")
        @Expose
        private Double battery3;
        @SerializedName("Battery4")
        @Expose
        private Double battery4;
        @SerializedName("BatteryVoltage")
        @Expose
        private Double batteryVoltage;
        @SerializedName("CargoWeight")
        @Expose
        private Double cargoWeight;
        @SerializedName("CoolantTemp")
        @Expose
        private Integer coolantTemp;
        @SerializedName("DeviceTypeID")
        @Expose
        private Integer deviceTypeID;
        @SerializedName("Direction")
        @Expose
        private Double direction;
        @SerializedName("DoorStatus")
        @Expose
        private Boolean doorStatus;
        @SerializedName("EngineStatus")
        @Expose
        private Boolean engineStatus;
        @SerializedName("EngineTotalRunTime")
        @Expose
        private Integer engineTotalRunTime;
        @SerializedName("EventType")
        @Expose
        private Integer eventType;
        @SerializedName("FuelLevelPer")
        @Expose
        private Double fuelLevelPer;
        @SerializedName("HarshAcceleration")
        @Expose
        private Integer harshAcceleration;
        @SerializedName("HarshBreaking")
        @Expose
        private Integer harshBreaking;
        @SerializedName("Hum1")
        @Expose
        private Double hum1;
        @SerializedName("Hum2")
        @Expose
        private Double hum2;
        @SerializedName("Hum3")
        @Expose
        private Double hum3;
        @SerializedName("Hum4")
        @Expose
        private Double hum4;
        @SerializedName("IdleTime")
        @Expose
        private Integer idleTime;
        @SerializedName("IngestionDate")
        @Expose
        private String ingestionDate;
        @SerializedName("InstantFuelConsum")
        @Expose
        private Double instantFuelConsum;
        @SerializedName("IsFuelCutOff")
        @Expose
        private Boolean isFuelCutOff;
        @SerializedName("IsLowPower")
        @Expose
        private Boolean isLowPower;
        @SerializedName("IsOverSpeed")
        @Expose
        private Boolean isOverSpeed;
        @SerializedName("IsPowerCutOff")
        @Expose
        private Boolean isPowerCutOff;
        @SerializedName("IsPowerFromBettary")
        @Expose
        private Boolean isPowerFromBettary;
        @SerializedName("IsSOSHighJack")
        @Expose
        private Boolean isSOSHighJack;
        @SerializedName("IsSOSHighjack")
        @Expose
        private Boolean isSOSHighjack;
        @SerializedName("IsValidRecord")
        @Expose
        private Boolean isValidRecord;
        @SerializedName("Latitude")
        @Expose
        private Double latitude;
        @SerializedName("Longitude")
        @Expose
        private Double longitude;
        @SerializedName("Mileage")
        @Expose
        private Double mileage;
        @SerializedName("MileageMeter")
        @Expose
        private Integer mileageMeter;
        @SerializedName("ParsingDate")
        @Expose
        private String parsingDate;
        @SerializedName("RPM")
        @Expose
        private Integer rpm;
        @SerializedName("RecordDateTime")
        @Expose
        private String recordDateTime;
        @SerializedName("SeatBelt")
        @Expose
        private Boolean seatBelt;
        @SerializedName("SeatBeltStatus")
        @Expose
        private Boolean seatBeltStatus;
        @SerializedName("SerialNumber")
        @Expose
        private String serialNumber;
        @SerializedName("SleepStatus")
        @Expose
        private Boolean sleepStatus;
        @SerializedName("Speed")
        @Expose
        private Double speed;
        @SerializedName("StoppedTime")
        @Expose
        private Integer stoppedTime;
        @SerializedName("StreetSpeed")
        @Expose
        private Integer streetSpeed;
        @SerializedName("Temp1")
        @Expose
        private Double temp1;
        @SerializedName("Temp2")
        @Expose
        private Double temp2;
        @SerializedName("Temp3")
        @Expose
        private Double temp3;
        @SerializedName("Temp4")
        @Expose
        private Double temp4;
        @SerializedName("TotalFuelConsum")
        @Expose
        private Integer totalFuelConsum;
        @SerializedName("TotalMileage")
        @Expose
        private Integer totalMileage;
        @SerializedName("VehicleStatus")
        @Expose
        private Integer vehicleStatus;
        @SerializedName("WeightSensorReading")
        @Expose
        private Integer weightSensorReading;
        @SerializedName("WeightVolt")
        @Expose
        private Double weightVolt;
        @SerializedName("WorkingHours")
        @Expose
        private Double workingHours;

        public Double getAccelPedalPosition() {
            return accelPedalPosition;
        }

        public void setAccelPedalPosition(Double accelPedalPosition) {
            this.accelPedalPosition = accelPedalPosition;
        }

        public Double getActualWeight() {
            return actualWeight;
        }

        public void setActualWeight(Double actualWeight) {
            this.actualWeight = actualWeight;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Double getBattery1() {
            return battery1;
        }

        public void setBattery1(Double battery1) {
            this.battery1 = battery1;
        }

        public Double getBattery2() {
            return battery2;
        }

        public void setBattery2(Double battery2) {
            this.battery2 = battery2;
        }

        public Double getBattery3() {
            return battery3;
        }

        public void setBattery3(Double battery3) {
            this.battery3 = battery3;
        }

        public Double getBattery4() {
            return battery4;
        }

        public void setBattery4(Double battery4) {
            this.battery4 = battery4;
        }

        public Double getBatteryVoltage() {
            return batteryVoltage;
        }

        public void setBatteryVoltage(Double batteryVoltage) {
            this.batteryVoltage = batteryVoltage;
        }

        public Double getCargoWeight() {
            return cargoWeight;
        }

        public void setCargoWeight(Double cargoWeight) {
            this.cargoWeight = cargoWeight;
        }

        public Integer getCoolantTemp() {
            return coolantTemp;
        }

        public void setCoolantTemp(Integer coolantTemp) {
            this.coolantTemp = coolantTemp;
        }

        public Integer getDeviceTypeID() {
            return deviceTypeID;
        }

        public void setDeviceTypeID(Integer deviceTypeID) {
            this.deviceTypeID = deviceTypeID;
        }

        public Double getDirection() {
            return direction;
        }

        public void setDirection(Double direction) {
            this.direction = direction;
        }

        public Boolean getDoorStatus() {
            return doorStatus;
        }

        public void setDoorStatus(Boolean doorStatus) {
            this.doorStatus = doorStatus;
        }

        public Boolean getEngineStatus() {
            return engineStatus;
        }

        public void setEngineStatus(Boolean engineStatus) {
            this.engineStatus = engineStatus;
        }

        public Integer getEngineTotalRunTime() {
            return engineTotalRunTime;
        }

        public void setEngineTotalRunTime(Integer engineTotalRunTime) {
            this.engineTotalRunTime = engineTotalRunTime;
        }

        public Integer getEventType() {
            return eventType;
        }

        public void setEventType(Integer eventType) {
            this.eventType = eventType;
        }

        public Double getFuelLevelPer() {
            return fuelLevelPer;
        }

        public void setFuelLevelPer(Double fuelLevelPer) {
            this.fuelLevelPer = fuelLevelPer;
        }

        public Integer getHarshAcceleration() {
            return harshAcceleration;
        }

        public void setHarshAcceleration(Integer harshAcceleration) {
            this.harshAcceleration = harshAcceleration;
        }

        public Integer getHarshBreaking() {
            return harshBreaking;
        }

        public void setHarshBreaking(Integer harshBreaking) {
            this.harshBreaking = harshBreaking;
        }

        public Double getHum1() {
            return hum1;
        }

        public void setHum1(Double hum1) {
            this.hum1 = hum1;
        }

        public Double getHum2() {
            return hum2;
        }

        public void setHum2(Double hum2) {
            this.hum2 = hum2;
        }

        public Double getHum3() {
            return hum3;
        }

        public void setHum3(Double hum3) {
            this.hum3 = hum3;
        }

        public Double getHum4() {
            return hum4;
        }

        public void setHum4(Double hum4) {
            this.hum4 = hum4;
        }

        public Integer getIdleTime() {
            return idleTime;
        }

        public void setIdleTime(Integer idleTime) {
            this.idleTime = idleTime;
        }

        public String getIngestionDate() {
            return ingestionDate;
        }

        public void setIngestionDate(String ingestionDate) {
            this.ingestionDate = ingestionDate;
        }

        public Double getInstantFuelConsum() {
            return instantFuelConsum;
        }

        public void setInstantFuelConsum(Double instantFuelConsum) {
            this.instantFuelConsum = instantFuelConsum;
        }

        public Boolean getIsFuelCutOff() {
            return isFuelCutOff;
        }

        public void setIsFuelCutOff(Boolean isFuelCutOff) {
            this.isFuelCutOff = isFuelCutOff;
        }

        public Boolean getIsLowPower() {
            return isLowPower;
        }

        public void setIsLowPower(Boolean isLowPower) {
            this.isLowPower = isLowPower;
        }

        public Boolean getIsOverSpeed() {
            return isOverSpeed;
        }

        public void setIsOverSpeed(Boolean isOverSpeed) {
            this.isOverSpeed = isOverSpeed;
        }

        public Boolean getIsPowerCutOff() {
            return isPowerCutOff;
        }

        public void setIsPowerCutOff(Boolean isPowerCutOff) {
            this.isPowerCutOff = isPowerCutOff;
        }

        public Boolean getIsPowerFromBettary() {
            return isPowerFromBettary;
        }

        public void setIsPowerFromBettary(Boolean isPowerFromBettary) {
            this.isPowerFromBettary = isPowerFromBettary;
        }

        public Boolean getIsSOSHighJack() {
            return isSOSHighJack;
        }

        public void setIsSOSHighJack(Boolean isSOSHighJack) {
            this.isSOSHighJack = isSOSHighJack;
        }

        public Boolean getIsSOSHighjack() {
            return isSOSHighjack;
        }

        public void setIsSOSHighjack(Boolean isSOSHighjack) {
            this.isSOSHighjack = isSOSHighjack;
        }

        public Boolean getIsValidRecord() {
            return isValidRecord;
        }

        public void setIsValidRecord(Boolean isValidRecord) {
            this.isValidRecord = isValidRecord;
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

        public Double getMileage() {
            return mileage;
        }

        public void setMileage(Double mileage) {
            this.mileage = mileage;
        }

        public Integer getMileageMeter() {
            return mileageMeter;
        }

        public void setMileageMeter(Integer mileageMeter) {
            this.mileageMeter = mileageMeter;
        }

        public String getParsingDate() {
            return parsingDate;
        }

        public void setParsingDate(String parsingDate) {
            this.parsingDate = parsingDate;
        }

        public Integer getRpm() {
            return rpm;
        }

        public void setRpm(Integer rpm) {
            this.rpm = rpm;
        }

        public String getRecordDateTime() {
            return recordDateTime;
        }

        public void setRecordDateTime(String recordDateTime) {
            this.recordDateTime = recordDateTime;
        }

        public Boolean getSeatBelt() {
            return seatBelt;
        }

        public void setSeatBelt(Boolean seatBelt) {
            this.seatBelt = seatBelt;
        }

        public Boolean getSeatBeltStatus() {
            return seatBeltStatus;
        }

        public void setSeatBeltStatus(Boolean seatBeltStatus) {
            this.seatBeltStatus = seatBeltStatus;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Boolean getSleepStatus() {
            return sleepStatus;
        }

        public void setSleepStatus(Boolean sleepStatus) {
            this.sleepStatus = sleepStatus;
        }

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Integer getStoppedTime() {
            return stoppedTime;
        }

        public void setStoppedTime(Integer stoppedTime) {
            this.stoppedTime = stoppedTime;
        }

        public Integer getStreetSpeed() {
            return streetSpeed;
        }

        public void setStreetSpeed(Integer streetSpeed) {
            this.streetSpeed = streetSpeed;
        }

        public Double getTemp1() {
            return temp1;
        }

        public void setTemp1(Double temp1) {
            this.temp1 = temp1;
        }

        public Double getTemp2() {
            return temp2;
        }

        public void setTemp2(Double temp2) {
            this.temp2 = temp2;
        }

        public Double getTemp3() {
            return temp3;
        }

        public void setTemp3(Double temp3) {
            this.temp3 = temp3;
        }

        public Double getTemp4() {
            return temp4;
        }

        public void setTemp4(Double temp4) {
            this.temp4 = temp4;
        }

        public Integer getTotalFuelConsum() {
            return totalFuelConsum;
        }

        public void setTotalFuelConsum(Integer totalFuelConsum) {
            this.totalFuelConsum = totalFuelConsum;
        }

        public Integer getTotalMileage() {
            return totalMileage;
        }

        public void setTotalMileage(Integer totalMileage) {
            this.totalMileage = totalMileage;
        }

        public Integer getVehicleStatus() {
            return vehicleStatus;
        }

        public void setVehicleStatus(Integer vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }

        public Integer getWeightSensorReading() {
            return weightSensorReading;
        }

        public void setWeightSensorReading(Integer weightSensorReading) {
            this.weightSensorReading = weightSensorReading;
        }

        public Double getWeightVolt() {
            return weightVolt;
        }

        public void setWeightVolt(Double weightVolt) {
            this.weightVolt = weightVolt;
        }

        public Double getWorkingHours() {
            return workingHours;
        }

        public void setWorkingHours(Double workingHours) {
            this.workingHours = workingHours;
        }
    }
}
