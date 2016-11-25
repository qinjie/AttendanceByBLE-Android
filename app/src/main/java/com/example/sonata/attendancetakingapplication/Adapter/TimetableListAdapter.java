package com.example.sonata.attendancetakingapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Preferences;
import com.example.sonata.attendancetakingapplication.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sonata on 11/9/2016.
 */

public class TimetableListAdapter extends ArrayAdapter<TimetableResult> {

    Context context;
    int layoutResourceId;
    int layoutSeparatorId;
    List<TimetableResult> data;
    List<Integer> dataType;

    public TimetableListAdapter(Context context, int layoutResourceId, int layoutSeparatorId, List<TimetableResult> data, List<Integer> dataType) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.layoutSeparatorId = layoutSeparatorId;
        this.context = context;
        this.data = data;
        this.dataType = dataType;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataType.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return Preferences.LIST_ITEM_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SubjectHolder subjectHolder = null;
        SeparatorHolder separatorHolder = null;

        int itemType = getItemViewType(position);

        if(row == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (itemType)
            {
                case Preferences.LIST_ITEM_TYPE_1:
                    row = mInflater.inflate(layoutSeparatorId, parent, false);
                    separatorHolder = new SeparatorHolder();
                    separatorHolder.tvWeekDay = (TextView) row.findViewById(R.id.separator_text);
                    row.setTag(separatorHolder);
                    break;

                case Preferences.LIST_ITEM_TYPE_2:
                    row = mInflater.inflate(layoutResourceId, parent, false);
                    subjectHolder = new SubjectHolder();
                    subjectHolder.tvStartTime   = (TextView) row.findViewById(R.id.timetable_start_time);
                    subjectHolder.tvEndTime     = (TextView) row.findViewById(R.id.timetable_end_time);
                    subjectHolder.tvSubjectArea = (TextView) row.findViewById(R.id.timetable_subject_area);
                    subjectHolder.tvLocation    = (TextView) row.findViewById(R.id.timetable_location);
                    row.setTag(subjectHolder);
                    break;
            }
        }
        else
        {
            switch (itemType)
            {
                case Preferences.LIST_ITEM_TYPE_1:
                    separatorHolder = (SeparatorHolder) row.getTag();
                    break;

                case Preferences.LIST_ITEM_TYPE_2:
                    subjectHolder = (SubjectHolder) row.getTag();
                    break;
            }
        }

        try
        {
            TimetableResult subject = data.get(position);
            switch (itemType)
            {
                case Preferences.LIST_ITEM_TYPE_1:
                    separatorHolder.tvWeekDay.setText(subject.getLesson_date().getDate());
                    break;

                case Preferences.LIST_ITEM_TYPE_2:
                    subjectHolder.tvStartTime.setText(subject.getLesson().getStart_time());
                    subjectHolder.tvEndTime.setText(subject.getLesson().getEnd_time());
                    subjectHolder.tvSubjectArea.setText(subject.getLesson().getSubject_area() + " "
                            + subject.getLesson().getCatalog_number());
                    subjectHolder.tvLocation.setText(subject.getVenue().getAddress());
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return row;
    }

    static class SubjectHolder
    {
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvSubjectArea;
        TextView tvLocation;
    }

    static class SeparatorHolder
    {
        TextView tvWeekDay;
    }
}


