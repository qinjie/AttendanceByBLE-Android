package edu.np.ece.attendancetakingapplication;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.np.ece.attendancetakingapplication.JobScheduler.BeaconJobScheduler;
import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
import edu.np.ece.attendancetakingapplication.Model.StudentInfo;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseManager;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by hoanglong on 21-Dec-16.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {



    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconmanager;

    ArrayList<Region> regionList = new ArrayList();
    public static List<TimetableResult> timetableList;

     public String record_time;

    final BootstrapNotifier tmp = this;
    private Handler mHandler;

    FragmentManager manager;


    private List<AttendanceResult> record;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());




        mBeaconmanager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        mBeaconmanager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconmanager.setBackgroundMode(true);
        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());

        mBeaconmanager.setBackgroundBetweenScanPeriod(30000l);
        mBeaconmanager.setBackgroundScanPeriod(5000l);

        DatabaseManager.init(getBaseContext());

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void didDetermineStateForRegion(int status, Region region) {
        final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin", "false");

        if (isLogin.equals("true") && regionList != null) {
            //in the beacon region
            if (status == 1) {
                try {
                    final String auCode = pref.getString("authorizationCode", null);
                    final String studentId = pref.getString("student_id", null);

                    String[] studentId_lessonDateId = region.getUniqueId().split(";");
                    if (studentId_lessonDateId.length > 0) {
                        //Get data of detected student
                        String detectedStudentId = studentId_lessonDateId[0];
                        String lessonDateId = studentId_lessonDateId[1];

                        if (timetableList != null) {
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse("{" +
                                    "\"data\": " +
                                    "[" +
                                    "{" +
                                    "\"lesson_date_id\":\"" + lessonDateId + "\"," +
                                    "\"student_id_1\":\"" + studentId + "\"," +
                                    "\"student_id_2\":\"" + detectedStudentId + "\"" +
                                    "}" +
                                    "]" +
                                    "}").getAsJsonObject();


                            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                            Call<String> call = client.takeAttendance(obj);
                            call.enqueue(new ServerCallBack<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("Attendance taking successfully")) {

                                        Toast.makeText(getBaseContext(), "Taking attendance success", Toast.LENGTH_SHORT).show();
/*                                        String r = getRecordTime();
                                        SharedPreferences sendRecord = getSharedPreferences("Record",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sendRecord.edit();
                                        editor.putString("Record_Time",r);
                                        editor.apply();*/
                                        Preferences.studentNotify(getBaseContext(), "Taking attendance success", "Your attendance has been recorded. Enjoy your class.", Integer.parseInt(studentId));
                                        Log.d("test attendance", "success");
                                    }

                                    if (response.body().contains("Late")) {
                                        Toast.makeText(getBaseContext(), "Late attendance", Toast.LENGTH_SHORT).show();
                                        Preferences.studentNotify(getBaseContext(), "Late attendance", "You're late for the class.", Integer.parseInt(studentId));
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    super.onFailure(call, t);
                                    Log.d("test attendance", "failed");
                                }
                            });


                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

/*    private String getRecordTime(){

        SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        final String auCode = pref.getString("authorizationCode", null);
        ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
        Call<List<AttendanceResult>> call = client.getAttendanceReports();
        call.enqueue(new ServerCallBack<List<AttendanceResult>>() {
            @Override
            public void onResponse(Call<List<AttendanceResult>> call, Response<List<AttendanceResult>> response) {
                try {
                    record = response.body();
                    if (record == null) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.another_login_title);
                        builder.setMessage(R.string.another_login_content);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int i) {
                                Preferences.clearStudentInfo();
                                Intent intent = new Intent(getActivity(), LogInActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.create().show();
                    }
                    else{
                        for (int i = 0; i<record.size();i++){
                            String lessonId = record.get(i).getLesson_date().getLesson_id();
                            if(timetableList.get(i).getLesson_id().equals(lessonId)){
                                 record_time = record.get(i).getRecorded_time();

                                break;
                            }
                            else{

                            }

                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        return record_time;
    }*/


    //this will re-run after every 12 hours
    private int mInterval = 43200000;
//    private int mInterval = 10000;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin", "false");

            if (isLogin.equals("true")) {
                //if user already login, this part only 2 times/day
                mInterval = 43200000;
//                mInterval = 10000;

                //timetableList may be null because slow connection
                //so we need to delay this part 10 seconds to make sure data not null
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timetableList != null) {
                            ComponentName serviceName = new ComponentName(getBaseContext(), BeaconJobScheduler.class);

                            String userMajor = pref.getString("major", "");
                            String userMinor = pref.getString("minor", "");

                            if (!userMajor.equals("") || !userMinor.equals("")) {

                                JobScheduler jobScheduler =
                                        (JobScheduler) getBaseContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                                jobScheduler.cancelAll();

                                for (TimetableResult aSubject_time : timetableList) {
                                    try {
                                        String timeEnd = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getEnd_time();
                                        Date timeEnd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeEnd);
                                        Calendar calendar1 = Calendar.getInstance();
                                        calendar1.setTime(timeEnd2);

                                        Date timeNow = new Date();
                                        Calendar calendar2 = Calendar.getInstance();
                                        calendar2.setTime(timeNow);

                                        //if the time end of lesson before time now beacon of that lesson
                                        //will be added to list to be monitored
                                        if (calendar2.getTime().before(calendar1.getTime())) {
                                            String timeStart = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getStart_time();
                                            Date timeStart2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeStart);
                                            long timeInterval = timeStart2.getTime() - timeNow.getTime();

                                            //In case student install app after the subject begin
                                            if (timeInterval < 0) {
                                                timeInterval = 60000;
                                            }

                                            //put data into the jobScheduler
                                            PersistableBundle bundle1 = new PersistableBundle();
                                            bundle1.putString("subject-uuid", aSubject_time.getLessonBeacon().getUuid());
                                            bundle1.putString("user-major", userMajor);
                                            bundle1.putString("user-minor", userMinor);
                                            bundle1.putString("user-lesson", aSubject_time.getLesson().getFacility() + " " + aSubject_time.getLesson().getCatalog_number() + " "
                                                    + aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getStart_time());

                                            //random time in 15 min time interval
                                            int min = 60000;
                                            int max = 900000;
                                            Random r = new Random();
                                            long randomTime = r.nextInt(max - min) + min;
                                            timeInterval = timeInterval + randomTime;

                                            //schedule for first time broadcast - first 15 min of lesson
                                            JobInfo.Builder builder = new JobInfo.Builder(Integer.parseInt(aSubject_time.getLesson_date().getId()), serviceName);
                                            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                                            builder.setMinimumLatency(timeInterval);
                                            builder.setExtras(bundle1);
                                            jobScheduler.schedule(builder.build());

                                            //Create random time between the end time of first broadcast to the end time of subject
                                            Random r2 = new Random();
                                            long max2 = (timeEnd2.getTime() - timeNow.getTime()) - (timeStart2.getTime() - timeNow.getTime());
                                            long randomTime2 = r2.nextInt((int) max2 - min) + min;
                                            timeInterval = timeInterval + randomTime2;

                                            //schedule for second time broadcast
                                            JobInfo.Builder builder2 = new JobInfo.Builder(Integer.parseInt(aSubject_time.getLesson_date().getId()) + 666, serviceName);
                                            builder2.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                                            builder2.setMinimumLatency(timeInterval);
                                            builder2.setExtras(bundle1);
                                            jobScheduler.schedule(builder2.build());

                                            //adding teacher beacon region to be monitored
                                            if (aSubject_time.getLecturers() != null) {
                                                if (aSubject_time.getLecturers().getBeacon() != null) {
                                                    String teacherMajor = aSubject_time.getLecturers().getBeacon().getMajor();
                                                    String teacherMinor = aSubject_time.getLecturers().getBeacon().getMinor();
                                                    Region region2 = new Region(aSubject_time.getLecturers().getId() + ";" +
                                                            aSubject_time.getLesson_date().getId() + ";teacher",
                                                            Identifier.parse(aSubject_time.getLessonBeacon().getUuid()),
                                                            Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));
                                                    if (!regionList.contains(region2)) {
                                                        regionList.add(region2);
                                                    }
                                                }
                                            }

                                            //adding students beacon region to be monitored
                                            for (StudentInfo aStudent : aSubject_time.getStudentList()) {
                                                if (!aStudent.getBeacon().getMajor().equals("")) {
                                                    String studentMajor = aStudent.getBeacon().getMajor();
                                                    String studentMinor = aStudent.getBeacon().getMinor();
                                                    Region region = new Region(aStudent.getId() + ";" +
                                                            aSubject_time.getLesson_date().getId() + ";student",
                                                            Identifier.parse(aSubject_time.getLessonBeacon().getUuid()),
                                                            Identifier.parse(studentMajor), Identifier.parse(studentMinor));
                                                    if (!regionList.contains(region)) {
                                                        regionList.add(region);
                                                    }
                                                }
                                            }

                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }

                        //init jobScheduler
                        Intent startServiceIntent = new Intent(getBaseContext(), BeaconJobScheduler.class);
                        startService(startServiceIntent);

                        //begin monitoring with list of added beacon
                        regionBootstrap = new RegionBootstrap(tmp, regionList);
                    }
                }, 10000);

            } else {
                //if user logout, begin stopping monitoring all beacon to release memory
                if (regionList != null && regionList.size() > 0) {
                    for (Region tmp : regionList) {
                        try {
                            mBeaconmanager.stopMonitoringBeaconsInRegion(tmp);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                regionList.clear();

                mInterval = 30000;

            }

            //rerun this method
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };


    public void startRepeatingTask() {
        mStatusChecker.run();
    }

    //Check the internet activity on or not
    //Return true if ON. False if OFF
    public boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }

}