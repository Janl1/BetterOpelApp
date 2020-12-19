package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Charge {

    @SerializedName("id")
    public String id;

    @SerializedName("startTime")
    public double startTime;

    @SerializedName("startLevel")
    public double startLevel;

    @SerializedName("endTime")
    public double endTime;

    @SerializedName("endLevel")
    public double endLevel;

    @SerializedName("odometer")
    public double odometer;

    @SerializedName("comment")
    public String comment;

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("type")
    public String type;

    @SerializedName("kWh")
    public double kWh;

    @SerializedName("batterykWh")
    public double batterykWh;

    @SerializedName("max")
    public double max;

    @SerializedName("range")
    public double range;

    @SerializedName("ac")
    public boolean ac;

    @SerializedName("cost")
    public double cost;

    @SerializedName("co2")
    public double co2;

    @SerializedName("renewable")
    public double renewable;

    @SerializedName("location")
    public ChargeLocation location;

    @SerializedName("pointId")
    public String pointId;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;

    @SerializedName("items")
    public List<ChargeItem> items;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;


}
