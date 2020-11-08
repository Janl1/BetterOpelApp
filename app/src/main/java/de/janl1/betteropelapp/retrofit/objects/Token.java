package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("id")
    public String id;
    @SerializedName("access_token")
    public String access_token;
    @SerializedName("expires_in")
    public String expires_in;
    @SerializedName("token_type")
    public String token_type;
}
