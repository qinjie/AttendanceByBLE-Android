package com.example.sonata.attendancetakingapplication.JobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

/**
 * Created by hoanglong on 27-Mar-17.
 */

public class BeaconJobScheduler extends JobService {

    private JobAsyncTask mJobAsyncTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mJobAsyncTask = new JobAsyncTask();
        mJobAsyncTask.execute(jobParameters);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mJobAsyncTask != null) {
            if (mJobAsyncTask.isCancelled()) {
                return true;
            }
            mJobAsyncTask.cancel(true);
        }
        return false;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {
        // JobParameters contains the parameters used to configure/identify the job.
        // You do not create this object yourself,
        // instead it is handed in to your application by the System.

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            final Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2("666")
                    .setId3("222")
                    .setManufacturer(0x015D)
                    //Estimo company code
                    //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
            final BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);

            //Wait for 10 seconds to finish transmit
            SystemClock.sleep(5000);
            beaconTransmitter.stopAdvertising();


            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters, false);
        }
    }
}
