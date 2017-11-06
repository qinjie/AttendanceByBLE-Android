package edu.np.ece.attendancetakingapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.Lesson;
import edu.np.ece.attendancetakingapplication.Model.LessonDate;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;
import edu.np.ece.attendancetakingapplication.OrmLite.DatabaseManager;
import edu.np.ece.attendancetakingapplication.OrmLite.Subject;
import edu.np.ece.attendancetakingapplication.OrmLite.SubjectDateTime;
import edu.np.ece.attendancetakingapplication.Preferences;
import edu.np.ece.attendancetakingapplication.R;

/**
 * Created by Sonata on 11/23/2016.
 */

public class HistoryListAdapter extends ArrayAdapter<AttendanceResult> {

    Context context;
    int layoutResourceId;
    int layoutSeparatorId;
    List<AttendanceResult> data;
    List<Integer> dataType;

    public HistoryListAdapter(Context context, int layoutResourceId, int layoutSeparatorId,List<AttendanceResult> data,List<Integer> dataType) {
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
    public int getItemViewType(int position) {
        return dataType.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return Preferences.LIST_ITEM_TYPE_COUNT;
    }

    public List<LessonDate> ChangeOrderOfDate(){
        List<LessonDate> dateTimeDecrease=null;
        for(int i =0; i<data.size();i++){
            dateTimeDecrease=new ArrayList<>();
            dateTimeDecrease.add(i,data.get(i).getLesson_date());


        }


        Collections.reverse(dateTimeDecrease);
        return dateTimeDecrease;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistoryListAdapter.SubjectHolder subjectHolder = null;
        HistoryListAdapter.SeparatorHolder separatorHolder = null;

        int itemType = getItemViewType(position);

        //Bind layout for displaying
        if (row == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (itemType) {
                //Bind layout for Display weekday
                case Preferences.LIST_ITEM_TYPE_1:
                    row = mInflater.inflate(layoutSeparatorId, parent, false);
                    separatorHolder = new SeparatorHolder();
                    separatorHolder.tvWeekDay = (TextView) row.findViewById(R.id.separator_text);
                    row.setTag(separatorHolder);
                    row.setFocusable(false);
                    break;

                //Bind layout for Display subject
                case Preferences.LIST_ITEM_TYPE_2:
                    row = mInflater.inflate(layoutResourceId, parent, false);
                    subjectHolder = new SubjectHolder();
                    subjectHolder.tvStartTime = (TextView) row.findViewById(R.id.history_start_time);
                    subjectHolder.tvEndTime = (TextView) row.findViewById(R.id.history_end_time);
                    subjectHolder.tvSubjectArea = (TextView) row.findViewById(R.id.history_subject_area);
                    subjectHolder.tvClass = (TextView) row.findViewById(R.id.history_class);
                    subjectHolder.tvAttendance=(TextView)row.findViewById(R.id.history_attendance);
                    subjectHolder.imgAttendance=(ImageView)row.findViewById(R.id.imgAttendance);
                    row.setTag(subjectHolder);
                    break;
            }
        } else {
                switch (itemType){
                    case Preferences.LIST_ITEM_TYPE_1:
                        separatorHolder = (SeparatorHolder) row.getTag();
                        break;
                    case Preferences.LIST_ITEM_TYPE_2:
                        subjectHolder = (SubjectHolder) row.getTag();
                        break;
                }
        }


        try {
            AttendanceResult subject = data.get(position);
/*
            String lessonId =subject.getLesson_date().getLesson_id();
            List<Subject> listSubject=new ArrayList<>();
            listSubject = DatabaseManager.getInstance().QueryBuilder("lesson_id",lessonId);
            List<SubjectDateTime> subjectDateTimeList=new ArrayList<>();
            subjectDateTimeList=listSubject.get(0).getSubject_Datetime();*/

            switch (itemType) {
                //Put data into layout for display weekday
                case Preferences.LIST_ITEM_TYPE_1:
                    //show latest day at the top 时间顺序 从新到旧
                    //List<LessonDate> dateTimeDecrease =ChangeOrderOfDate();
                    separatorHolder.tvWeekDay.setText(subject.getLesson_date().getLdate()+" ("+subject.getLesson_date().getDate()+")");
                  //  separatorHolder.tvWeekDay.setText(dateTimeDecrease.get(position).getLdate()+" ("+dateTimeDecrease.get(position).getDate()+")");
                    break;

                //Put data into layout for display Subject
                case Preferences.LIST_ITEM_TYPE_2:

                    subjectHolder.tvStartTime.setText(subject.getLesson().getStart_time());
                    subjectHolder.tvEndTime.setText(subject.getLesson().getEnd_time());
                    subjectHolder.tvSubjectArea.setText(subject.getLesson().getSubject_area()+" "+subject.getLesson().getCatalog_number());
                    subjectHolder.tvAttendance.setText(subject.getRecorded_time());
                    subjectHolder.tvClass.setText(subject.getLesson().getClass_section());

/*
                    subjectHolder.tvStartTime.setText(subjectDateTimeList.get(0).getStartTime());
                    subjectHolder.tvEndTime.setText(subjectDateTimeList.get(0).getEndTime());
                    subjectHolder.tvSubjectArea.setText(listSubject.get(0).getSubject_area() + " "
                            + listSubject.get(0).getCatalog_number());

                    subjectHolder.tvAttendance.setText(subject.getRecorded_time());
                    subjectHolder.tvClass.setText(listSubject.get(0).getClass_section());
                    */
                    String status= subject.getStatus();
                    if(status.equals("0")){

                        subjectHolder.imgAttendance.setImageResource(R.drawable.circle_green_32);
                    }
                    else if(status.equals("-1")){
                        subjectHolder.tvAttendance.setText("");
                        subjectHolder.imgAttendance.setImageResource(R.drawable.circle_red_32);
                    }
                    else{

                        subjectHolder.imgAttendance.setImageResource(R.drawable.circle_orange_32);
                    }

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    static class SubjectHolder {
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvSubjectArea;
        TextView tvClass;
        TextView tvAttendance;
        ImageView imgAttendance;
    }
    static class SeparatorHolder {
        TextView tvWeekDay;
    }

}
