package edu.np.ece.attendancetakingapplication;

import android.app.Application;
import android.os.Handler;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.TimetableResult;

/**
 * Created by dellpc on 10/30/2017.
 */

public class Beacon_Scan_Activation extends Application implements BootstrapNotifier {

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;

    ArrayList<Region> regionList  = new ArrayList<>();
    public static List<TimetableResult> timetableList;

    public String record_time;

    final BootstrapNotifier tmp = this;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
