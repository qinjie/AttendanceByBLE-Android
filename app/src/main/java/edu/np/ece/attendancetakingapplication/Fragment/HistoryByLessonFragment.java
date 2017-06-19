package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.R;


public class HistoryByLessonFragment extends Fragment {
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

    private List<TimetableResult> data = new ArrayList<>();
    private List<Integer> itemType = new ArrayList<>();


    public HistoryByLessonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryByLessonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryByLessonFragment newInstance(String param1, String param2) {
        HistoryByLessonFragment fragment = new HistoryByLessonFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_history_by_lesson, container, false);
        return myView;
    }




}
