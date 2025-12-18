package com.example.dawanlocations.data.local;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "locations")
public class LocationEntity {
    @PrimaryKey public int id;
    public String name;
    public String address;
    public String city;
    public String postalCode;
    public double latitude;
    public double longitude;
}
