package com.example.sonata.attendancetakingapplication.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sonata.attendancetakingapplication.Service.BeaconConsumingService;

public class ToastMakerReceiver extends BroadcastReceiver {
    public ToastMakerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(BeaconConsumingService.class.getSimpleName(), "ToastMakerReceiver activated...");
        Toast.makeText(context, intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
    }
}
