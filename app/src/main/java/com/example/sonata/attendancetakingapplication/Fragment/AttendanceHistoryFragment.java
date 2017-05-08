package com.example.sonata.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sonata.attendancetakingapplication.Adapter.HistoryListAdapter;
import com.example.sonata.attendancetakingapplication.LogInActivity;
import com.example.sonata.attendancetakingapplication.Model.HistoricalResult;
import com.example.sonata.attendancetakingapplication.NavigationActivity;
import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.R;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerApi;
import com.example.sonata.attendancetakingapplication.Retrofit.ServerCallBack;
import com.example.sonata.attendancetakingapplication.Retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class AttendanceHistoryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private List<HistoricalResult> historicalList;

    private View myView;
    private Activity context;

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

    private void initHistoricalList() {
        HistoryListAdapter adapter = new HistoryListAdapter(context, R.layout.item_history, historicalList);
        adapter.notifyDataSetChanged();

        ListView listView = (ListView) myView.findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }

    private void loadHistoricalRecords() {
        Preferences.showLoading(context, "History", "Loading data from server...");
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
                public void onFailure(Call<List<HistoricalResult>> call, Throwable t) {
                    super.onFailure(call, t);
                    Preferences.dismissLoading();

//                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity().getBaseContext()).create();
//                    alertDialog.setTitle("This function needs internet connection");
//                    alertDialog.setMessage("Please turn on internet to get latest update about you attendance history.");
//                    alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//
//                                    Intent intent = new Intent(getActivity().getBaseContext(), NavigationActivity.class);
//                                    startActivity(intent);
//                                    getActivity().finish();
//
//
//                                }
//                            });
//                    alertDialog.show();


                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
                    builder.setTitle("This function needs internet connection");
                    builder.setMessage("Please turn on internet to get latest update about you attendance history.");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, final int i) {
                                    dialog.dismiss();

                                    Intent intent = new Intent(getActivity().getBaseContext(), NavigationActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                    builder.create().show();

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
