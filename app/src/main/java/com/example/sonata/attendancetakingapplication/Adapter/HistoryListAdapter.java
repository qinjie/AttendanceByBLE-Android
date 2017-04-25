package com.example.sonata.attendancetakingapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.Model.HistoricalResult;
import com.example.sonata.attendancetakingapplication.R;

import java.util.List;

/**
 * Created by Sonata on 11/23/2016.
 */

public class HistoryListAdapter extends ArrayAdapter<HistoricalResult> {

    Context context;
    int layoutResourceId;
    List<HistoricalResult> data;

    public HistoryListAdapter(Context context, int layoutResourceId, List<HistoricalResult> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistoryListAdapter.ViewHolder holder = null;

        //Bind layout for displaying
        if (row == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder = new HistoryListAdapter.ViewHolder();
            holder.tvLessonName = (TextView) row.findViewById(R.id.subject_name);
            holder.tvAbsentSlots = (TextView) row.findViewById(R.id.absent_slots);
            holder.tvAttendanceRate = (TextView) row.findViewById(R.id.absent_rate);
            holder.tvAttendanceNumber = (TextView) row.findViewById(R.id.attendance_number);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        HistoricalResult record = data.get(position);

        holder.tvLessonName.setText(record.getLesson_name());

        int totalSlot = Integer.valueOf(record.getTotal());
        int limitSlot = totalSlot / 5;

        int absentedSlot = Integer.valueOf(record.getAbsented());
        int presentedSlot = Integer.valueOf(record.getPresented());
        int lateSlot = Integer.valueOf(record.getLate());
        int remainingSlot = limitSlot - absentedSlot;

        int attendedPercent = (int) ((float) 100 * presentedSlot / totalSlot);


        if (remainingSlot <= 0) {
            //display for subject if student have more than maximum absent slot
            holder.tvAbsentSlots.setText("You were absent " + absentedSlot + " times of this class");
            holder.tvAttendanceRate.setTextColor(Color.RED);
        } else {
            //display for subject if student still not reach maximum absent slot
            holder.tvAbsentSlots.setText("You can only miss this class " + remainingSlot + " times");
            if (attendedPercent <= 90) {
                holder.tvAttendanceRate.setTextColor(holder.tvAttendanceRate.getResources().getColor(R.color.orange));
            } else {
                holder.tvAttendanceRate.setTextColor(holder.tvAttendanceRate.getResources().getColor(R.color.green));
            }
        }

        holder.tvAttendanceRate.setText(attendedPercent + "%");

        holder.tvAttendanceNumber.setText("Absent: " + absentedSlot + " | Late: " + lateSlot);

        return row;
    }

    static class ViewHolder {
        TextView tvLessonName;
        TextView tvAbsentSlots;
        TextView tvAttendanceRate;
        TextView tvAttendanceNumber;
    }

}
