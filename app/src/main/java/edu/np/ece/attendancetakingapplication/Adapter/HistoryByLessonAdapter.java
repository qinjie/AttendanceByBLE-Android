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

public class HistoryByLessonAdapter extends ArrayAdapter<Subject> {
    Context context;
    int layoutResourceId;
    List<Subject> data;
    List<SubjectDateTime> subjectDateTimes;
    List<String> recordedTime;
    List<String> ldate;
    List<String> date;
    List<String> status;


    public HistoryByLessonAdapter(Context context, int layoutResourceId, List<Subject> data,List<SubjectDateTime> subjectDateTimes,List<String> recordedTime,List<String> ldate,List<String> date,List<String> status) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.subjectDateTimes=subjectDateTimes;
        this.recordedTime=recordedTime;
        this.ldate=ldate;
        this.date=date;
        this.status=status;

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

        try {
            holder.tvLessonDate.setText(ldate.get(position)+" ("+date.get(position)+")");
            holder.tvAttendance_time.setText(recordedTime.get(position));
            holder.tvLesson_timeslot.setText(subjectDateTimes.get(position).getStartTime()+" - "+subjectDateTimes.get(position).getEndTime());
            String state= status.get(position);
            if(state.equals("0")){

                holder.imgAttendance_rate.setImageResource(R.drawable.circle_green_32);
            }
            else if(state.equals("-1")){
                holder.tvAttendance_time.setText("");
                holder.imgAttendance_rate.setImageResource(R.drawable.circle_red_32);
            }
            else {
                holder.imgAttendance_rate.setImageResource(R.drawable.circle_orange_32);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        /* SharedPreferences getName=context.getSharedPreferences("valueOfTB",Context.MODE_PRIVATE); //点击进入后的lesson name
       for (int i =0;i<data.size();i++){ //判断datalist里有多少个 和 所点击的lesson相符的
            String lesson_id=getName.getString("Lesson_id","error-0");
            String dataLesson_id=data.get(i).getLesson_date().getLesson_id();
            if (lesson_id.equals(dataLesson_id)){
                //pass verification and get the wanted value
                int count = 0;
                List<Subject> listSubject = DatabaseManager.getInstance().QueryBuilder("lesson_id",dataLesson_id);
                List<SubjectDateTime> subjectDateTimeList=listSubject.get(count).getSubject_Datetime();
                //逻辑还有问题！ 1）同一科目不同时间段的attendance应该都要显示 2）重复显示数据
                holder.tvLessonDate.setText(data.get(i).getLesson_date().getLdate()+"("+data.get(i).getLesson_date().getDate()+")");
                holder.tvLesson_timeslot.setText(subjectDateTimeList.get(count).getStartTime()+" - "+subjectDateTimeList.get(count).getEndTime());
                holder.tvAttendance_time.setText(data.get(i).getRecorded_time());

                String status = data.get(i).getStatus();
               *//* if(status.equals("0")){
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_green_32);
                }
                else if(status.equals("-1")){
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_red_32);
                }
                else {
                    holder.imgAttendance_rate.setImageResource(R.drawable.circle_orange_32);
                }*//*


                count++;
                //break;

            }

         //   break;

        }
*/




        return row;
    }
    static class ViewHolder {
        TextView tvLessonDate;
        TextView tvLesson_timeslot;
        TextView tvAttendance_time;
        ImageView imgAttendance_rate;
    }



}
