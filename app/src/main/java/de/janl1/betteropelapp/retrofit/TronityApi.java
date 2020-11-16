package de.janl1.betteropelapp.retrofit;

import de.janl1.betteropelapp.retrofit.objects.Battery;
import de.janl1.betteropelapp.retrofit.objects.Bulk;
import de.janl1.betteropelapp.retrofit.objects.Charge;
import de.janl1.betteropelapp.retrofit.objects.Location;
import de.janl1.betteropelapp.retrofit.objects.Odometer;
import de.janl1.betteropelapp.retrofit.objects.Token;
import de.janl1.betteropelapp.retrofit.objects.TokenRequestDTO;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TronityApi {

    @POST("/oauth/authentication")
    Call<Token> auth(@Body TokenRequestDTO tokenRequestDTO);

    @GET("/v1/vehicles")
    Call<VehiclesResponseDTO> getVehicles(@Header("Authorization") String token);

    @GET("/v1/vehicles/{vehicleid}/location")
    Call<Location> getLocation(@Header("Authorization") String token, @Path("vehicleid") String vehicleId);

    @GET("/v1/vehicles/{vehicleid}/battery")
    Call<Battery> getBattery(@Header("Authorization") String token, @Path("vehicleid") String vehicleId);

    @GET("/v1/vehicles/{vehicleid}/odometer")
    Call<Odometer> getOdometer(@Header("Authorization") String token, @Path("vehicleid") String vehicleId);

    @GET("/v1/vehicles/{vehicleid}/charge")
    Call<Charge> getChargingSate(@Header("Authorization") String token, @Path("vehicleid") String vehicleId);

    @GET("/v1/vehicles/{vehicleid}/bulk")
    Call<Bulk> getBulkInformation(@Header("Authorization") String token, @Path("vehicleid") String vehicleId);
}
