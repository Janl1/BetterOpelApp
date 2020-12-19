package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChargeLocation {

    @SerializedName("type")
    public String type;

    @SerializedName("coordinates")
    public List<Double> coordinates;
}
