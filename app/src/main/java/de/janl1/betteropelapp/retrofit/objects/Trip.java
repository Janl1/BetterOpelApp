package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trip {

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("odometer")
    public double odometer;

    @SerializedName("distance")
    public double distance;

    @SerializedName("usedkWh")
    public double usedkWh;

    @SerializedName("startTime")
    public double startTime;

    @SerializedName("startLevel")
    public double startLevel;

    @SerializedName("endTime")
    public double endTime;

    @SerializedName("endLevel")
    public double endLevel;

    @SerializedName("cardId")
    public String cardId;

    @SerializedName("avgPower")
    public double avgPower;

    @SerializedName("avgTemp")
    public double avgTemp;

    @SerializedName("items")
    public List<TripItem> items;

    @SerializedName("avgSpeed")
    public double avgSpeed;

    @SerializedName("cost")
    public double cost;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;

    @SerializedName("id")
    public String id;

}
