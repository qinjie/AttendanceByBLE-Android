package com.example.sonata.attendancetakingapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.example.sonata.attendancetakingapplication.Utils.BluetoothUtils;
import com.example.sonata.attendancetakingapplication.Utils.ConnectivityUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Preferences.setActivity(this);

            if (!ConnectivityUtils.isConnected(this)) {
                ConnectivityUtils.showConnectionFailureDialog(this);
                return;
            }

            int bluetoothStatus = BluetoothUtils.isConnected();
            switch (bluetoothStatus)
            {
                case BluetoothUtils.BLUETOOTH_NOT_SUPPORT:
                    BluetoothUtils.showNotSupportedBluetoothDialog(this);
                    finish();
                    break;
                case BluetoothUtils.BLUETOOTH_NOT_ENABLE:
                    requestTurnOnBluetooth();
                    break;
                case BluetoothUtils.BLUETOOTH_ENABLE:
                    break;
            }

        obtainedAuCode();
    }

    private void obtainedAuCode()
    {
        Preferences.showLoading(SplashActivity.this, "Initialize", "Checking authentication...");
        try
        {
            SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            String expand = new String("lesson,lesson_date,lecturers,venue");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try{
                        Preferences.dismissLoading();

                        int code = response.code();
                        if (code == 200)
                        {
                            startAuthenticatedArea();
                        }
                        else
                        {
                            startLogin();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void requestTurnOnBluetooth()
    {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
            else
            {
                finish();
            }
        }
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
}
