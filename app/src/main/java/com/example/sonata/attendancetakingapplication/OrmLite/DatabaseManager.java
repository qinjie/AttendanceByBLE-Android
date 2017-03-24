package com.example.sonata.attendancetakingapplication.OrmLite;

import android.content.Context;
import android.database.SQLException;

import java.util.List;

/**
 * Created by hoanglong on 21-Mar-17.
 */

public class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public List<Subject> getAllSubjects() {
        List<Subject> Subjects = null;
        try {
            Subjects = getHelper().getSubjectDao().queryForAll();
        }
         catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return Subjects;
    }


    public void addSubject(Subject l) {
        try {
            getHelper().getSubjectDao().create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public SubjectDateTime newSubjectDateTimeItem() {
        SubjectDateTime subjectDateTimeItem = new SubjectDateTime();
        try {
            getHelper().getSubjectDateTimeDao().create(subjectDateTimeItem);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return subjectDateTimeItem;
    }

    public void updateTimeItem(SubjectDateTime item) {
        try {
            getHelper().getSubjectDateTimeDao().update(item);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(Subject subject) {
        try {
            getHelper().getSubjectDao().delete(subject);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllSubject() {
        try {
            getHelper().getSubjectDao().delete(getAllSubjects());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Subject> getSubjectWithSubjectArea(String wishListId) {
        List<Subject> wishList = null;
        try {
            wishList = getHelper().getSubjectDao().queryForEq("subject_area", wishListId);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


}
