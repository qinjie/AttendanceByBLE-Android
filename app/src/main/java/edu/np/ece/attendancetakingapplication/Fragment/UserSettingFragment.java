package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.np.ece.attendancetakingapplication.BeaconScanActivation;
import edu.np.ece.attendancetakingapplication.ChangePasswordActivity;
import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseManager;
import edu.np.ece.attendancetakingapplication.OrmLite.Subject;
import edu.np.ece.attendancetakingapplication.OrmLite.SubjectDateTime;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


public class UserSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Activity context;

    private View inflateView;

    @BindView(R.id.tvModule)
    TextView Module;

    @BindView(R.id.user_profile_name)
    TextView userName;

    @BindView(R.id.user_profile_short_bio)
    TextView userBio;

    @BindView(R.id.btnActivateBeacon)
    ToggleButton btnActivateBeacon;

    @BindView(R.id.tvClass)
    TextView Class;

    @BindView(R.id.tvTime)
    TextView Time;

    @BindView(R.id.tvVenue)
    TextView Venue;

    private Handler mHandler;
    public static BeaconTransmitter beaconTransmitter;
    public static Beacon.Builder beaconBuilder;


    public UserSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSettingFragment.
     */
    public static UserSettingFragment newInstance(String param1, String param2) {
        UserSettingFragment fragment = new UserSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = this.getActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserSettingLayout() {
        TextView signoutTv = (TextView) inflateView.findViewById(R.id.btn_signout);
        signoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.clearStudentInfo();
            }
        });

        TextView changePasswordTv = (TextView) inflateView.findViewById(R.id.btn_change_password);
        changePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_user_setting, container, false);

        ButterKnife.bind(this, inflateView);

        SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin", "false");
        String isStudent = pref.getString("isStudent", "true");

        if (isLogin.equals("true") && isStudent.equals("true")) {
            String studentName = pref.getString("student_name", "");
            userName.setText(studentName);

            userBio.setText("Still not take attendance yet for this class");



            if (BeaconScanActivation.timetableList != null) {

                for (final TimetableResult aSubject_time : BeaconScanActivation.timetableList) {
                    try {
                        //Get the data of current subject to display attendance status
                        String aTime = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getEnd_time();
                        String aID = aSubject_time.getLesson_id();
                        List<Subject> subjectList = DatabaseManager.getInstance().QueryBuilder("lesson_id",aID);
                        List<SubjectDateTime> subjectDateTimeList = subjectList.get(0).getSubject_Datetime();

                        String sTime = aSubject_time.getLesson_date().getLdate()+" "+aSubject_time.getLesson().getStart_time();



                        String aModuleSec = subjectList.get(0).getSubject_area();
                        String aModule = subjectList.get(0).getCatalog_number();
                        Module.setText(aModuleSec + " " + aModule);//显示Module


                        String aClass = aSubject_time.getLesson().getClass_section();
                        Class.setText(aClass);//显示Class


                        String cStartTime = aSubject_time.getLesson().getStart_time();
                        String cEndTime = aSubject_time.getLesson().getEnd_time();
                        Time.setText(cStartTime+ " - " + cEndTime);//显示时间

                        String cVenue = aSubject_time.getLesson().getFacility() ;
                        Venue.setText("#" + cVenue);//显示教室地点



                        Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(aTime);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time);

                        Date time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sTime);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(time2);

                        Date timeNow = new Date();
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(timeNow);
                        long diff =time2.getTime() - timeNow.getTime();
                        long min = diff/(1000*60);

                        String minn = String.valueOf(min);
                        int m = Integer.parseInt(minn);

                        //Log.e("test",minn);

//                        if(min > 5 ){
//                            userName.setText("Not yet time. \n Please try at 5 min before the class");
//                        }

                       if(calendar2.getTime().before(calendar1.getTime()) && m < 5){
//                        if(m < 7) {
//                          btnActivateBeacon.setVisibility(View.VISIBLE);
//
//                        }
//                else{        //if(m < 5){
                           Log.e("test",minn);
                            btnActivateBeacon.setVisibility(View.VISIBLE);
                            final String auCode = pref.getString("authorizationCode", null);
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse("{" +
                                    "\"lesson_date_id\":\"" + aSubject_time.getLesson_date().getId() + "\"" +
                                    "}").getAsJsonObject();

                            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                            Call<String> call = client.checkAttendanceStatus(obj);
                            call.enqueue(new ServerCallBack<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();

                                    if (result != null) {
                                        if (result.equals("0")) {
                                            userBio.setText("You're present for the class "+aSubject_time.getLesson().getSubject_area()+" "+aSubject_time.getLesson().getCatalog_number());
                                        }

                                        if (result.equals("-1")) {
                                            userBio.setText("You're absent for the class "+aSubject_time.getLesson().getSubject_area()+" "+aSubject_time.getLesson().getCatalog_number());
                                        }

                                        if (result.equals("Not yet")) {
                                            userBio.setText("Still not take attendance yet for the class "+aSubject_time.getLesson().getSubject_area()+" "+aSubject_time.getLesson().getCatalog_number());
                                        } else {
                                            userBio.setText("You're late for the class "+aSubject_time.getLesson().getSubject_area()+" "+aSubject_time.getLesson().getCatalog_number());

                                        }
                                    } else {
                                        if (response.code() == 401) {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle(R.string.another_login_title);
                                            builder.setMessage(R.string.another_login_content);
                                            builder.setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                                            Preferences.clearStudentInfo();
                                                            Intent intent = new Intent(context, LogInActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            builder.create().show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    super.onFailure(call, t);
                                }
                            });

                            break;

                        }
                        else{
                            btnActivateBeacon.setVisibility(View.INVISIBLE);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        setUserSettingLayout();

        mHandler = new Handler();


        String isBeaconActivated = pref.getString("isActivateBeacon", "");
        if (isBeaconActivated.equals("true")) {
            btnActivateBeacon.setChecked(true);
            btnActivateBeacon.setEnabled(false);

        } else {
            btnActivateBeacon.setChecked(false);
            btnActivateBeacon.setEnabled(true);

        }

        return inflateView;
    }


    @OnClick(R.id.btnActivateBeacon)
    public void turnOnOffBeacon() {
       // btnActivateBeacon.setBackground(R.drawable.bluetooth_checked);

        final SharedPreferences pref = context.getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

        String userMajor = pref.getString("major", "");
        String userMinor = pref.getString("minor", "");

        if (BeaconScanActivation.timetableList != null) {
            if (!userMajor.equals("") || !userMinor.equals("")) {
                for (TimetableResult aSubject_time : BeaconScanActivation.timetableList) {
                    try {
                        //Get the data of current subject for transmitting beacon signal
                        String aTime = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getEnd_time();
                        Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(aTime);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time);

                        Date timeNow = new Date();
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(timeNow);

                        if (calendar2.getTime().before(calendar1.getTime())) {

                            beaconBuilder = new Beacon.Builder();
                            beaconBuilder.setId1(aSubject_time.getLessonBeacon().getUuid());
                            beaconBuilder.setId2(userMajor);
                            beaconBuilder.setId3(userMinor);

                            //Estimote company code
                            //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
                            beaconBuilder.setManufacturer(0x015D);
                            beaconBuilder.setTxPower(-59);
                            beaconBuilder.setDataFields(Arrays.asList(new Long[]{0l}));
                            BeaconParser beaconParser = new BeaconParser()
                                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
                            beaconTransmitter = new BeaconTransmitter(getActivity().getBaseContext(), beaconParser);
                            break;

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        //random time in 15 min time interval
//        int min = 10000;
//        int max = 900000;
//        Random r = new Random();
//        long randomTime = r.nextInt(max - min) + min;

        if (btnActivateBeacon.isChecked() && beaconTransmitter != null && beaconBuilder != null) {

            final SharedPreferences.Editor editor = pref.edit();
            editor.putString("isActivateBeacon", "true");
            editor.apply();

            //transmit beacon signal in next 1 second
            Toast.makeText(context, "Please keep app open at least next 15 minutes to get attendance.", Toast.LENGTH_SHORT).show();
            btnActivateBeacon.setEnabled(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    beaconTransmitter.startAdvertising(beaconBuilder.build());
                }
            }, 1000);

            //turn off beacon after 10 seconds
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Transmitting beacon done. Please refresh this page to check new attendance status", Toast.LENGTH_SHORT).show();
                    beaconTransmitter.stopAdvertising();
                    btnActivateBeacon.setChecked(false);
                    btnActivateBeacon.setEnabled(true);
                    editor.putString("isActivateBeacon", "false");
                    editor.apply();
                }
            }, 10000);

        }


    }


}
