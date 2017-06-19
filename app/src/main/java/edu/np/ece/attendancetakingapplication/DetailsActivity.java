package edu.np.ece.attendancetakingapplication;

//import android.app.Fragment;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.np.ece.attendancetakingapplication.Fragment.HistoryByLessonFragment;
import edu.np.ece.attendancetakingapplication.Fragment.LessonDetailsFragment;
import edu.np.ece.attendancetakingapplication.Fragment.TimeTableFragment;

public  class DetailsActivity extends FragmentActivity  {
    @BindView(R.id.subjectCatalog_name)
    TextView subjectCatalog_name;
    @BindView(R.id.lesson_name)
    TextView lesson_name;

    @BindView(R.id.lesson_credit)
    TextView lesson_credit;
    @BindView(R.id.student_group)
    TextView student_group;
    @BindView(R.id.lesson_time)
    TextView lesson_time;
    @BindView(R.id.lesson_venue)
    TextView lesson_venue;

    ViewPager viewPager;
    List<Fragment> fragmentList;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        viewPager=(ViewPager)this.findViewById(R.id.viewPage);
        fragmentList = new ArrayList<Fragment>();


        Intent intent = getIntent();
        if(intent!=null){


           // android.app.Fragment fragment = null;
            Fragment fragment = new LessonDetailsFragment();
           // FragmentManager fragmentManager = getFragmentManager();
            //FragmentTransaction transaction= fragmentManager.beginTransaction();
             manager = getSupportFragmentManager();
            FragmentTransaction transaction= manager.beginTransaction();

            Bundle args=new Bundle();
            args.putString("Catalog",intent.getStringExtra("Catalog"));
            args.putString("Area",intent.getStringExtra("Area"));
            args.putString("Group",intent.getStringExtra("Group"));
            args.putString("Timestart",intent.getStringExtra("Timestart"));
            args.putString("Timeend",intent.getStringExtra("Timeend"));
            args.putString("Venue",intent.getStringExtra("Venue"));
            args.putString("Teacher_name",intent.getStringExtra("Teacher_name"));
            args.putString("Teacher_phone",intent.getStringExtra("Teacher_phone"));
            args.putString("Teacher_mail",intent.getStringExtra("Teacher_mail"));
            args.putString("Teacher_venue",intent.getStringExtra("Teacher_venue"));
            fragment.setArguments(args);

        //    transaction.replace(R.id.detailcontainer,fragment);
        //    transaction.commit();
            fragmentList.add(fragment);
            fragmentList.add(new HistoryByLessonFragment());
            ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),fragmentList);
            viewPager.setAdapter(viewpagerAdapter);

        }

        
    }








}
