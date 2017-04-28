package com.example.sonata.attendancetakingapplication.OrmLite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hoanglong on 21-Mar-17.
 */

@DatabaseTable
public class SubjectDateTime {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String lesson_date_id;

    @DatabaseField
    private String startTime;

    @DatabaseField
    private String endTime;

    @DatabaseField
    private String lesson_date;

    @DatabaseField(foreign=true,foreignAutoRefresh=true)
    private Subject subject;


    public SubjectDateTime() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLesson_date_id() {
        return lesson_date_id;
    }

    public void setLesson_date_id(String lesson_date_id) {
        this.lesson_date_id = lesson_date_id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLesson_date() {
        return lesson_date;
    }

    public void setLesson_date(String lesson_date) {
        this.lesson_date = lesson_date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
