package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vehicle {

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
}
