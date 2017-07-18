package edu.np.ece.attendancetakingapplication.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.np.ece.attendancetakingapplication.ChangePasswordActivity;
import edu.np.ece.attendancetakingapplication.LogInActivity;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerApi;
import edu.np.ece.attendancetakingapplication.Retrofit.ServerCallBack;
import edu.np.ece.attendancetakingapplication.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link } subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity context;

    private View InfoView;

    private List<TimetableResult> timetablelist;

    private Activity context1;

    @BindView(R.id.tvName)
    TextView stName;

    @BindView(R.id.tvAcad)
    TextView stAcad;

    @BindView(R.id.tvEmail)
    TextView stEmail;

    @BindView(R.id.tvPhone)
    TextView stPhone;




    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
            context1 = this.getActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfoView= inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this,InfoView);

        SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);
        String nameStudent = pref.getString("isStudent","true");
        String infoLogin = pref.getString("isLogin","false");
        if(nameStudent.equals("true") && infoLogin.equals("true")){
            String stname = pref.getString("student_name","");
            final String stacad = pref.getString("student_acad","");
//            String stEmail = pref.getString("student_email","");

            stName.setText(stname);

            Bundle arguments = getArguments();



            String aucode = pref.getString("authorizationCode",null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class,aucode);
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek("students");
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try{
                        timetablelist = response.body();
                       if(timetablelist == null){
                           final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                           builder.setTitle(R.string.another_login_title);
                           builder.setMessage(R.string.another_login_content);
                           builder.setPositiveButton("OK",
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(final DialogInterface dialog, final int i) {
                                           Preferences.clearStudentInfo();
                                           Intent intent = new Intent(getActivity(), LogInActivity.class);
                                           startActivity(intent);
                                       }
                                   });
                           builder.create().show();

                       }
                       else{

                           for (int i = 0; i < timetablelist.size(); i++){
                               stEmail.setText(String.valueOf(timetablelist.get(0).getStudentList().get(0).getEmail()));
                               stPhone.setText(String.valueOf(timetablelist.get(0).getStudentList().get(0).getPhone_number()));
                               String acadLevel = String.valueOf(timetablelist.get(0).getStudentList().get(0).getAcad_level());
                               stAcad.setText(stacad+"\n"+acadLevel);
                               break;

                           }
                       }


                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        setUserSettingLayout();
        return InfoView;




    }

    private void setUserSettingLayout() {
        TextView ChangePassword = (TextView) InfoView.findViewById(R.id.btChange_password);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        TextView Signout = (TextView) InfoView.findViewById(R.id.btSignout);
        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.clearStudentInfo();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event





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

}
