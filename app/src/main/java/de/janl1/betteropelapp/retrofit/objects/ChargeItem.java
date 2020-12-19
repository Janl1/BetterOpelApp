package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class ChargeItem {

    @SerializedName("id")
    public double id;

    @SerializedName("level")
    public double level;

    @SerializedName("usableLevel")
    public double usableLevel;

    @SerializedName("range")
    public double range;

    @SerializedName("speed")
    public double speed;

    @SerializedName("power")
    public double power;

    @SerializedName("chargerPower")
    public double chargerPower;

    @SerializedName("phases")
    public double phases;

    @SerializedName("voltage")
    public double voltage;

    @SerializedName("current")
    public double current;

    @SerializedName("energyAdded")
    public double energyAdded;

    @SerializedName("milesAdded")
    public double milesAdded;

    @SerializedName("socMax")
    public double socMax;

    @SerializedName("consumption")
    public double consumption;

    @SerializedName("outTemp")
    public double outTemp;

    @SerializedName("inTemp")
    public double inTemp;

    @SerializedName("passTemp")
    public double passTemp;

    @SerializedName("limitSoc")
    public double limitSoc;

    @SerializedName("elevation")
    public double elevation;

    @SerializedName("fan")
    public double fan;

    @SerializedName("climate")
    public boolean climate;

    @SerializedName("seatHeating")
    public boolean seatHeating;

    @SerializedName("preconditioning")
    public boolean preconditioning;

    @SerializedName("charging")
    public String charging;

    @SerializedName("driving")
    public boolean driving;

    @SerializedName("wakeup")
    public boolean wakeup;

    @SerializedName("supercharger")
    public boolean supercharger;

    @SerializedName("doorLocked")
    public boolean doorLocked;

    @SerializedName("sentryMode")
    public boolean sentryMode;

    @SerializedName("windowsLocked")
    public boolean windowsLocked;

    @SerializedName("odometer")
    public double odometer;

    @SerializedName("vehicleStatusTime")
    public double vehicleStatusTime;

    @SerializedName("vehicleLocationTime")
    public double vehicleLocationTime;

    @SerializedName("tripCurrentId")
    public double tripCurrentId;

    @SerializedName("tripTravelTime")
    public double tripTravelTime;

    @SerializedName("tripTimestamp")
    public double tripTimestamp;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;

    @SerializedName("softwareUpdate")
    public String softwareUpdate;
}
