package edu.np.ece.attendancetakingapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.np.ece.attendancetakingapplication.Model.AttendanceResult;
import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.Lesson;
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

            String lessonId =subject.getLesson_date().getLesson_id();
            List<Subject> listSubject = DatabaseManager.getInstance().QueryBuilder("lesson_id",lessonId);
            List<SubjectDateTime> subjectDateTimeList=listSubject.get(0).getSubject_Datetime();
            switch (itemType) {
                //Put data into layout for display weekday
                case Preferences.LIST_ITEM_TYPE_1:

                    separatorHolder.tvWeekDay.setText(subject.getLesson_date().getLdate()+" ("+subject.getLesson_date().getDate()+")");

                    break;

                //Put data into layout for display Subject
                case Preferences.LIST_ITEM_TYPE_2:
                    subjectHolder.tvStartTime.setText(subjectDateTimeList.get(0).getStartTime());
                    subjectHolder.tvEndTime.setText(subjectDateTimeList.get(0).getEndTime());
                    subjectHolder.tvSubjectArea.setText(listSubject.get(0).getSubject_area() + " "
                            + listSubject.get(0).getCatalog_number());

                    subjectHolder.tvAttendance.setText(subject.getRecorded_time());
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
    }
    static class SeparatorHolder {
        TextView tvWeekDay;
    }

}
