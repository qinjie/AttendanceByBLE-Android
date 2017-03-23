package com.example.sonata.attendancetakingapplication;

import android.Manifest;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sonata.attendancetakingapplication.Fragment.AttendanceHistoryFragment;
import com.example.sonata.attendancetakingapplication.Fragment.TimeTableFragment;
import com.example.sonata.attendancetakingapplication.Fragment.UserSettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;
import static com.example.sonata.attendancetakingapplication.Preferences.getActivity;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = NavigationActivity.class.getSimpleName();

    private Context context;

//    private static final String VENUE_NAME = "MonitoringVenueBeacon";
//    private static final String VENUE_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
//
//    private static final String LESSON_NAME = "RangingLessonBeacon";
//    private static final String LESSON_UUID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";
//
//    private static final String STUDENT_MAJOR = "1";
//    private static final String STUDENT_MINOR = "2";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    BluetoothAdapter bluetoothAdapter;


//    Intent mServiceIntent;

//    private BeaconConsumingService mSensorService;

//    private static final String REGION_NAME = "TestBeacon";
//    private static final String REGION_UUID = "2F234454-CF6D-4A0F-ADF2-LPF4911BA9FFA6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setLayoutContent();

        context = this;

        Preferences.setActivity(this);


        //TODO Request venue and lesson information from server
//        ServerApi client = ServiceGenerator.createService(ServerApi.class);
//        LoginInfo up = new LoginInfo(username, password, this);
//        Call<LoginResult> call = client.login(up);
//        call.enqueue(new ServerCallBack<LoginResult>() {
//            @Override
//            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                try {
//                    Preferences.dismissLoading();
//                    int messageCode = response.code();
//                    if (messageCode == 200) // SUCCESS
//                    {
//                        if (response.body().getDevice_hash().equals(Preferences.getMac(getBaseContext()))) {
//                            Preferences.setStudentInfo(response.body());
//                            startNavigation();
//                            onLoginSuccess();
//                        } else {
//                            requestRegisterNewDevice();
//                        }
//                    } else {
//                        onLoginFailed();
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResult> call, Throwable t) {
//                onLoginFailed();
//            }
//        });


        //TODO Save data to local
//        Preferences.saveDataToLocal(VENUE_NAME, VENUE_UUID,
//                LESSON_NAME, LESSON_UUID,
//                STUDENT_MAJOR, STUDENT_MINOR);

//        mSensorService = new BeaconConsumingService();
//        mServiceIntent = new Intent(context, mSensorService.getClass());
//        if (!isMyServiceRunning(mSensorService.getClass())) {
//            startService(mServiceIntent);
//            if (!isMyServiceRunning(mSensorService.getClass())) {
//                Preferences.notify(context, "Service", "Fail to start Sensor Service!");
//            }
//            else
//            {
//                Preferences.notify(context, "Service", "Sensor Service started successfully!");
//            }
//        }
//        else
//        {
//            Preferences.notify(context, "Service", "Sensor Service started successfully!");
//        }

        checkPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (this.getIntent() != null) {
//            String message = this.getIntent().getStringExtra("message");
//            if (message != null) {
////                TextView textView = (TextView) this.findViewById(R.id.textView);
////                textView.setText(message);
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        //-- Stop service so that it will restart
//        stopService(mServiceIntent);
        super.onDestroy();
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.i("isMyServiceRunning?", true + "");
//                return true;
//            }
//        }
//        Log.i("isMyServiceRunning?", false + "");
//        return false;
//    }

    private void setLayoutContent() {
        try {
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    android.app.Fragment fragment = null;
                    String title = "";
                    switch (tabId) {
                        case R.id.tab_schedule:
                            fragment = new TimeTableFragment();
                            title = "Timetable";
                            break;
                        case R.id.tab_history:
                            fragment = new AttendanceHistoryFragment();
                            title = "History";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_user:

                            //TODO
                            //for test
                            Intent intent = new Intent(getBaseContext(),TestActivity.class);
                            startActivity(intent);

                            //TODO
                            //bo cmt di, for test
//                            fragment = new UserSettingFragment();
//                            title = "User setting";
//                            getSupportActionBar().setSubtitle(null);
                            break;
                        default:
                            fragment = new TimeTableFragment();
                            title = "Timetable";
                            break;
                    }

                    //TODO
                    //bo cmt di, for test
//                    setActionBarTitle(title);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .commit();
                }
            });

            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(@IdRes int tabId) {
                    android.app.Fragment fragment = null;
                    String title = null;
                    switch (tabId) {
                        case R.id.tab_schedule:
                            title = "Timetable";
                            fragment = new TimeTableFragment();
                            break;
                        case R.id.tab_history:
                            title = "History";
                            fragment = new AttendanceHistoryFragment();
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_user:
                            title = "User setting";
                            fragment = new UserSettingFragment();
                            getSupportActionBar().setSubtitle(null);
                            break;
                        default:
                            title = "Timetable";
                            fragment = new TimeTableFragment();
                            break;
                    }

                    setActionBarTitle(title);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || !bluetoothAdapter.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs bluetooth service and location access");
                builder.setMessage("Please activate bluetooth service and grant location access so this app can detect start taking attendance.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        initBluetooth();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 9);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to take attendance.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


}
