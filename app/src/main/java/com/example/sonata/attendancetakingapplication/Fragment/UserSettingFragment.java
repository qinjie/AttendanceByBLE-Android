package com.example.sonata.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.ChangePasswordActivity;
import com.example.sonata.attendancetakingapplication.JobScheduler.BeaconJobScheduler;
import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.R;


public class UserSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private Activity context;

    private View inflateView;


    BeaconJobScheduler testService;
    private static int kJobId = 0;

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
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }

            context = this.getActivity();

//            Intent startServiceIntent = new Intent(getActivity().getBaseContext(), BeaconJobScheduler.class);
//            getActivity().startService(startServiceIntent);
//
//            ComponentName serviceName = new ComponentName(context, BeaconJobScheduler.class);
//            JobInfo builder = new JobInfo.Builder(kJobId, serviceName)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                    .setPeriodic(30000)
//                    .build();
//            JobScheduler jobScheduler =
//                    (JobScheduler) getActivity().getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void startJob() {
//        ComponentName serviceComponent = new ComponentName(getActivity().getBaseContext(), BeaconJobScheduler.class);
//        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, serviceComponent);
//        builder.setMinimumLatency(1 * 1000); // wait at least
//        builder.setOverrideDeadline(2 * 1000); // maximum delay
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
//        builder.setRequiresDeviceIdle(true); // device should be idle
//        builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        PersistableBundle bundle = new PersistableBundle();
//        bundle.putString("abc", "123");
//        builder.setExtras(bundle);
//        JobScheduler jobScheduler =
//                (JobScheduler) getActivity().getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(builder.build());
//    }
//
//    public void cancelJob() {
//        JobScheduler tm = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        tm.cancelAll();
//    }

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

        setUserSettingLayout();

        return inflateView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
