package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;

import edu.np.ece.attendancetakingapplication.NavigationActivity;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


public class LessonDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity context;
    private Calendar calendar;

    private List<HistoricalResult> historicalList;

    private View myView;
    private int totalSlot;
    private int absentedSlot;
    private int presentedSlot;
    private int lateSlot;
    private int now;
    private int attendedPercent;





    public LessonDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LessonDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonDetailsFragment newInstance(String param1, String param2) {
        LessonDetailsFragment fragment = new LessonDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
      //  context = getActivity();
    }


    private void loadInfo(){
        Preferences.showLoading(getActivity(), "Details", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            Call<List<HistoricalResult>> call = client.getHistoricalReports();
            call.enqueue(new ServerCallBack<List<HistoricalResult>>() {
                @Override
                public void onResponse(Call<List<HistoricalResult>> call, Response<List<HistoricalResult>> response) {
                    try {

                        historicalList = response.body();

                        if (historicalList == null) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.another_login_title);
                            builder.setMessage(R.string.another_login_content);
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearStudentInfo();
                                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            builder.create().show();
                        } else {
                            //initHistorytableList();
                            SharedPreferences getName=getActivity().getSharedPreferences("valueOfTB",Context.MODE_PRIVATE);
                            for(int i=0;i<historicalList.size();i++){
                                String lessonCat=getName.getString("Catalog","error");
                                String dataCat =historicalList.get(i).getLesson_name();
                                if(lessonCat.equals(dataCat)){
                                     totalSlot = Integer.valueOf(historicalList.get(i).getTotal());

                                     absentedSlot = Integer.valueOf(historicalList.get(i).getAbsented());
                                     presentedSlot = Integer.valueOf(historicalList.get(i).getPresented());

                                     lateSlot = Integer.valueOf(historicalList.get(i).getLate());

                                     now=absentedSlot+presentedSlot+lateSlot;
                                     attendedPercent = (int) ((float) 100 * presentedSlot / totalSlot);
                                    TextView tvTotal=(TextView)myView.findViewById(R.id.Total);
                                    TextView tvPresent=(TextView)myView.findViewById(R.id.Present);
                                    TextView tvLate=(TextView)myView.findViewById(R.id.Late);
                                    TextView tvAbsent=(TextView)myView.findViewById(R.id.Absent);
                                    tvTotal.setText(String.valueOf(now)+" / "+String.valueOf(totalSlot));
                                    tvAbsent.setText(String.valueOf(absentedSlot));
                                    tvPresent.setText(String.valueOf(presentedSlot)+" ( "+attendedPercent+"% )");
                                    tvLate.setText(String.valueOf(lateSlot));

                                    break;


                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();

                }

                @Override
                public void onFailure(Call<List<HistoricalResult>> call, Throwable t) {
                    super.onFailure(call, t);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("This function needs internet connection");
                    builder.setMessage("Please turn on internet to get latest update about you attendance history.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, final int i) {
                                    Intent intent = new Intent(getActivity().getBaseContext(), NavigationActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                    builder.create().show();

                    Preferences.dismissLoading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_lesson_details, container, false);

        TextView subjectCatalog_name = (TextView)myView.findViewById(R.id.subjectCatalog_name);
        TextView lesson_name =(TextView)myView.findViewById(R.id.lesson_name);
        TextView lesson_credit=(TextView)myView.findViewById(R.id.lesson_credit);
        TextView student_group = (TextView)myView.findViewById(R.id.student_group);
        TextView lesson_time=(TextView)myView.findViewById(R.id.lesson_time);
        TextView lesson_venue=(TextView)myView.findViewById(R.id.lesson_venue);
        TextView teacher_name=(TextView)myView.findViewById(R.id.teacher_name);
        TextView teacher_phone=(TextView)myView.findViewById(R.id.teacher_phone);
        TextView teacher_mail=(TextView)myView.findViewById(R.id.teacher_mail);
        TextView teacher_venue=(TextView)myView.findViewById(R.id.teacher_venue);



        Bundle arguments = getArguments();
        if(arguments!=null){
            subjectCatalog_name.setText(arguments.getString("Area")+" "+arguments.getString("Catalog","error"));
            student_group.setText(arguments.getString("Group"));
            lesson_time.setText(arguments.getString("Timestart")+" - "+arguments.getString("Timeend"));
            lesson_venue.setText(arguments.getString("Venue"));
            teacher_name.setText(arguments.getString("Teacher_name"));
            teacher_phone.setText(arguments.getString("Teacher_phone"));
            teacher_mail.setText(arguments.getString("Teacher_mail"));
            teacher_venue.setText(arguments.getString("Teacher_venue"));


            loadInfo();



        }


        return myView;
    }






}
