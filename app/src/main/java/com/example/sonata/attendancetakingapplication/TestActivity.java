package com.example.sonata.attendancetakingapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sonata.attendancetakingapplication.OrmLite.DatabaseManager;
import com.example.sonata.attendancetakingapplication.OrmLite.Subject;
import com.example.sonata.attendancetakingapplication.OrmLite.SubjectDateTime;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView test = (TextView) findViewById(R.id.tvTest);
//        final List<Subject> wishLists = DatabaseManager.getInstance().getSubjectWithSubjectArea("IS PDA");
        final List<Subject> wishLists = DatabaseManager.getInstance().getAllSubjects();

        String text = "";
        for (Subject tmp : wishLists){
            for (SubjectDateTime tmp2 : tmp.getSubject_Datetime()){
                text += tmp.getSubject_area() + " | " + tmp.getCatalog_number() +" | "+ tmp.getUuid()+" | "+tmp.getLesson_id() +" | "+tmp.getLocation() +" |xxx| "
                +tmp2.getLesson_date_id()+" | "+ tmp2.getLesson_date()+" | "+ tmp2.getStartTime()+" | "+ tmp2.getEndTime()+" ";
            }

        }

        test.setText(text);
    }
}
