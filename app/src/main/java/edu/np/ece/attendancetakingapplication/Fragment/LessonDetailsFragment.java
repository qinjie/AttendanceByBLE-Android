package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.R;


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

    private List<TimetableResult> timetableList;

    private View myView;


    private List<Integer> itemType = new ArrayList<>();
    //private List<HistoricalResult> data;

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

        TextView tvTotal=(TextView)myView.findViewById(R.id.Total);
        TextView tvPresent=(TextView)myView.findViewById(R.id.Present);
        TextView tvLate=(TextView)myView.findViewById(R.id.Late);
        TextView tvAbsent=(TextView)myView.findViewById(R.id.Absent);

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

            /*SharedPreferences getvalue= getActivity().getSharedPreferences("valueOfAttendance",Context.MODE_PRIVATE);
            String total=String.valueOf(getvalue.getInt("totalSlot",0))+" ("+String.valueOf(getvalue.getInt("attendedPercent",0))+"%)";
            String present = String.valueOf(getvalue.getInt("presentedSlot",0));
            String late = String.valueOf(getvalue.getInt("lateSlot",0));
            String absent = String.valueOf(getvalue.getInt("absentedSlot",0));
            tvTotal.setText(total);
            tvPresent.setText(present);
            tvLate.setText(late);
            tvAbsent.setText(absent);
*/




        }


        return myView;
    }






}
