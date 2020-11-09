package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vehicle implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("scopes")
    public List<String> scopes;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("vin")
    public String vin;
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("manufacture")
    public String manufacture;
    @SerializedName("model")
    public String model;
    @SerializedName("year")
    public int year;
    @SerializedName("kWh")
    public int kWh;
    @SerializedName("deployment")
    public String deployment;
    @SerializedName("timeStartSleep")
    public int timeStartSleep;
    @SerializedName("timeWaitSleep")
    public int timeWaitSleep;
    @SerializedName("chargesGPS")
    public boolean chargesGPS;
    @SerializedName("tripsGPS")
    public boolean tripsGPS;
    @SerializedName("fixed")
    public boolean fixed;
    @SerializedName("wakeUp")
    public boolean wakeUp;
    @SerializedName("fixedTime")
    public int fixedTime;
    @SerializedName("wakeUpTime")
    public int wakeUpTime;

    public String getId() {
        return id;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getVin() {
        return vin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getManufacture() {
        return manufacture;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getkWh() {
        return kWh;
    }

    public String getDeployment() {
        return deployment;
    }

    public int getTimeStartSleep() {
        return timeStartSleep;
    }

    public int getTimeWaitSleep() {
        return timeWaitSleep;
    }

    public boolean isChargesGPS() {
        return chargesGPS;
    }

    public boolean isTripsGPS() {
        return tripsGPS;
    }

    public boolean isFixed() {
        return fixed;
    }

    public boolean isWakeUp() {
        return wakeUp;
    }

    public int getFixedTime() {
        return fixedTime;
    }

    public int getWakeUpTime() {
        return wakeUpTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setkWh(int kWh) {
        this.kWh = kWh;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public void setTimeStartSleep(int timeStartSleep) {
        this.timeStartSleep = timeStartSleep;
    }

    public void setTimeWaitSleep(int timeWaitSleep) {
        this.timeWaitSleep = timeWaitSleep;
    }

    public void setChargesGPS(boolean chargesGPS) {
        this.chargesGPS = chargesGPS;
    }

    public void setTripsGPS(boolean tripsGPS) {
        this.tripsGPS = tripsGPS;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void setWakeUp(boolean wakeUp) {
        this.wakeUp = wakeUp;
    }

    public void setFixedTime(int fixedTime) {
        this.fixedTime = fixedTime;
    }

    public void setWakeUpTime(int wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }
}
