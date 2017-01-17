package com.example.sonata.attendancetakingapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Sonata on 11/25/2016.
 */

public class SharedPreferenceUtils {

    public static final String PREFS_NAME = "ATK_BLE_Preferences";
    public static final String LECTURERS  = "ATK_BLE_Lecturers_Beacon_Info";
    public static final String STUDENTS   = "ATK_BLE_Students_Beacon_Info";

    public SharedPreferenceUtils() {
        super();
    }

//    // This four methods are used for maintaining favorites.
//    public void saveBeaconInfo(Context context, List<Product> favorites, String infoType) {
//        SharedPreferences settings;
//        SharedPreferences.Editor editor;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        editor = settings.edit();
//
//        Gson gson = new Gson();
//        String jsonFavorites = gson.toJson(favorites);
//
//        editor.putString(infoType, jsonFavorites);
//
//        editor.commit();
//    }
}
