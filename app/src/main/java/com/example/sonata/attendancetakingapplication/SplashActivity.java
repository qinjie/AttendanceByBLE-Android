package com.example.sonata.attendancetakingapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.example.sonata.attendancetakingapplication.Utils.ConnectivityUtils;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;

public class SplashActivity extends Activity{

//    private final static int REQUEST_ENABLE_BT = 1;

//    protected static final String TAG = "MonitoringActivity";
//    public static BeaconManager beaconManager;
//    private BackgroundPowerSaver backgroundPowerSaver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences.setActivity(this);
        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String islogin = pref.getString("isLogin", "");
        if (!islogin.equals("true")) {
            if (!ConnectivityUtils.isConnected(this)) {
                ConnectivityUtils.showConnectionFailureDialog(this);
                return;
            }
            obtainedAuCode();
        } else {
            startAuthenticatedArea();
        }

//        beaconManager = BeaconManager.getInstanceForApplication(this);
//        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
//        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
//        // beaconManager.getBeaconParsers().add(new BeaconParser().
//        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//        beaconManager.bind(this);

    }

    private void obtainedAuCode() {
        Preferences.showLoading(SplashActivity.this, "Initialize", "Checking authentication...");
        try {
            SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            String expand = new String("lesson,lesson_date,lecturers,venue");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        Preferences.dismissLoading();

                        int code = response.code();
                        if (code == 200) {
                            startAuthenticatedArea();
                        } else {
                            startLogin();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//            }
//            else
//            {
//                finish();
//            }
//        }
    }

    private void startLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAuthenticatedArea() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }


//    @Override
//    public void onBeaconServiceConnect() {
//        beaconManager.addMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//                Log.i("yoloooooo", region.getId2() + " | " + region.getId3() + " hahahahha");
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                Log.i(TAG, "I no longer see an beacon");
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
//            }
//        });
//
//        try {
//            beaconManager.startMonitoringBeaconsInRegion(new Region( "test",  Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Identifier.parse("24890"), Identifier.parse("6699")));
//        } catch (RemoteException e) {
//        }
//    }

}
