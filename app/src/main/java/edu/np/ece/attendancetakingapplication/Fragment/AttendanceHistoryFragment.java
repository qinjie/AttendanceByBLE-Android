package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Adapter.HistoryListAdapter;
import edu.np.ece.attendancetakingapplication.DetailsActivity;
import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
import edu.np.ece.attendancetakingapplication.NavigationActivity;
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


public class AttendanceHistoryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Activity context;
    private Calendar calendar;
    private List<AttendanceResult> historicalList;
    private View myView;

    private List<Integer> itemType = new ArrayList<>();
    private List<AttendanceResult> data = new ArrayList<>();


    public AttendanceHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceHistoryFragment newInstance(String param1, String param2) {
        AttendanceHistoryFragment fragment = new AttendanceHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        calendar = Calendar.getInstance();
    }

    public void onResume() {
        super.onResume();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(dateFormat.format(calendar.getTime()));
    }

    private boolean isOnDifferentDate(AttendanceResult temp1, AttendanceResult temp2) {

        if (temp1.getLesson_date().getLdate().compareToIgnoreCase(temp2.getLesson_date().getLdate()) == 0) {

            return false;
        }
        return true;
    }
    private void addItem(AttendanceResult subject, Integer type) {
        data.add(subject);
        itemType.add(type);
    }

    private void initHistoricalList() {

        HistoryListAdapter adapter = null;
        try {
            final ListView listView = (ListView) myView.findViewById(R.id.history_list);

            for (int i = 0; i < historicalList.size(); i++) {
                if (i == 0 || isOnDifferentDate(historicalList.get(i), historicalList.get(i - 1))) {
                    addItem(historicalList.get(i), Preferences.LIST_ITEM_TYPE_1);
                }
                addItem(historicalList.get(i), Preferences.LIST_ITEM_TYPE_2);
            }

            adapter = new HistoryListAdapter(context, R.layout.item_history_subject, R.layout.item_week_day, data, itemType);

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<Subject> subjectList= DatabaseManager.getInstance().QueryBuilder("lesson_id",data.get(position).getLesson_date().getLesson_id());
                    List<SubjectDateTime> subjectDateTimeList=subjectList.get(0).getSubject_Datetime();

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);

                    /*intent.putExtra("Catalog",subjectList.get(0).getCatalog_number());
                    intent.putExtra("Area",subjectList.get(0).getSubject_area());
                    //lesson full name + lesson credit
                    intent.putExtra("Group",subjectList.get(0).getClass_section());
                    intent.putExtra("Timestart",subjectDateTimeList.get(0).getStartTime());
                    intent.putExtra("Timeend",subjectDateTimeList.get(0).getEndTime());
                    intent.putExtra("Venue",subjectList.get(0).getLocation());
                    intent.putExtra("Teacher_name",subjectList.get(0).getTeacher_name());
                    intent.putExtra("Teacher_phone",subjectList.get(0).getTeacher_phone());
                    intent.putExtra("Teacher_mail",subjectList.get(0).getTeacher_email());
                    intent.putExtra("Teacher_venue",subjectList.get(0).getTeacher_office());
                    intent.putExtra("Lesson_date",subjectDateTimeList.get(0).getLesson_date());
                    intent.putExtra("Lesson_id",data.get(position).getLesson_date().getLesson_id());

                    intent.putExtra("lesson_name",subjectList.get(0).getLesson_name());
                    intent.putExtra("credit_unit",subjectList.get(0).getCredit_unit());*/

                    intent.putExtra("Catalog",data.get(position).getLesson().getCatalog_number());
                    intent.putExtra("Area",data.get(position).getLesson().getSubject_area());
                    //lesson full name + lesson credit
                    intent.putExtra("Group",data.get(position).getLesson().getClass_section());
                    intent.putExtra("Timestart",data.get(position).getLesson().getStart_time());
                    intent.putExtra("Timeend",data.get(position).getLesson().getEnd_time());
                    intent.putExtra("Venue",data.get(position).getLesson().getVenue_id());
                   /* intent.putExtra("Teacher_name",historicalList.get(position).getLecturer());
                    intent.putExtra("Teacher_phone",subjectList.get(0).getTeacher_phone());
                    intent.putExtra("Teacher_mail",subjectList.get(0).getTeacher_email());
                    intent.putExtra("Teacher_venue",subjectList.get(0).getTeacher_office());*/
                    intent.putExtra("Lesson_date",data.get(position).getLesson_date().getLdate());
                    intent.putExtra("Lesson_id",data.get(position).getLesson_date().getLesson_id());

                    intent.putExtra("lesson_name",data.get(position).getLesson().getLesson_name());
                    intent.putExtra("credit_unit",data.get(position).getLesson().getCredit_unit());

                    startActivity(intent);
                    Toast.makeText(getActivity(),data.get(position).getLesson_date().getLesson_id(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadHistoricalRecords() {
        Preferences.showLoading(context, "History", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            Call<List<AttendanceResult>> call = client.Attendance();
            call.enqueue(new ServerCallBack<List<AttendanceResult>>() {
                @Override
                public void onResponse(Call<List<AttendanceResult>> call, Response<List<AttendanceResult>> response) {
                    try {

                        historicalList = response.body();
                        if (historicalList == null) {
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
                        } else {
                            initHistoricalList();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();

                }

                @Override
                public void onFailure(Call<List<AttendanceResult>> call, Throwable t) {
                    super.onFailure(call, t);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        myView = inflater.inflate(R.layout.fragment_attendance_history, container, false);

        loadHistoricalRecords();

        return myView;
    }



}
