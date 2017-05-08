package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sonata on 11/23/2016.
 */

public class HistoricalResult {
    @SerializedName("lesson_name")
    @Expose
    private String lesson_name;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("presented")
    @Expose
    private String presented;

    @SerializedName("absented")
    @Expose
    private String absented;

    @SerializedName("late")
    @Expose
    private String late;


    public String getLesson_name() {
        return lesson_name;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPresented() {
        return presented;
    }

    public void setPresented(String presented) {
        this.presented = presented;
    }

    public String getAbsented() {
        return absented;
    }

    public void setAbsented(String absented) {
        this.absented = absented;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }
}
