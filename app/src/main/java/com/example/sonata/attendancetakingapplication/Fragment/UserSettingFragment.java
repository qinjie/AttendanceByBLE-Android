package com.example.sonata.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sonata.attendancetakingapplication.ChangePasswordActivity;
import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sonata.attendancetakingapplication.BeaconScanActivation.timetableList;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferencesTag;
import static com.example.sonata.attendancetakingapplication.Preferences.SharedPreferences_ModeTag;


public class UserSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Activity context;

    private View inflateView;

    @BindView(R.id.user_profile_name)
    TextView userName;

    @BindView(R.id.user_profile_short_bio)
    TextView userBio;

    @BindView(R.id.btnActivateBeacon)
    ToggleButton btnActivateBeacon;

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
    // TODO: Rename and change types and number of parameters
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
//            if (getArguments() != null) {
//                mParam1 = getArguments().getString(ARG_PARAM1);
//                mParam2 = getArguments().getString(ARG_PARAM2);
//            }
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

        SharedPreferences pref = getActivity().getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin", "false");
        String isStudent = pref.getString("isStudent", "true");

        if (isLogin.equals("true") && isStudent.equals("true")) {
            String studentName = pref.getString("student_name", "");
            userName.setText(studentName);

            String studentID = pref.getString("student_id", "");
            userBio.setText(studentID);
        }

        setUserSettingLayout();


        mHandler = new Handler();

//        String userMajor = pref.getString("major", "");
//        String userMinor = pref.getString("minor", "");
//
//        if (timetableList != null) {
//            if (!userMajor.equals("") || !userMinor.equals("")) {
//                for (TimetableResult aSubject_time : timetableList) {
//                    try {
//                        String aTime = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getEnd_time();
//                        Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(aTime);
//                        Calendar calendar1 = Calendar.getInstance();
//                        calendar1.setTime(time);
//
//                        Date timeNow = new Date();
//                        Calendar calendar2 = Calendar.getInstance();
//                        calendar2.setTime(timeNow);
//
//                        if (calendar2.getTime().before(calendar1.getTime())) {
//
//                            beaconBuilder = new Beacon.Builder();
//                            beaconBuilder.setId1(aSubject_time.getLessonBeacon().getUuid());
//                            beaconBuilder.setId2(userMajor);
//                            beaconBuilder.setId3(userMinor);
//
//                            //Estimote company code
//                            //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
//                            beaconBuilder.setManufacturer(0x015D);
//                            beaconBuilder.setTxPower(-59);
//                            beaconBuilder.setDataFields(Arrays.asList(new Long[]{0l}));
//                            BeaconParser beaconParser = new BeaconParser()
//                                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
//                            beaconTransmitter = new BeaconTransmitter(getActivity().getBaseContext(), beaconParser);
//                            break;
//
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

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


//        String userMajor = pref.getString("major", "");
//        String userMinor = pref.getString("minor", "");
//
//
//        if (timetableList != null) {
//
//            if (!userMajor.equals("") || !userMinor.equals("")) {
//
//                for (TimetableResult aSubject_time : timetableList) {
//
//                    try {
//                        String aTime = aSubject_time.getLesson_date().getLdate() + " " + aSubject_time.getLesson().getEnd_time();
//                        Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(aTime);
//                        Calendar calendar1 = Calendar.getInstance();
//                        calendar1.setTime(time);
//
//                        Date timeNow = new Date();
//                        Calendar calendar2 = Calendar.getInstance();
//                        calendar2.setTime(timeNow);
//
//                        if (calendar2.getTime().before(calendar1.getTime())) {
//
//                            //random time in 15 min time interval
//                            int min = 10000;
//                            int max = 900000;
//                            Random r = new Random();
//                            long randomTime = r.nextInt(max - min) + min;
//
//
//                            final Beacon.Builder beaconBuilder = new Beacon.Builder();
//                            beaconBuilder.setId1(aSubject_time.getLessonBeacon().getUuid());
//                            beaconBuilder.setId2(userMajor);
//                            beaconBuilder.setId3(userMinor);
//
//                            //Estimote company code
//                            //read more: https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
//                            beaconBuilder.setManufacturer(0x015D);
//                            beaconBuilder.setTxPower(-59);
//                            beaconBuilder.setDataFields(Arrays.asList(new Long[]{0l}));
//                            BeaconParser beaconParser = new BeaconParser()
//                                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
//                            final BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getActivity().getBaseContext(), beaconParser);
//
//
//
////                            else{
////                                Toast.makeText(context, "Attendance progress has been cancelled.", Toast.LENGTH_SHORT).show();
////                                beaconTransmitter.stopAdvertising();
////                                btnActivateBeacon.setChecked(false);
////                            }
//
//                            break;
//
//                        }
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }


        final SharedPreferences pref = context.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);

        String userMajor = pref.getString("major", "");
        String userMinor = pref.getString("minor", "");

        if (timetableList != null) {
            if (!userMajor.equals("") || !userMinor.equals("")) {
                for (TimetableResult aSubject_time : timetableList) {
                    try {
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

        //random time in 15 min time interval
        int min = 10000;
        int max = 900000;
        Random r = new Random();
        long randomTime = r.nextInt(max - min) + min;

        if (btnActivateBeacon.isChecked() && beaconTransmitter != null && beaconBuilder != null) {

            final SharedPreferences.Editor editor = pref.edit();
            editor.putString("isActivateBeacon", "true");
            editor.apply();


            Toast.makeText(context, "Please stay in this page at least next 15 minutes to get attendance.", Toast.LENGTH_SHORT).show();
            btnActivateBeacon.setEnabled(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    beaconTransmitter.startAdvertising(beaconBuilder.build());
                    Toast.makeText(context, "Attvvv.", Toast.LENGTH_SHORT).show();
                }
            }, 10000);


            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Attendance has been recorded.", Toast.LENGTH_SHORT).show();
                    beaconTransmitter.stopAdvertising();
                    btnActivateBeacon.setChecked(false);
                    btnActivateBeacon.setEnabled(true);
                    editor.putString("isActivateBeacon", "false");
                    editor.apply();
                }
            }, 30000);

        }


    }


}
