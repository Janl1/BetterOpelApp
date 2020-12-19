package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class ChargeStatus {

    @SerializedName("charging")
    public String charging;

    @SerializedName("timestamp")
    public double timestamp;
}
