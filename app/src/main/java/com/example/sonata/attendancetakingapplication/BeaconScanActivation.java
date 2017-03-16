package com.example.sonata.attendancetakingapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Model.Lesson;
import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;


/**
 * Created by hoanglong on 21-Dec-16.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {

    private static final int LOGIN_ANOTHER_DEVICE = 401;

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconmanager;

    ArrayList<Region> regionList = new ArrayList();
    Lesson lesson = null;
    TimetableResult specificTimetable = null;

    final BootstrapNotifier tmp = this;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mBeaconmanager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        mBeaconmanager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconmanager.setBackgroundMode(true);
        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());

        mBeaconmanager.setBackgroundBetweenScanPeriod(25000l);
        mBeaconmanager.setBackgroundScanPeriod(20000l);

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void didDetermineStateForRegion(int status, Region region) {
        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String studentId = pref.getString("student_id", null);

        String isLogin = pref.getString("isLogin", "false");
        if (isLogin.equals("true")) {
            if (specificTimetable != null) {
                Log.i("Activation-determine", region.getUniqueId() + ": " + status);

                String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
                String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
                Region region2 = new Region("Teacher", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));

                if (region.equals(region2) && status == 1) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("hasTeacher", "true");
                    editor.apply();
                }

                String hasTeacher = pref.getString("hasTeacher", "");
                if (hasTeacher.equals("true") && status == 1 && (!region.equals(region2))) {
                    Log.i("Presenttttttt", region.getUniqueId() + " : " + status);
                    Preferences.studentNotify(getBaseContext(), "Attendance", "Student Present", Integer.parseInt(studentId));
                }
            }
        }
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

        if (specificTimetable != null) {
            String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
            String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
            Region region2 = new Region("Teacher", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));

            if (region.equals(region2)) {
                SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("hasTeacher", "false");
                editor.apply();
                Log.i("Outgoing", region.getUniqueId());
            }
        }

    }


    //this will re-run after every 10 seconds
    private int mInterval = 10000;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin", "false");

            if (isLogin.equals("true")) {

                String auCode = pref.getString("authorizationCode", null);
                final String studentId = pref.getString("student_id", null);

                Date aDate = new Date();
                SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateObj = curFormatter.format(aDate);

//                JsonObject obj = new JsonObject();
//                obj.addProperty("student_id", studentId);
//                obj.addProperty("datetime", dateObj);

                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse("{\"datetime\": \"" + dateObj + "\"}").getAsJsonObject();

                ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                Call<TimetableResult> call = client.getLessonAtSpecificTime(obj);
                call.enqueue(new Callback<TimetableResult>() {
                    @Override
                    public void onResponse(Call<TimetableResult> call, Response<TimetableResult> response) {
                        try {
                            if (response.body() != null) {
                                specificTimetable = response.body();
                                lesson = response.body().getLesson();
                            } else {
                                if (response.code() == LOGIN_ANOTHER_DEVICE) {
                                    SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("isLogin", "false");
                                    editor.apply();

                                    Preferences.studentNotifyWithLongText(getBaseContext(), "Detected another login", "Your account has been sign in from another device. You will automatically sign out. Click here to sign in again.", Integer.parseInt(studentId));
                                }

                                specificTimetable = null;
                                lesson = null;

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<TimetableResult> call, Throwable t) {
                        if (!checkInternetOn()) {
                            Preferences.notify(getBaseContext(), "Attendance taking", "Please check your internet connection.");
                        } else {
                            Preferences.notify(getBaseContext(), "Attendance taking", "Server is busy. Please try again later.");

                        }

                    }
                });

                if (lesson != null) {
                    Region region = new Region("AllStudent", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), null, null);

                    String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
                    String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
                    Region region2 = new Region("Teacher", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));

                    regionList.add(region);
                    regionList.add(region2);
                }

                regionBootstrap = new RegionBootstrap(tmp, regionList);

            } else {
                if (regionList != null && regionList.size() > 0) {
                    for (Region tmp : regionList) {
                        try {
                            mBeaconmanager.stopMonitoringBeaconsInRegion(tmp);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


            Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2("10001")
                    .setId3("20001")
                    .setManufacturer(0x015D)
                    //Estimo company code
                    //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[] {0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
            BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);

            mHandler.postDelayed(mStatusChecker, mInterval);

        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    //Check the internet activity on or not
    //Return true if ON. False if OFF
    public boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }
}