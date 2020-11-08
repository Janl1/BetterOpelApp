package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesResponseDTO {

    @SerializedName("data")
    List<Vehicle> data;
    @SerializedName("count")
    int count;
}
