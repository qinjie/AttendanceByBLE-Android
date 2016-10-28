package com.example.sonata.attendancetakingapplication.Model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Sonata on 10/26/2016.
 */

public class LoginInfo {
    String username = "NULL";
    String password = "NULL";
    String device_hash = "NULL";

    public LoginInfo(String _username, String _password, Context context){
        username = _username;
        password = _password;
        device_hash = getMac(context);
    }

    private String getMac(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }
}
