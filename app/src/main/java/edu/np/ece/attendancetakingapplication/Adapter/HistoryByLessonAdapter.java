package edu.np.ece.attendancetakingapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.Lesson;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseHelper;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseManager;
import edu.np.ece.attendancetakingapplication.OrmLite.Subject;
import edu.np.ece.attendancetakingapplication.OrmLite.SubjectDateTime;
import edu.np.ece.attendancetakingapplication.R;

/**
 * Created by MIYA on 23/06/17.
 */

public class HistoryByLessonAdapter extends ArrayAdapter<HistoricalResult> {
    Context context;
    int layoutResourceId;
    List<HistoricalResult> data;
    private int totalSlot;
    private int limitSlot;
    private int absentedSlot;
    private int presentedSlot;
    private int lateSlot;
    private int remainingSlot;
    private int attendedPercent;


    public HistoryByLessonAdapter(Context context, int layoutResourceId, List<HistoricalResult> data) {
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
        HistoryByLessonAdapter.ViewHolder holder = null;
        if(row==null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder = new HistoryByLessonAdapter.ViewHolder();
            holder.tvLessonDate=(TextView)row.findViewById(R.id.lesson_date);
            holder.tvLesson_timeslot=(TextView)row.findViewById(R.id.lesson_timeslot);
            holder.tvAttendance_time=(TextView)row.findViewById(R.id.attendance_time);
            holder.imgAttendance_rate=(ImageView)row.findViewById(R.id.attendance_rate);
            row.setTag(holder);

        }
        else {
            holder = (HistoryByLessonAdapter.ViewHolder) row.getTag();
        }



        SharedPreferences getName=context.getSharedPreferences("valueOfTB",Context.MODE_PRIVATE); //点击进入后的lesson name
        //比较historicallist里的data和当前lesson name

        for(int i=0;i<data.size();i++){
            String name=getName.getString("Catalog","error");
            String dataname=data.get(i).getLesson_name();

            if(dataname.equals(name)){
                //通过验证 获得数据
                totalSlot = Integer.valueOf(data.get(i).getTotal());
                limitSlot = totalSlot / 5;

                absentedSlot = Integer.valueOf(data.get(i).getAbsented());
                presentedSlot = Integer.valueOf(data.get(i).getPresented());
                lateSlot = Integer.valueOf(data.get(i).getLate());
                remainingSlot = limitSlot - absentedSlot;
                attendedPercent = (int) ((float) 100 * presentedSlot / totalSlot);
                holder.tvLessonDate.setText(data.get(i).getLesson_name());

                break;

            }
            else {
                holder.tvLessonDate.setText("error");
            }


        }


        /*List<Subject> listSubject = DatabaseManager.getInstance().QueryBuilder("catalog_number",getName.getString("Catalog","error"));
            //for内置段语句error：list中size大于1的时候 显示数组越界
        for(int i=0;i<listSubject.size();i++){
            Subject subject=listSubject.get(i);
            List <SubjectDateTime> subjectDateTimelist =subject.getSubject_Datetime();//存入了好几个datetimeobject
            for (int n=0;n<subjectDateTimelist.size();n++) {
                holder.tvLesson_timeslot.setText(subjectDateTimelist.get(i).getStartTime());
            }
        }

        */

       /* SharedPreferences sendValue = context.getSharedPreferences("valueOfAttendance",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sendValue.edit(); //send values to detailsFragment to show
        editor.putInt("totalSlot",totalSlot);
        editor.putInt("absentedSlot",absentedSlot);
        editor.putInt("presentedSlot",presentedSlot);
        editor.putInt("lateSlot",lateSlot);
        editor.putInt("attendedPercent",attendedPercent);
        editor.commit();*/
        //延迟


        if (remainingSlot <= 0) {
            //display for subject if student have more than maximum absent slot

            holder.imgAttendance_rate.setImageResource(R.drawable.circle_red_32);
        } else {
            //display for subject if student still not reach maximum absent slot

            if (attendedPercent <= 90) {
                holder.imgAttendance_rate.setImageResource(R.drawable.circle_orange_32);
            } else {
                holder.imgAttendance_rate.setImageResource(R.drawable.circle_green_32);
            }
        }





        return row;
    }
    static class ViewHolder {
        TextView tvLessonDate;
        TextView tvLesson_timeslot;
        TextView tvAttendance_time;
        ImageView imgAttendance_rate;
    }



}
