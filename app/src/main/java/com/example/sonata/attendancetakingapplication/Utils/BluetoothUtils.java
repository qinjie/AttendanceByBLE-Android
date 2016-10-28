package com.example.sonata.attendancetakingapplication.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;

import com.example.sonata.attendancetakingapplication.R;

/**
 * Created by Sonata on 10/28/2016.
 */

public class BluetoothUtils {

    public static final int BLUETOOTH_NOT_SUPPORT = 0;
    public static final int BLUETOOTH_NOT_ENABLE  = 1;
    public static final int BLUETOOTH_ENABLE      = 2;

    public static int isConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return BLUETOOTH_NOT_SUPPORT;
        } else {
            if (!mBluetoothAdapter.isEnabled()) { // Bluetooth is not enable
                return BLUETOOTH_NOT_ENABLE;
            }
            else
            {
                return BLUETOOTH_ENABLE;
            }
        }
    }

    public static void showNotSupportedBluetoothDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.error_title_bluetooth_not_support)
                .setMessage(R.string.error_message_bluetooth_not_support)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }

}
