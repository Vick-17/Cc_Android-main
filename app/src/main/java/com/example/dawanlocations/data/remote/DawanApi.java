package com.example.dawanlocations.data.remote;


import com.example.dawanlocations.data.remote.model.LocationDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;


public interface DawanApi {
    @GET("public/location/")
    Call<List<LocationDto>> getLocations();
}