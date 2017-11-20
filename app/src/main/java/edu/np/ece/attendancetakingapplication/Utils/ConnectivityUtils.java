package edu.np.ece.attendancetakingapplication.Utils;

/**
 * Created by Sonata on 10/26/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import edu.np.ece.attendancetakingapplication.R;

public class ConnectivityUtils {

    private static NetworkInfo getNetworkInfo(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


    public static void showConnectionFailureDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.error_title_login_failed)
                .setMessage(R.string.error_message_login_failed)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .show();
    }
}
