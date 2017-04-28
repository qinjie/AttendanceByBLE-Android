package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sonata on 11/14/2016.
 */

public class Lesson {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("semester")
    @Expose
    private String semester;

    @SerializedName("module_id")
    @Expose
    private String module_id;

    @SerializedName("subject_area")
    @Expose
    private String subject_area;

    @SerializedName("catalog_number")
    @Expose
    private String catalog_number;

    @SerializedName("class_section")
    @Expose
    private String class_section;

    @SerializedName("component")
    @Expose
    private String component;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("weekday")
    @Expose
    private String weekday;

    @SerializedName("start_time")
    @Expose
    private String start_time;

    @SerializedName("end_time")
    @Expose
    private String end_time;


    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getSubject_area() {
        return subject_area;
    }

    public void setSubject_area(String subject_area) {
        this.subject_area = subject_area;
    }

    public String getCatalog_number() {
        return catalog_number;
    }

    public void setCatalog_number(String catalog_number) {
        this.catalog_number = catalog_number;
    }

    public String getClass_section() {
        return class_section;
    }

    public void setClass_section(String class_section) {
        this.class_section = class_section;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStart_time() {
        return start_time.substring(0, 5);
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time.substring(0, 5);
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getComponent() {
        return component;
    }

}
