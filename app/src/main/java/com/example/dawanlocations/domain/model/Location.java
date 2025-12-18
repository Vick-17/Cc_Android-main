package com.example.dawanlocations.domain.model;


public class Location {
    public final int id;
    public final String name;
    public final String address;
    public final String city;
    public final String postalCode;
    public final double latitude;
    public final double longitude;


    public Location(int id, String name, String address
            , String city, String postalCode
            , double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}