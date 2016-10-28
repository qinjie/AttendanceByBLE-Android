package com.example.sonata.attendancetakingapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sonata.attendancetakingapplication.Utils.BluetoothUtils;
import com.example.sonata.attendancetakingapplication.Utils.ConnectivityUtils;

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

            // get Shared Preferences of the app
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

            // get session token from the Shared Preferences
            String token = pref.getString("token", "");

            // if a token is available then try to login
            if (!token.equalsIgnoreCase("")) {
                startAuthenticatedArea();
            } else {
                startLogin();
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
