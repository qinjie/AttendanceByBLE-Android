package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by Sonata on 10/31/2016.
 */

public class Venue {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("name")
    @Expose
    private String name;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress()
    {
        //if there is internet this will display
        if(name!=null){
            address = name + " Block " + location.substring(0, 2) + " #" + location.substring(3, 8);
        }

        //if there is no internet, address will be null, setAddress first
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
