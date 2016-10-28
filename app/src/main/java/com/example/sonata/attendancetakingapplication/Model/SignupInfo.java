package com.example.sonata.attendancetakingapplication.Model;

import android.content.Context;

import com.example.sonata.attendancetakingapplication.Preferences;

/**
 * Created by Sonata on 10/28/2016.
 */

public class SignupInfo {
    String username = "NULL";
    String password = "NULL";
    String email = "NULL";
    String student_id = "NULL";
    String device_hash = "NULL";

    public SignupInfo(String _username, String _password, String _email, String _student_id, Context context){
        username = _username;
        password = _password;
        email = _email;
        student_id = _student_id;
        device_hash = Preferences.getMac(context);
    }
}
