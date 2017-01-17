package com.example.sonata.attendancetakingapplication.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.google.gson.JsonObject;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sonata on 12/21/2016.
 */

public class BeaconRangingService extends Service implements BeaconConsumer {
    private final String TAG = BeaconRangingService.class.getSimpleName();
    private BeaconManager beaconManager;

    private ArrayList<String> beaconList;

    private static final String INTENT_NAME_TOAST = "edu.np.ece.beaconmonitor.toast";
//    private static final String LESSON_UUID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";
//    private static final String LESSON_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
//    private static final String LESSON_NAME = "LessonBeacon";

    private static boolean isTimeUp = false;
    private static boolean isDataSent = false;

    private Context context;

    public BeaconRangingService() {
        super();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        beaconList = new ArrayList<>();
        context = getApplicationContext();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void removeRangingRegion()
    {
        //-- Remove any region
        try {
            Region anyRegion = new Region("AnyRangingBeacon", null, null, null);
            beaconManager.stopRangingBeaconsInRegion(anyRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect()");

        removeRangingRegion();

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion(): " + region.getUniqueId());

                // Notify to finish collecting data
//                Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
//                broadcastIntent.putExtra("message", "didRangeBeaconsInRegion() is called.");
//                sendBroadcast(broadcastIntent);

                if (region.getUniqueId().compareToIgnoreCase(Preferences.getLessonBeaconName(context)) == 0)
                {
                    saveBeaconInfo(collection);
                }
            }
        });

        try {
            String LESSON_UUID = Preferences.getLessonBeaconUUID(context);
            String LESSON_NAME = Preferences.getLessonBeaconName(context);
            Identifier identifier = Identifier.parse(LESSON_UUID);
            Region lessonRegion = new Region(LESSON_NAME, identifier, null, null);
            beaconManager.startRangingBeaconsInRegion(lessonRegion);

            Preferences.notify(context, "ATK_BLE Process", "Start timer in 30 seconds!");
            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    isTimeUp = true;
                }

            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBeaconInfo(String major, String minor)
    {
        beaconList.add(major);
        beaconList.add(minor);
    }

    private boolean isNewBeaconFound(String major, String minor)
    {
        int index = 0;
        while (index < beaconList.size())
        {
            if (major.compareToIgnoreCase(beaconList.get(index)) == 0
                    && minor.compareToIgnoreCase(beaconList.get(index+1)) == 0)
            {
                return false;
            }

            index += 2;
        }

        return true;
    }

    private void sendDataToServer()
    {
        Log.d(TAG, "sendDataToServer()");
        isDataSent = true;

        Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
        if (beaconList.size() > 0)
        {
            String content = new String("Sending data to Server\n"
                    + "(" + Preferences.getStudentBeaconMajor(context) + ", " + Preferences.getStudentBeaconMinor(context) + ")\n"
                    + "(" + beaconList.get(0) + ", " + beaconList.get(1) + ")");
            broadcastIntent.putExtra("message", content);
            sendBroadcast(broadcastIntent);

            Preferences.notify(context, "ATK_BLE Process", content);
        }
        else
        {
            broadcastIntent.putExtra("message", "Did not find any beacons in range");
            sendBroadcast(broadcastIntent);

            Preferences.notify(context, "ATK_BLE Process", "No virtual beacon in range");
            return;

        }

        int count = 2;
        String temp = null;
        String major = "major";
        String minor = "minor";
        JsonObject toUp = new JsonObject();

        toUp.addProperty("major1", Preferences.getStudentBeaconMajor(context));
        toUp.addProperty("minor1", Preferences.getStudentBeaconMinor(context));

        for(int i = 0; i < beaconList.size(); i++)
        {
            if (i % 2 == 0)
            {
                temp = major + String.valueOf(count);
                toUp.addProperty(temp, beaconList.get(i));
            }
            else if (i % 2 == 1)
            {
                temp = minor + String.valueOf(count);
                toUp.addProperty(temp, beaconList.get(i));
                count++;
            }
        }

        Preferences.showLoading(Preferences.getActivity(), "Testing", "Sending data to server...");
        String auCode = "Bearer EU9nhMR0QfFUuxFoRYpboeYc3Ev4_Zvb";
        ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
        Call<ResponseBody> call = client.pushStudentArrayList(toUp);
        call.enqueue (new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Preferences.dismissLoading();

                    int messageCode = response.code();
                    Log.d(TAG, "Attendance recorded successfully: " + messageCode);

                    Preferences.notify(getApplicationContext(), "ATK_BLE Process", "Send data to server successfully!");
//                    if (messageCode == 200) // SUCCESS
//                    {
//                        Preferences.notify(getApplicationContext(), "ATK_BLE", "Attendance recorded successfully for subject MAC101.");
//                    }
//                    else
//                    {
//
//                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveBeaconInfo(Collection<Beacon> collection)
    {
        for(Beacon beacon : collection)
        {
            if (beaconList.size() == 0 || isNewBeaconFound(beacon.getId2().toString(), beacon.getId3().toString()))
            {
                saveBeaconInfo(beacon.getId2().toString(), beacon.getId3().toString());
            }
        }

        if (isTimeUp == true && isDataSent == false)
        {
            removeRangingRegion();

            // Notify to finish collecting data
            Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
            broadcastIntent.putExtra("message", "Finish collecting data");
            sendBroadcast(broadcastIntent);

            sendDataToServer();

            try
            {
                Identifier identifier = Identifier.parse(Preferences.getLessonBeaconUUID(context));
                Region lessonRegion = new Region(Preferences.getLessonBeaconName(context), identifier, null, null);
                beaconManager.stopRangingBeaconsInRegion(lessonRegion);
                beaconManager.unbind(this);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }

            stopSelf();
        }
    }
}
