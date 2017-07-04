package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Adapter.HistoryByLessonAdapter;
import edu.np.ece.attendancetakingapplication.Adapter.HistoryListAdapter;
import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.NavigationActivity;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


public class HistoryByLessonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private List<AttendanceResult> historicalList;

    private View myView;




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



    private void initHistorytableList(){
        try {

            HistoryByLessonAdapter adapter = new HistoryByLessonAdapter(getActivity(), R.layout.item_history_by_lesson, historicalList);

            final ListView listView = (ListView) myView.findViewById(R.id.historybylesson_list);

            listView.setAdapter(adapter);
          //  int i = listView.getCount();
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadList(){
        Preferences.showLoading(getActivity(), "History", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            Call<List<AttendanceResult>> call = client.getAttendanceReports();
            call.enqueue(new ServerCallBack<List<AttendanceResult>>() {
                @Override
                public void onResponse(Call<List<AttendanceResult>> call, Response<List<AttendanceResult>> response) {
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
                            initHistorytableList();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();

                }

                @Override
                public void onFailure(Call<List<AttendanceResult>> call, Throwable t) {
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
        myView= inflater.inflate(R.layout.fragment_history_by_lesson, container, false);
        loadList();
        return myView;
    }





}
