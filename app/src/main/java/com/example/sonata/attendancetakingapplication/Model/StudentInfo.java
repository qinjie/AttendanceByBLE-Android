package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sonata on 11/14/2016.
 */

public class StudentInfo {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("card")
    @Expose
    private String card;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("acad")
    @Expose
    private String acad;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("beacon_user")
    @Expose
    private UserBeacon beacon;

    public StudentInfo() {
    }

    public StudentInfo(String id, String card, String name, String acad, String email, String user_id, UserBeacon beacon) {
        this.id = id;
        this.card = card;
        this.name = name;
        this.acad = acad;
        this.email = email;
        this.user_id = user_id;
        this.beacon = beacon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcad() {
        return acad;
    }

    public void setAcad(String acad) {
        this.acad = acad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(UserBeacon beacon) {
        this.beacon = beacon;
    }
}
