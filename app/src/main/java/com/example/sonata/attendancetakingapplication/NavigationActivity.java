package com.example.sonata.attendancetakingapplication;

import android.Manifest;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.sonata.attendancetakingapplication.Fragment.AttendanceHistoryFragment;
import com.example.sonata.attendancetakingapplication.Fragment.TimeTableFragment;
import com.example.sonata.attendancetakingapplication.Fragment.UserSettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import static com.example.sonata.attendancetakingapplication.Preferences.getActivity;

public class NavigationActivity extends AppCompatActivity{


    private Context context;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    BluetoothAdapter bluetoothAdapter;

//    Alarm alarm = new Alarm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setLayoutContent();

        context = this;

        Preferences.setActivity(this);

        checkPermissions();


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }


    @Override
    protected void onStart() {
//        alarm.setAlarm(this);
//        Toast.makeText(this, "create alarm",Toast.LENGTH_SHORT).show();
        super.onStart();
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
//                            Intent intent = new Intent(getBaseContext(),TestActivity.class);
//                            startActivity(intent);

                            //TODO
                            //bo cmt di, for test
                            fragment = new UserSettingFragment();
                            title = "User setting";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        default:
                            fragment = new TimeTableFragment();
                            title = "Timetable";
                            break;
                    }

                    //TODO
                    //bo cmt di, for test
                    setActionBarTitle(title);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
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

    public void returnToDefaultTab(){
//        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
//
//        bottomBar.selectTabAtPosition(0);

        String title = "Timetable";
        android.app.Fragment fragment = new TimeTableFragment();
//        setActionBarTitle(title);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
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
//                    Log.d(TAG, "coarse location permission granted");
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
