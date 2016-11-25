package com.example.sonata.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sonata on 11/21/2016.
 */

public class LessonDate {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lesson_id")
    @Expose
    private String lesson_id;

    @SerializedName("ldate")
    @Expose
    private String ldate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getLdate() {
        return ldate;
    }

    public void setLdate(String ldate) {
        this.ldate = ldate;
    }

    public String getDate()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        String week_day = null;
        try
        {
            startDate = df.parse(ldate);
            week_day = getWeekDay(startDate.getDay());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return week_day;
    }

    private String getWeekDay(int day)
    {
        String strWeekDay = null;
        switch (day) {
            case 1:
                strWeekDay = "Monday";
                break;
            case 2:
                strWeekDay = "Tuesday";
                break;
            case 3:
                strWeekDay = "Wednesday";
                break;
            case 4:
                strWeekDay = "Thursday";
                break;
            case 5:
                strWeekDay = "Friday";
                break;
            case 6:
                strWeekDay = "Saturday";
                break;
            case 7:
                strWeekDay = "Sunday";
                break;
        }

        return strWeekDay;
    }
}
