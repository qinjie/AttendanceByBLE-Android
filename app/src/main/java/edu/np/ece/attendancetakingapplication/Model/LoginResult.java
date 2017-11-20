package edu.np.ece.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sonata on 11/17/2016.
 */

public class LoginResult {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("acad")
    @Expose
    private String acad;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("major")
    @Expose
    private String major;

    @SerializedName("minor")
    @Expose
    private String minor;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("device_hash")
    @Expose
    private String device_hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_hash() {
        return device_hash;
    }

    public void setDevice_hash(String device_hash) {
        this.device_hash = device_hash;
    }
}
