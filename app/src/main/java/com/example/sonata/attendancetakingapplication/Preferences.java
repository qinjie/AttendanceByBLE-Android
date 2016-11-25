package com.example.sonata.attendancetakingapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Model.LoginResult;
import com.example.sonata.attendancetakingapplication.Model.StudentInfo;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Sonata on 10/26/2016.
 */

public class Preferences {

    // tags for Shared Preferences to store and retrieve some piece of data from local
    public static final String SharedPreferencesTag = "ATK_BLE_Preferences";
    public static final int SharedPreferences_ModeTag = Context.MODE_PRIVATE;

    public static final int LIST_ITEM_TYPE_1 = 0;
    public static final int LIST_ITEM_TYPE_2 = 1;
    public static final int LIST_ITEM_TYPE_COUNT = 2;

    private static final int CODE_INCORRECT_USERNAME      = 10;
    private static final int CODE_INCORRECT_PASSWORD      = 11;
    private static final int CODE_INCORRECT_DEVICE        = 12;
    private static final int CODE_UNVERIFIED_EMAIL        = 13;
    public  static final int CODE_UNVERIFIED_DEVICE       = 14;
    private static final int CODE_UNVERIFIED_EMAIL_DEVICE = 15;
    private static final int CODE_INVALID_ACCOUNT         = 16;

    public static ProgressDialog loading;
    public static boolean isShownLoading = false;

    private static StudentInfo studentInfo;

    public static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity act)
    {
        activity = act;
    }

    public static void showLoading(final Activity activity, final String title, final String message){
        try {
            if (!isShownLoading) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading = ProgressDialog.show(activity, title, message, false, false);
                        isShownLoading = true;
                    }
                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void dismissLoading(){
        try {
            if (isShownLoading) {

                loading.dismiss();
                isShownLoading = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean obtainedAuCode (Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String auCode = pref.getString("authorizationCode", null);
        if (auCode != null && auCode != "{\"password\":[\"Incorrect username or password.\"]}"){
            return true;
        }
        return false;
    }

    public static void showBadRequestNotificationDialog(final Activity activity, int badRequestCode, int title)
    {
        int message;

        switch (badRequestCode)
        {
            case CODE_INCORRECT_USERNAME:

                break;
            case CODE_INCORRECT_PASSWORD:

                break;
            case CODE_INCORRECT_DEVICE:

                break;
            case CODE_UNVERIFIED_EMAIL:

                break;
            case CODE_UNVERIFIED_DEVICE:

                break;
            case CODE_UNVERIFIED_EMAIL_DEVICE:

                break;
            case CODE_INVALID_ACCOUNT:

                break;
        }
    }

    public static void showNotificationDialog(final Activity activity, int title, int message) {
        new android.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }

    public static String getMac(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public static void setStudentInfo(LoginResult _studentInfo)
    {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("student_id", _studentInfo.getId());
        editor.putString("student_name", _studentInfo.getName());
        editor.putString("student_acad", _studentInfo.getAcad());
        editor.putString("authorizationCode", "Bearer " + _studentInfo.getToken());
        editor.putString("major", _studentInfo.getMajor());
        editor.putString("minor", _studentInfo.getMinor());

        editor.apply();
    }

    public static void clearStudentInfo()
    {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String auCode = pref.getString("authorizationCode", null);

        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.apply();

        ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
        client.logout();

        Intent intent = new Intent(activity, LogInActivity.class);
        activity.startActivity(intent);
    }
}
