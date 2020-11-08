package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesResponseDTO {

    @SerializedName("data")
    public List<Vehicle> data;
    @SerializedName("count")
    public int count;
    @SerializedName("total")
    public int total;
    @SerializedName("page")
    public int page;
    @SerializedName("pageCount")
    public int pageCount;
}
