package edu.np.ece.attendancetakingapplication;

import android.app.Application;

/**
 * Created by dellpc on 11/16/2017.
 */

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.setCrashHandler(getApplicationContext());
    }
}
