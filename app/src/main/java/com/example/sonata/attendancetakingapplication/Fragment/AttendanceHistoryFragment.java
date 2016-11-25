package com.example.sonata.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sonata.attendancetakingapplication.Adapter.HistoryListAdapter;
import com.example.sonata.attendancetakingapplication.Model.HistoricalResult;
import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.NavigationActivity;
import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.R;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<HistoricalResult> historicalList;

    private View myView;
    private Activity context;

    public static View.OnClickListener myOnClickListener;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        context = getActivity();
        setHasOptionsMenu(true);
    }

    private void initHistoricalList()
    {
        HistoryListAdapter adapter = new HistoryListAdapter(context, R.layout.item_history, historicalList);
        adapter.notifyDataSetChanged();

        ListView listView = (ListView) myView.findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }

    private void loadHistoricalRecords()
    {
        Preferences.showLoading(context, "History", "Loading data from server...");
        try
        {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            Call<List<HistoricalResult>> call = client.getHistoricalReports();
            call.enqueue(new ServerCallBack<List<HistoricalResult>>() {
                @Override
                public void onResponse(Call<List<HistoricalResult>> call, Response<List<HistoricalResult>> response) {
                    try{
                        Preferences.dismissLoading();

                        historicalList = response.body();
                        initHistoricalList();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
