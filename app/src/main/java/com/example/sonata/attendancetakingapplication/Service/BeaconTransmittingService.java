package com.example.sonata.attendancetakingapplication.Service;

import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.Utils.BleUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;
import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;

public class BeaconTransmittingService extends Service implements BeaconConsumer {

    final static String TAG = BeaconTransmittingService.class.getSimpleName();
    private BeaconTransmitter mBeaconTransmitter;
    private BeaconManager beaconManager;
    private Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        // Sets up to transmit as an iBeacon beacon.
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BleUtil.LAYOUT_IBEACON);
        mBeaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect()");

        String UUID  = Preferences.getLessonBeaconUUID(context);
        String major = Preferences.getStudentBeaconMajor(context);
        String minor = Preferences.getStudentBeaconMinor(context);
        startBeacon(UUID, major, minor);
    }

    private void startBeacon(String uuid, String major, String minor) {
        Log.i(TAG, "startBeacon()");

        Beacon beacon = new Beacon.Builder()
                .setId1(uuid)
                .setId2(major)
                .setId3(minor)
                .setManufacturer(0x0118) // https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                .setBluetoothName("TestBeacon")
                .build();

        mBeaconTransmitter.setAdvertiseMode(ADVERTISE_MODE_LOW_LATENCY);
        mBeaconTransmitter.setAdvertiseTxPowerLevel(ADVERTISE_TX_POWER_HIGH);
        mBeaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Advertisement start failed with code: " + errorCode);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i(TAG, "Advertisement start succeeded.");

                Preferences.notify(context, "Beacon_Transmitter_Process", "startBeacon(): "
                        + Preferences.getStudentBeaconMajor(context) + " " + Preferences.getStudentBeaconMinor(context));
            }
        });

        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                stopBeacon();
            }

        }.start();
    }

    private void stopBeacon() {
        Log.i(TAG, "stopBeacon()");

        Preferences.notify(context, "Beacon_Transmitter_Process", "stopBeacon()");

        if (mBeaconTransmitter == null) {
            return;
        }
        if (mBeaconTransmitter.isStarted())
            mBeaconTransmitter.stopAdvertising();
        mBeaconTransmitter = null;
    }
}
