package com.example.sonata.attendancetakingapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;
import com.example.sonata.attendancetakingapplication.Utils.ConnectivityUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;

public class SplashActivity extends AppCompatActivity {

//    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences.setActivity(this);
        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String islogin = pref.getString("isLogin", "");
        if (!islogin.equals("true")) {
            if (!ConnectivityUtils.isConnected(this)) {
                ConnectivityUtils.showConnectionFailureDialog(this);
                return;
            }
            obtainedAuCode();
        } else {
            startAuthenticatedArea();
        }




    }


    private void shortcutAdd(String name, int number) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        shortcutIntent.setAction(Constants.ACTION_PLAY);

        // Create bitmap with number in it -> very default. You probably want to give it a more stylish look
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(0xFF808080); // gray
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        new Canvas(bitmap).drawText(""+number, 50, 50, paint);
        ((ImageView) findViewById(R.id.icon)).setImageBitmap(bitmap);

        // Decorate the shortcut
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

        // Inform launcher to create shortcut
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    private void shortcutDel(String name) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        shortcutIntent.setAction(Constants.ACTION_PLAY);

        // Decorate the shortcut
        Intent delIntent = new Intent();
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // Inform launcher to remove shortcut
        delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(delIntent);
    }

    private void obtainedAuCode() {
        Preferences.showLoading(SplashActivity.this, "Initialize", "Checking authentication...");
        try {
            SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            String expand = new String("lesson,lesson_date,lecturers,venue");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        Preferences.dismissLoading();

                        int code = response.code();
                        if (code == 200) {
                            startAuthenticatedArea();
                        } else {
                            startLogin();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void requestTurnOnBluetooth()
//    {
//        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(intent, REQUEST_ENABLE_BT);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//            }
//            else
//            {
//                finish();
//            }
//        }
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
