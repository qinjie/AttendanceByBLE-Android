package com.example.sonata.attendancetakingapplication.OrmLite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hoanglong on 21-Mar-17.
 */

@DatabaseTable
public class Time {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String datetime;

    @DatabaseField(foreign=true,foreignAutoRefresh=true)
    private Subject subjectList;


    public Time() {

    }

    public Time(int id, String datetime) {
        this.id = id;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Subject getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(Subject subjectList) {
        this.subjectList = subjectList;
    }
}
