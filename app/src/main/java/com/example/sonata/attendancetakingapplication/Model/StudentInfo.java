package com.example.sonata.attendancetakingapplication.Model;

import org.json.JSONObject;

/**
 * Created by Sonata on 10/28/2016.
 */

public class StudentInfo {
    private String student_Id;
    private String student_Name;
    private String student_Acad;

    public StudentInfo(JSONObject data)
    {
        try
        {
            student_Id   = data.getString("id");
            student_Name = data.getString("name");
            student_Acad = data.getString("acad");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getStudent_Id()   { return student_Id;   }
    public String getStudent_Name() { return student_Name; }
    public String getStudent_Acad() { return student_Acad; }
}
