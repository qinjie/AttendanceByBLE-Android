package edu.np.ece.attendancetakingapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Fragment.HistoryByLessonFragment;
import edu.np.ece.attendancetakingapplication.Fragment.LessonDetailsFragment;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    public ViewpagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        fragmentList=fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);

    }

    @Override
    public int getCount() {
        //   return fragmentList!=null?fragmentList.size():0;
        return fragmentList.size();
    }

}
