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
        loadDefaultFragment();
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
                    switch (tabId)
                    {
                        case R.id.tab_schedule:
                            fragment = new TimeTableFragment();
                            break;
                        case R.id.tab_history:
                            fragment = new AttendanceHistoryFragment();
                            break;
                        case R.id.tab_user:
                            fragment = new UserSettingFragment();
                            break;
                        default:
                            fragment = new TimeTableFragment();
                            break;
                    }

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
                    switch (tabId)
                    {
                        case R.id.tab_schedule:
                            fragment = new TimeTableFragment();
                            break;
                        case R.id.tab_history:
                            fragment = new AttendanceHistoryFragment();
                            break;
                        case R.id.tab_user:
                            fragment = new UserSettingFragment();
                            break;
                        default:
                            fragment = new TimeTableFragment();
                            break;
                    }

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

    private void loadDefaultFragment()
    {
        android.app.Fragment fragment = new TimeTableFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
