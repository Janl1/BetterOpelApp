package de.janl1.betteropelapp.retrofit;

import de.janl1.betteropelapp.retrofit.objects.Token;
import de.janl1.betteropelapp.retrofit.objects.TokenRequestDTO;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TronityApi {

    @POST("/oauth/authentication")
    Call<Token> auth(@Body TokenRequestDTO tokenRequestDTO);

    @GET("/v1/vehicles")
    Call<VehiclesResponseDTO> getVehicles(@Header("Authorization") String token);
}
