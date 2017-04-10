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
        //return false the OS will auto kill job, if return true we have to decide to stop by yourself
        // by calling jobScheduler.cancelAll()
        // method onStopJob will be called
        return true;
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
            if(params[0].getExtras().getString("subject-uuid") != null){

                final Beacon.Builder beaconBuilder = new Beacon.Builder();
                beaconBuilder.setId1(params[0].getExtras().getString("subject-uuid"));
                beaconBuilder.setId2(params[0].getExtras().getString("user-major"));
//                beaconBuilder.setId3(params[0].getExtras().getString("user-minor"));
                beaconBuilder.setId3("666");

                beaconBuilder.setManufacturer(0x015D);
                //Estimo company code
                //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
                beaconBuilder.setTxPower(-59);
                beaconBuilder.setDataFields(Arrays.asList(new Long[]{0l}));
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
                final BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
//                beaconTransmitter.stopAdvertising();

                beaconTransmitter.startAdvertising(beaconBuilder.build());

                //Wait for 10 seconds to finish transmit
//                SystemClock.sleep(30000);
                SystemClock.sleep(5000);

                beaconTransmitter.stopAdvertising();
            }



            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters, true);
        }
    }
}
