package edu.np.ece.attendancetakingapplication;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.LoginResult;
import edu.np.ece.attendancetakingapplication.Model.StudentInfo;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;

/**
 * Created by Sonata on 10/26/2016.
 */

public class Preferences {

    // tags for Shared Preferences to store and retrieve some piece of data from local
    public static final String SharedPreferencesTag = "ATK_BLE_Preferences";
    public static final int SharedPreferences_ModeTag = Context.MODE_PRIVATE;

    public static final int LIST_ITEM_TYPE_1 = 0;
    public static final int LIST_ITEM_TYPE_2 = 1;
    public static final int LIST_ITEM_TYPE_COUNT = 2;

    private static final int CODE_INCORRECT_USERNAME = 10;
    private static final int CODE_INCORRECT_PASSWORD = 11;
    private static final int CODE_INCORRECT_DEVICE = 12;
    private static final int CODE_UNVERIFIED_EMAIL = 13;
    public static final int CODE_UNVERIFIED_DEVICE = 14;
    private static final int CODE_UNVERIFIED_EMAIL_DEVICE = 15;
    private static final int CODE_INVALID_ACCOUNT = 16;

    public static ProgressDialog loading;
    public static boolean isShownLoading = false;

    public static Activity activity;

    public static void showLoading(final Activity activity, final String title, final String message) {
        try {
            if (!isShownLoading) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading = ProgressDialog.show(activity, title, message, false, false);
                        isShownLoading = true;
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissLoading() {
        try {
            if (isShownLoading) {

                loading.dismiss();
                isShownLoading = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBadRequestNotificationDialog(final Activity activity, int badRequestCode, int title) {
        int message;

        switch (badRequestCode) {
            case CODE_INCORRECT_USERNAME:

                break;
            case CODE_INCORRECT_PASSWORD:

                break;
            case CODE_INCORRECT_DEVICE:

                break;
            case CODE_UNVERIFIED_EMAIL:

                break;
            case CODE_UNVERIFIED_DEVICE:

                break;
            case CODE_UNVERIFIED_EMAIL_DEVICE:

                break;
            case CODE_INVALID_ACCOUNT:

                break;
        }
    }

    public static void showNotificationDialog(final Activity activity, int title, int message) {
        new android.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }

    public static String getMac(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static void setStudentInfo(LoginResult _studentInfo) {
        SharedPreferences pref = activity.getSharedPreferences("ATK_BLE_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("isLogin", "true");
        editor.putString("isStudent", "true");
        editor.putString("student_id", _studentInfo.getId());
        editor.putString("student_name", _studentInfo.getName());
        editor.putString("student_acad", _studentInfo.getAcad());
        editor.putString("authorizationCode", "Bearer " + _studentInfo.getToken());
        editor.putString("major", _studentInfo.getMajor());
        editor.putString("minor", _studentInfo.getMinor());
        editor.putString("status", _studentInfo.getStatus());


        editor.apply();
    }

    public static void StudentInfo(StudentInfo _Info){
        SharedPreferences pref = activity.getSharedPreferences("ATK_BLE_Preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("student_email", _Info.getEmail());

        editor.apply();
    }

    public static void clearStudentInfo() {
        SharedPreferences pref = getActivity().getSharedPreferences("ATK_BLE_Preferences", Context.MODE_PRIVATE);
        String auCode = pref.getString("authorizationCode", null);

        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.apply();

        ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
        client.logout();

        Intent intent = new Intent(getActivity(), LogInActivity.class);
        activity.startActivity(intent);
    }

    public static void notify(Context context, String title, String content) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.checklist)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent intent = new Intent(context, LogInActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9696, builder.build());
    }

    public static void studentNotify(Context context, String title, String content, int id) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.checklist)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent intent = new Intent(context, LogInActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public static void studentNotifyWithLongText(Context context, String title, String content, int id) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.checklist)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent intent = new Intent(context, LogInActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public static void setActivity(Activity act) {
        activity = act;
    }

    public static Activity getActivity() {
        return activity;
    }


    public static boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }
}
