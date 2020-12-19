package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Charges {

    @SerializedName("data")
    public List<Charge> data;
    @SerializedName("count")
    public int count;
    @SerializedName("total")
    public int total;
    @SerializedName("page")
    public int page;
    @SerializedName("pageCount")
    public int pageCount;
}
