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
    private String name;

    @ForeignCollectionField
    private ForeignCollection<Time> subject_datetime;

    public Subject() {
    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject_datetime(ForeignCollection<Time> subject_datetime) {
        this.subject_datetime = subject_datetime;
    }

    public List<Time> getSubject_Datetime(){
        ArrayList<Time> itemList = new ArrayList<Time>();
        for(Time aTime : subject_datetime){
            itemList.add(aTime);
        }
        return itemList;
    }
}
