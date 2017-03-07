package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 07-Mar-17.
 */

public class LectureBeacon {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("major")
    @Expose
    private String major;

    @SerializedName("minor")
    @Expose
    private String minor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
}
