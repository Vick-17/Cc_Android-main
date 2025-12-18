package com.example.dawanlocations.data.remote.model;


import com.google.gson.annotations.SerializedName;


public class LocationDto {
    @SerializedName("id") public int id;
    @SerializedName("name") public String name;
    @SerializedName("address") public String address;
    @SerializedName("city") public String city;
    @SerializedName("postalCode") public String postalCode;
    @SerializedName("latitude") public double latitude;
    @SerializedName("longitude") public double longitude;
}