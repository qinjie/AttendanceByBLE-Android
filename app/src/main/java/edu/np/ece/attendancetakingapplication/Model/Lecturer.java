package edu.np.ece.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sonata on 11/14/2016.
 */

public class Lecturer {

    @SerializedName("id")
    @Expose
    private String id;

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

    @SerializedName("beacon")
    @Expose
    private UserBeacon beacon;

    @SerializedName("office")
    @Expose
    private String office;

    @SerializedName("phone")
    @Expose
    private String phone;

    public  String getOffice(){return office;}

    public  void setOffice(String office){this.office=office;}

    public  String getPhone(){return phone;}

    public  void setPhone(String phone){this.phone=phone;}

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
