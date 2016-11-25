package com.example.sonata.attendancetakingapplication;

import android.app.FragmentManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.example.sonata.attendancetakingapplication.Fragment.AttendanceHistoryFragment;
import com.example.sonata.attendancetakingapplication.Fragment.TimeTableFragment;
import com.example.sonata.attendancetakingapplication.Fragment.UserSettingFragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setLayoutContent();
    }

    private void setLayoutContent()
    {
        try
        {
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    android.app.Fragment fragment = null;
                    String title = null;
                    switch (tabId)
                    {
                        case R.id.tab_schedule:
                            fragment = new TimeTableFragment();
                            title = "Timetable";
                            break;
                        case R.id.tab_history:
                            fragment = new AttendanceHistoryFragment();
                            title = "History";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_user:
                            fragment = new UserSettingFragment();
                            title = "User setting";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        default:
                            fragment = new TimeTableFragment();
                            title = "Timetable";
                            break;
                    }

                    setActionBarTitle(title);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(@IdRes int tabId) {
                    android.app.Fragment fragment = null;
                    String title = null;
                    switch (tabId)
                    {
                        case R.id.tab_schedule:
                            title = "Timetable";
                            fragment = new TimeTableFragment();
                            break;
                        case R.id.tab_history:
                            title = "History";
                            fragment = new AttendanceHistoryFragment();
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_user:
                            title = "User setting";
                            fragment = new UserSettingFragment();
                            getSupportActionBar().setSubtitle(null);
                            break;
                        default:
                            title = "Timetable";
                            fragment = new TimeTableFragment();
                            break;
                    }

                    setActionBarTitle(title);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
