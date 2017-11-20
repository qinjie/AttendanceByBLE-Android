package edu.np.ece.attendancetakingapplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by dellpc on 11/16/2017.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";

    private Context mContext;
    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().toString();
    private static CrashHandler mInstance = new CrashHandler();

    private CrashHandler(){

    }
    public static CrashHandler getInstance(){
        return mInstance;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveInfoToSD(mContext,ex);

        showToast(mContext,"Sorry, the application got error.");
        try{
            thread.sleep(2000);
        }catch (InterruptedException e ){
            e.printStackTrace();
        }
        ExitAppUtils.getInstance().exit();

    }

    public void setCrashHandler(Context context){
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void showToast(final Context context, final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context){
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);

        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" +  Build.PRODUCT);

        return map;
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        Log.e(TAG, mStringWriter.toString());
        return mStringWriter.toString();
    }

    private String saveInfoToSD(Context context, Throwable ex){
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(SDCARD_ROOT + File.separator + "crash" + File.separator);
            if(! dir.exists()){
                dir.mkdir();
            }

            try{
                fileName = dir.toString() + File.separator + paserTime(System.currentTimeMillis()) + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        return fileName;
    }

    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Singapore");
        TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date(milliseconds));
        return times;
    }


}
