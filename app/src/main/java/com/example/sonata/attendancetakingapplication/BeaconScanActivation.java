package com.example.sonata.attendancetakingapplication;

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

import com.crashlytics.android.Crashlytics;
import com.example.sonata.attendancetakingapplication.JobScheduler.BeaconJobScheduler;
import com.example.sonata.attendancetakingapplication.Model.StudentInfo;
import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.OrmLite.DatabaseManager;

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

import io.fabric.sdk.android.Fabric;

import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;


/**
 * Created by hoanglong on 21-Dec-16.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {

    private static final int LOGIN_ANOTHER_DEVICE = 401;

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconmanager;

    ArrayList<Region> regionList = new ArrayList();
    //    TimetableResult specificTimetable = null;
    public static List<TimetableResult> timetableList;


    final BootstrapNotifier tmp = this;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mBeaconmanager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        mBeaconmanager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconmanager.setBackgroundMode(true);
        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());

        mBeaconmanager.setBackgroundBetweenScanPeriod(25000l);
        mBeaconmanager.setBackgroundScanPeriod(20000l);

        DatabaseManager.init(getBaseContext());

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void didDetermineStateForRegion(int status, Region region) {
        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String studentId = pref.getString("student_id", null);

        String isLogin = pref.getString("isLogin", "false");
        if (isLogin.equals("true")) {
//            if (specificTimetable != null) {
//                if (specificTimetable.getLecturers() != null) {
////                Log.i("Activation-determine", region.getUniqueId() + ": " + status);
//                    if (specificTimetable.getLecturers().getBeacon() != null) {
//
//                        String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
//                        String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
//
//                        Region region2 = new Region("Teacher", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));
//
//                        if (region.equals(region2) && status == 1) {
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString("hasTeacher", "true");
//                            editor.apply();
//                        }
//
//                        String hasTeacher = pref.getString("hasTeacher", "");
//                        if (hasTeacher.equals("true") && status == 1 && (!region.equals(region2))) {
//                            Log.i("Presenttttttt", region.getUniqueId() + " : " + status);
//                            Preferences.studentNotify(getBaseContext(), "Attendance", "Student Present", Integer.parseInt(studentId));
//                        }
//                    }
////
//                }
//            }
        }
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

//        if (specificTimetable != null) {
//            if (specificTimetable.getLecturers() != null) {
//                if (specificTimetable.getLecturers().getBeacon() != null) {
//                    String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
//                    String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
//                    Region region2 = new Region("Teacher", Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));
//
//                    if (region.equals(region2)) {
//                        SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("hasTeacher", "false");
//                        editor.apply();
//                        Log.i("Outgoing", region.getUniqueId());
//                    }
//                }
//            }
//
//        }

    }


    //this will re-run after every 10 seconds
//    private int mInterval = 43200000;
    private int mInterval = 30000;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin", "false");

            if (isLogin.equals("true")) {

                String auCode = pref.getString("authorizationCode", null);
                final String studentId = pref.getString("student_id", null);

//                Date aDate = new Date();
//                SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateObj = curFormatter.format(aDate);
//
//                JsonParser parser = new JsonParser();
//                JsonObject obj = parser.parse("{\"datetime\": \"" + dateObj + "\"}").getAsJsonObject();
//
//                ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
//                Call<TimetableResult> call = client.getLessonAtSpecificTime(obj);
//                call.enqueue(new Callback<TimetableResult>() {
//                    @Override
//                    public void onResponse(Call<TimetableResult> call, Response<TimetableResult> response) {
//                        try {
//                            if (response.body() != null) {
//                                specificTimetable = response.body();
//                            } else {
//                                if (response.code() == LOGIN_ANOTHER_DEVICE) {
//                                    SharedPreferences pref = getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.putString("isLogin", "false");
//                                    editor.apply();
//
//                                    Preferences.studentNotifyWithLongText(getBaseContext(), "Detected another login", "Your account has been sign in from another device. You will automatically sign out. Click here to sign in again.", Integer.parseInt(studentId));
//                                }
//
//                                specificTimetable = null;
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<TimetableResult> call, Throwable t) {
//
//                        final List<Subject> subjectList = DatabaseManager.getInstance().getAllSubjects();
//
//                        for (Subject tmp : subjectList) {
//                            for (SubjectDateTime tmp2 : tmp.getSubject_Datetime()) {
//                                try {
//                                    String startTime = tmp2.getLesson_date() + " " + tmp2.getStartTime();
//                                    Date time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
//                                    Calendar calendar1 = Calendar.getInstance();
//                                    calendar1.setTime(time1);
//
//                                    String endTime = tmp2.getLesson_date() + " " + tmp2.getEndTime();
//                                    Date time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
//                                    Calendar calendar2 = Calendar.getInstance();
//                                    calendar2.setTime(time2);
//
//                                    Date nowTime = new Date();
//                                    Calendar calendar3 = Calendar.getInstance();
//                                    calendar3.setTime(nowTime);
//
//
//                                    Date x = calendar3.getTime();
//                                    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
//                                        specificTimetable = new TimetableResult();
//
//                                        Log.d("test date", " true");
//                                        specificTimetable.setLesson_id(tmp.getLesson_id());
//
//                                        Lesson aLesson = new Lesson();
//                                        aLesson.setSubject_area(tmp.getSubject_area());
//                                        aLesson.setCatalog_number(tmp.getCatalog_number());
//                                        aLesson.setStart_time(tmp2.getStartTime());
//                                        aLesson.setEnd_time(tmp2.getEndTime());
//                                        specificTimetable.setLesson(aLesson);
//
//                                        LessonDate aLessonDate = new LessonDate();
//                                        aLessonDate.setLesson_id(tmp.getLesson_id());
//                                        aLessonDate.setLdate(tmp2.getLesson_date());
//                                        specificTimetable.setLesson_date(aLessonDate);
//
//                                        LessonBeacon aLessonBeacon = new LessonBeacon();
//                                        aLessonBeacon.setUuid(tmp.getUuid());
//                                        specificTimetable.setLessonBeacon(aLessonBeacon);
//
//                                        Lecturer aLecturer = new Lecturer();
//                                        aLecturer.setId(tmp.getTeacher_id());
//                                        aLecturer.setName(tmp.getTeacher_name());
//                                        aLecturer.setAcad(tmp.getTeacher_acad());
//                                        aLecturer.setEmail(tmp.getTeacher_email());
//                                        UserBeacon aLecturerBeacon = new UserBeacon();
//                                        aLecturerBeacon.setMajor(tmp.getTeacher_major());
//                                        aLecturerBeacon.setMinor(tmp.getTeacher_minor());
//                                        aLecturer.setBeacon(aLecturerBeacon);
//                                        specificTimetable.setLecturers(aLecturer);
//
//                                        Venue aVenue = new Venue();
//                                        aVenue.setAddress(tmp.getLocation());
//                                        specificTimetable.setVenue(aVenue);
//
//
//                                        List<StudentInfo> studentList = new ArrayList<StudentInfo>();
//                                        for (Student tmp3 : tmp.getStudent_list()) {
//                                            StudentInfo aStudent = new StudentInfo();
//                                            aStudent.setName(tmp3.getName());
//                                            aStudent.setCard(tmp3.getCard());
//                                            aStudent.setId(tmp3.getStudent_id());
//                                            UserBeacon aStudentBeacon = new UserBeacon();
//                                            aStudentBeacon.setMajor(tmp3.getBeacon_major());
//                                            aStudentBeacon.setMinor(tmp3.getBeacon_minor());
//                                            aStudent.setBeacon(aStudentBeacon);
//
//                                            if (!studentList.contains(aStudent)) {
//                                                studentList.add(aStudent);
//                                            }
//                                        }
//
//
//                                        specificTimetable.setStudentList(studentList);
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//
//                        }
//
//                    }
//                });


//                if (specificTimetable != null) {
//                    if (specificTimetable.getLecturers() != null) {
//                        if (specificTimetable.getLecturers().getBeacon() != null) {
//                            String teacherMajor = specificTimetable.getLecturers().getBeacon().getMajor();
//                            String teacherMinor = specificTimetable.getLecturers().getBeacon().getMinor();
//                            Region region2 = new Region("Teacher - " + specificTimetable.getLesson().getSubject_area(), Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));
//
//
//                            if (!regionList.contains(region2)) {
//                                regionList.add(region2);
//                            }
//                        }
//                    }
//
//
//                    for (StudentInfo aStudent : specificTimetable.getStudentList()) {
//                        if (!aStudent.getBeacon().getMajor().equals("")) {
//                            String studentMajor = aStudent.getBeacon().getMajor();
//                            String studentMinor = aStudent.getBeacon().getMinor();
//                            Region region = new Region(aStudent.getName() + " - " + specificTimetable.getLesson().getSubject_area(), Identifier.parse(specificTimetable.getLessonBeacon().getUuid()), Identifier.parse(studentMajor), Identifier.parse(studentMinor));
//                            if (!regionList.contains(region)) {
//                                regionList.add(region);
//                            }
//                        }
//                    }
//
//                }


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
                                String aTime = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getStart_time();

                                Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(aTime);

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.setTime(time);

                                Date timeNow = new Date();
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(timeNow);

                                if (calendar2.getTime().before(calendar1.getTime())) {
                                    long timeInterval = time.getTime() - timeNow.getTime();

                                    PersistableBundle bundle1 = new PersistableBundle();
                                    bundle1.putString("subject-uuid", aSubject_time.getLessonBeacon().getUuid());
                                    bundle1.putString("user-major", userMajor);
                                    bundle1.putString("user-minor", userMinor);

                                    JobInfo.Builder builder = new JobInfo.Builder(1, serviceName);
                                    builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                                    builder.setOverrideDeadline(2000);
                                    builder.setMinimumLatency(timeInterval);
                                    builder.setExtras(bundle1);

                                    jobScheduler.schedule(builder.build());


                                    if (aSubject_time.getLecturers() != null) {
                                        if (aSubject_time.getLecturers().getBeacon() != null) {
                                            String teacherMajor = aSubject_time.getLecturers().getBeacon().getMajor();
                                            String teacherMinor = aSubject_time.getLecturers().getBeacon().getMinor();
                                            Region region2 = new Region(aSubject_time.getLecturers().getName() + ";" +
                                                    aSubject_time.getLesson().getSubject_area() + ";" +
                                                    aSubject_time.getLesson_date().getLdate(),
                                                    Identifier.parse(aSubject_time.getLessonBeacon().getUuid()),
                                                    Identifier.parse(teacherMajor), Identifier.parse(teacherMinor));
                                            if (!regionList.contains(region2)) {
                                                regionList.add(region2);
                                            }
                                        }
                                    }
                                    
                                    for (StudentInfo aStudent : aSubject_time.getStudentList()) {
                                        if (!aStudent.getBeacon().getMajor().equals("")) {
                                            String studentMajor = aStudent.getBeacon().getMajor();
                                            String studentMinor = aStudent.getBeacon().getMinor();
                                            Region region = new Region(aStudent.getName() + " - " +
                                                    aSubject_time.getLesson().getSubject_area(),
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

                Intent startServiceIntent = new Intent(getBaseContext(), BeaconJobScheduler.class);
                startService(startServiceIntent);

                regionBootstrap = new RegionBootstrap(tmp, regionList);


            } else {
                if (regionList != null && regionList.size() > 0) {
                    for (Region tmp : regionList) {
                        try {
                            mBeaconmanager.stopMonitoringBeaconsInRegion(tmp);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };


    void startRepeatingTask() {
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


    public void setTimetableList(List<TimetableResult> list) {
        timetableList = list;
    }
}