package com.example.sonata.attendancetakingapplication.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Preferences;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class BeaconConsumingService extends Service implements BeaconConsumer {
    private final String TAG = BeaconConsumingService.class.getSimpleName();
    private BeaconManager beaconManager;

    private static boolean isProcessStarted = false;
    private static boolean checkDate = true;
    private static boolean checkTime = true;

//    private static final String REGION_NAME = "MonitoringBeacon";
//    private static final String REGION_UUID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";
//    private static final String REGION_UUID = "23A01AF0-232A-4518-9C0E-323FB773F5EF";
//    private static final String REGION_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final String INTENT_NAME_TOAST = "edu.np.ece.beaconmonitor.toast";

//    Context context;

    public BeaconConsumingService() {
        super();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

//        context = getBaseContext();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        beaconManager.unbind(this);

        //-- Ask broadcast Receiver to restart service
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);

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

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect()");

        //-- Remove any region
        try {
            Region anyRegion = new Region("AnyBeacon", null, null, null);
            beaconManager.stopMonitoringBeaconsInRegion(anyRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "didEnterRegion(): " + region.getUniqueId());

                if (region.getUniqueId().compareTo(Preferences.getVenueBeaconName( getBaseContext())) == 0)
                {
                    Preferences.setEnterVenueRegion(true);
                    //TODO Check date of the lesson in local data
                    if (checkDate == true)
                    {
                        //TODO Check time of the lesson in local data with current time
                        if (checkTime == true)
                        {
                            //TODO Check lesson is processed or not
                            if (isProcessStarted == false)
                            {
                                Preferences.notify(getBaseContext(), "ATK_BLE Process", "Enter Venue Beacon!");
                                isProcessStarted = true;
                                Intent intent = new Intent(getApplicationContext(), BeaconTransmittingService.class);
                                startService(intent);

                                //-- Test
                                Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
                                broadcastIntent.putExtra("message", "service.didEnterRegion() " + region.getUniqueId() );
                                sendBroadcast(broadcastIntent);
                            }
                        }
                    }
                }
                else if (region.getUniqueId().compareTo(Preferences.getLessonBeaconName( getBaseContext())) == 0)
                {
                    Preferences.notify(getBaseContext(), "ATK_BLE Process", "Found Virtual Beacon in range!");

                    if (Preferences.getEnterVenueRegion() == true)
                    {
                        Preferences.notify(getBaseContext(), "ATK_BLE Process", "Found Virtual Beacon in range!");
                        Intent intent = new Intent(getApplicationContext(), BeaconRangingService.class);
                        startService(intent);
                    }
                    else
                    {
                        Preferences.notify(getBaseContext(), "ATK_BLE Process", "You are not in the class!");
                    }

                    //-- Test
                    Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
                    broadcastIntent.putExtra("message", "service.foundVirtualBeacon in range " + region.getUniqueId() );
                    sendBroadcast(broadcastIntent);
                }


            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "didExitRegion(): " + region.getUniqueId());

                Preferences.setEnterVenueRegion(false);

                Preferences.notify(getBaseContext(), "ATK_BLE Process", "Exit Venue Beacon!");

                //-- Test
                Intent broadcastIntent = new Intent(INTENT_NAME_TOAST);
                broadcastIntent.putExtra("message", "service.didExitRegion()");
                sendBroadcast(broadcastIntent);
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }
        });

        try {
            // Venue beacon monitoring
            String Region_Name = Preferences.getVenueBeaconName( getBaseContext());
//            String Region_UUID = Preferences.getVenueBeaconUUID( getBaseContext());
//            Region monitoringVenueRegion = new Region(Region_Name, Identifier.parse(Region_UUID), null, null);
//            beaconManager.startMonitoringBeaconsInRegion(monitoringVenueRegion);
//
//            // Student beacon monitoring
//            String Lesson_Name = Preferences.getLessonBeaconName( getBaseContext());
//            String Lesson_UUID = Preferences.getLessonBeaconUUID( getBaseContext());
//            Region monitoringLessonRegion = new Region(Lesson_Name, Identifier.parse(Lesson_UUID), null, null);
//            beaconManager.startMonitoringBeaconsInRegion(monitoringLessonRegion);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}