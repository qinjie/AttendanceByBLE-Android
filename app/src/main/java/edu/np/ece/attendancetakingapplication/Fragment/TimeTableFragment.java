package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.np.ece.attendancetakingapplication.Adapter.TimetableListAdapter;
import edu.np.ece.attendancetakingapplication.BeaconScanActivation;
import edu.np.ece.attendancetakingapplication.DetailsActivity;
import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.Lecturer;
import edu.np.ece.attendancetakingapplication.Model.Lesson;
import edu.np.ece.attendancetakingapplication.Model.LessonBeacon;
import edu.np.ece.attendancetakingapplication.Model.LessonDate;
import edu.np.ece.attendancetakingapplication.Model.StudentInfo;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.Model.UserBeacon;
import edu.np.ece.attendancetakingapplication.Model.Venue;
import edu.np.ece.attendancetakingapplication.NavigationActivity;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseHelper;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseManager;
import edu.np.ece.attendancetakingapplication.OrmLite.Student;
import edu.np.ece.attendancetakingapplication.OrmLite.Subject;
import edu.np.ece.attendancetakingapplication.OrmLite.SubjectDateTime;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class TimeTableFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


//    private OnFragmentInteractionListener mListener;

    private Activity context;
    private Calendar calendar;

    private List<TimetableResult> timetableList;

    private View myView;

    private List<TimetableResult> data = new ArrayList<>();

    private List<Integer> itemType = new ArrayList<>();

    public TimeTableFragment() {
        // Required empty public constructor
    }

    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
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

    private boolean isOnDifferentDate(TimetableResult temp1, TimetableResult temp2) {
        if (temp1.getLesson_date().getLdate().compareToIgnoreCase(temp2.getLesson_date().getLdate()) == 0) {
            return false;
        }
        return true;
    }

    private void addItem(TimetableResult subject, Integer type) {
        data.add(subject);
        itemType.add(type);
    }

    private void initTimetableList() {
        try {
            final ListView listView = (ListView) myView.findViewById(R.id.timetable_list);

            for (int i = 0; i <timetableList.size(); i++) {
                if (i == 0 || isOnDifferentDate(timetableList.get(i), timetableList.get(i - 1))) {
                    addItem(timetableList.get(i), Preferences.LIST_ITEM_TYPE_1);
                }
                addItem(timetableList.get(i), Preferences.LIST_ITEM_TYPE_2);
            }

            TimetableListAdapter adapter = new TimetableListAdapter(context, R.layout.item_subject, R.layout.item_week_day, data, itemType);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                   // Intent intent = new Intent();
                   // intent.setClass(getActivity(),DetailsActivity.class);
                    intent.putExtra("Catalog",data.get(i).getLesson().getCatalog_number());
                    intent.putExtra("Area",data.get(i).getLesson().getSubject_area());
                    //lesson full name + lesson credit
                    intent.putExtra("Group",data.get(i).getLesson().getClass_section());
                    intent.putExtra("Timestart",data.get(i).getLesson().getStart_time());
                    intent.putExtra("Timeend",data.get(i).getLesson().getEnd_time());
                    intent.putExtra("Venue",data.get(i).getVenue().getAddress());
                    intent.putExtra("Teacher_name",data.get(i).getLecturers().getName());
                    intent.putExtra("Teacher_phone",data.get(i).getLecturers().getPhone());
                    intent.putExtra("Teacher_mail",data.get(i).getLecturers().getEmail());
                    intent.putExtra("Teacher_venue",data.get(i).getLecturers().getOffice());
                    intent.putExtra("Lesson_date",data.get(i).getLesson_date().getDate());


                    startActivity(intent);


                    Toast.makeText(getActivity().getBaseContext(), data.get(i).getLesson().getCatalog_number(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTimetable() {
        Preferences.showLoading(context, "Timetable", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            String expand = new String("lesson,lesson_date,lecturers,venue");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        timetableList = response.body();
                        if (timetableList == null) {
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
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();

                        } else {
                            initTimetableList();

                            //clear old data
                            DatabaseManager.getInstance().deleteAllSubject();

                            //add to DB
                            for (int i = 0; i < timetableList.size(); i++) {
                                Subject aSubject = new Subject();
                                aSubject.setLesson_id(timetableList.get(i).getLesson_id());
                                aSubject.setSubject_area(timetableList.get(i).getLesson().getSubject_area());
                                aSubject.setCatalog_number(timetableList.get(i).getLesson().getCatalog_number());
                                aSubject.setLocation(timetableList.get(i).getVenue().getAddress());
                                aSubject.setUuid(timetableList.get(i).getLessonBeacon().getUuid());
                                aSubject.setTeacher_id(timetableList.get(i).getLecturers().getId());
                                aSubject.setTeacher_name(timetableList.get(i).getLecturers().getName());
/*
                                aSubject.setTeacher_office(timetableList.get(i).getLecturers().getOffice());
                                aSubject.setTeacher_phone(timetableList.get(i).getLecturers().getPhone());*/

                                aSubject.setTeacher_acad(timetableList.get(i).getLecturers().getAcad());
                                aSubject.setTeacher_email(timetableList.get(i).getLecturers().getEmail());

                                if (timetableList.get(i).getLecturers().getBeacon() == null) {
                                    aSubject.setTeacher_major("");
                                    aSubject.setTeacher_minor("");
                                } else {
                                    aSubject.setTeacher_major(timetableList.get(i).getLecturers().getBeacon().getMajor());
                                    aSubject.setTeacher_minor(timetableList.get(i).getLecturers().getBeacon().getMinor());
                                }
                                DatabaseManager.getInstance().addSubject(aSubject);

                                SubjectDateTime aSubjectDateTime = DatabaseManager.getInstance().newSubjectDateTimeItem();
                                aSubjectDateTime.setLesson_date_id(timetableList.get(i).getLesson_date().getId());
                                aSubjectDateTime.setStartTime(timetableList.get(i).getLesson().getStart_time());
                                aSubjectDateTime.setEndTime(timetableList.get(i).getLesson().getEnd_time());
                                aSubjectDateTime.setLesson_date(timetableList.get(i).getLesson_date().getLdate());
                                aSubjectDateTime.setSubject(aSubject);
                                DatabaseManager.getInstance().updateSubjectDateTimeItem(aSubjectDateTime);




                                for (StudentInfo theStudent : timetableList.get(i).getStudentList()) {
                                    Student aStudent = DatabaseManager.getInstance().newStudentItem();
                                    if (theStudent.getBeacon() == null) {
                                        aStudent.setBeacon_id("");
                                        aStudent.setBeacon_major("");
                                        aStudent.setBeacon_minor("");
                                    } else {
                                        aStudent.setBeacon_id(theStudent.getBeacon().getId());
                                        aStudent.setBeacon_major(theStudent.getBeacon().getMajor());
                                        aStudent.setBeacon_minor(theStudent.getBeacon().getMinor());
                                    }
                                    aStudent.setCard(theStudent.getCard());
                                    aStudent.setName(theStudent.getName());
                                    aStudent.setStudent_id(theStudent.getId());
                                    aStudent.setSubject(aSubject);
                                    DatabaseManager.getInstance().updateStudentItem(aStudent);

                                }

                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();

                    BeaconScanActivation.timetableList = timetableList;
                }

                @Override
                public void onFailure(Call<List<TimetableResult>> call, Throwable t) {
                    super.onFailure(call, t);
                    Preferences.dismissLoading();

                    //Create the temporary timetableList with minimum attribute on each element
                    //all unused attribute will be null
                    //if you want to add more information for display when offline,
                    //please add more attribute into objects in OrmLite folder first
                    //then get new attribute from server and set that attribute for OrmLite object in onResponse method code above
                    timetableList = new ArrayList<TimetableResult>();

                    List<Subject> listSubject = DatabaseManager.getInstance().getAllSubjects();

                    //Get data from DB
                    for (Subject tmp : listSubject) {
                        for (SubjectDateTime tmp2 : tmp.getSubject_Datetime()) {

                            TimetableResult aTimetableResult = new TimetableResult();
                            aTimetableResult.setLesson_id(tmp.getLesson_id());

                            Lesson aLesson = new Lesson();
                            aLesson.setSubject_area(tmp.getSubject_area());
                            aLesson.setCatalog_number(tmp.getCatalog_number());
                            aLesson.setStart_time(tmp2.getStartTime());
                            aLesson.setEnd_time(tmp2.getEndTime());
                            aTimetableResult.setLesson(aLesson);

                            LessonDate aLessonDate = new LessonDate();
                            aLessonDate.setId(tmp2.getLesson_date_id());
                            aLessonDate.setLesson_id(tmp.getLesson_id());
                            aLessonDate.setLdate(tmp2.getLesson_date());
                            aTimetableResult.setLesson_date(aLessonDate);

                            LessonBeacon aLessonBeacon = new LessonBeacon();
                            aLessonBeacon.setUuid(tmp.getUuid());
                            aTimetableResult.setLessonBeacon(aLessonBeacon);

                            Lecturer aLecturer = new Lecturer();
                            aLecturer.setId(tmp.getTeacher_id());
                            aLecturer.setName(tmp.getTeacher_name());
               /*             aLecturer.setOffice(tmp.getTeacher_office());
                            aLecturer.setPhone(tmp.getTeacher_phone());*/

                            aLecturer.setAcad(tmp.getTeacher_acad());
                            aLecturer.setEmail(tmp.getTeacher_email());

                            UserBeacon aLecturerBeacon = new UserBeacon();
                            aLecturerBeacon.setMajor(tmp.getTeacher_major());
                            aLecturerBeacon.setMinor(tmp.getTeacher_minor());
                            aLecturer.setBeacon(aLecturerBeacon);
                            aTimetableResult.setLecturers(aLecturer);

                            Venue aVenue = new Venue();
                            aVenue.setAddress(tmp.getLocation());
                            aTimetableResult.setVenue(aVenue);

                            List<StudentInfo> aStudentList = new ArrayList();
                            for (Student tmpStudent : tmp.getStudent_list()) {
                                StudentInfo aStudentInfo = new StudentInfo();
                                UserBeacon aStudentBeacon = new UserBeacon();
                                aStudentBeacon.setMajor(tmpStudent.getBeacon_major());
                                aStudentBeacon.setMinor(tmpStudent.getBeacon_minor());
                                aStudentInfo.setBeacon(aStudentBeacon);
                                aStudentInfo.setId(tmpStudent.getStudent_id());
                                aStudentInfo.setName(tmpStudent.getName());
                                aStudentList.add(aStudentInfo);
                            }
                            aTimetableResult.setStudentList(aStudentList);
                            timetableList.add(aTimetableResult);
                        }

                    }
                    initTimetableList();

                    BeaconScanActivation.timetableList = timetableList;

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
        myView = inflater.inflate(R.layout.fragment_time_table, container, false);

        loadTimetable();

        return myView;
    }

}
