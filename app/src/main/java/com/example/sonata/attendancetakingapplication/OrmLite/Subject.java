package com.example.sonata.attendancetakingapplication.OrmLite;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 20-Mar-17.
 */

@DatabaseTable
public class Subject extends OrmLiteBaseActivity {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String lesson_id;

    @DatabaseField
    private String subject_area;

    @DatabaseField
    private String catalog_number;

    @DatabaseField
    private String location;

    @DatabaseField
    private String uuid;

    @ForeignCollectionField
    private ForeignCollection<SubjectDateTime> subject_datetime;

    public Subject() {
    }

    public Subject(int id, String subject_area, String catalog_number, ForeignCollection<SubjectDateTime> subject_datetime) {
        this.id = id;
        this.subject_area = subject_area;
        this.catalog_number = catalog_number;
        this.subject_datetime = subject_datetime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject_area() {
        return subject_area;
    }

    public void setSubject_area(String subject_area) {
        this.subject_area = subject_area;
    }

    public String getCatalog_number() {
        return catalog_number;
    }

    public void setCatalog_number(String catalog_number) {
        this.catalog_number = catalog_number;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSubject_datetime(ForeignCollection<SubjectDateTime> subject_datetime) {
        this.subject_datetime = subject_datetime;
    }

    public List<SubjectDateTime> getSubject_Datetime(){
        ArrayList<SubjectDateTime> itemList = new ArrayList<SubjectDateTime>();
        for(SubjectDateTime aSubjectDateTime : subject_datetime){
            itemList.add(aSubjectDateTime);
        }
        return itemList;
    }
}
