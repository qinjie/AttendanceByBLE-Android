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

import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
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

public class HistoryByLessonAdapter extends ArrayAdapter<AttendanceResult> {
    Context context;
    int layoutResourceId;
    List<AttendanceResult> data;



    public HistoryByLessonAdapter(Context context, int layoutResourceId, List<AttendanceResult> data) {
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
        for (int i =0;i<data.size();i++){
            String lesson_id=getName.getString("Lesson_id","error-0");
            String dataLesson_id=data.get(i).getLesson_date().getLesson_id();
            if (lesson_id.equals(dataLesson_id)){
                //pass verification and get the wanted value

                List<Subject> listSubject = DatabaseManager.getInstance().QueryBuilder("lesson_id",dataLesson_id);
                List<SubjectDateTime> subjectDateTimeList=listSubject.get(0).getSubject_Datetime();
                //逻辑还有问题！ 1）同一科目不同时间段的attendance应该都要显示 2）重复显示数据
                holder.tvLessonDate.setText(data.get(i).getLesson_date().getLdate()+"("+data.get(i).getLesson_date().getDate()+")");
                holder.tvLesson_timeslot.setText(subjectDateTimeList.get(0).getStartTime()+" - "+subjectDateTimeList.get(0).getEndTime());
                holder.tvAttendance_time.setText(data.get(i).getRecorded_time());

                String status = data.get(i).getStatus();
                if(status.equals("0")){
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_green_32);
                }
                else if(status.equals("-1")){
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_red_32);
                }
                else {
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_orange_32);
                }



                break;

            }
            /*else {
                holder.tvLessonDate.setText("ERROR");

            }*/

        }





      //  data.clear();
        return row;
    }
    static class ViewHolder {
        TextView tvLessonDate;
        TextView tvLesson_timeslot;
        TextView tvAttendance_time;
        ImageView imgAttendance_rate;
    }



}
