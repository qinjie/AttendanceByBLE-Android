package edu.np.ece.attendancetakingapplication.OrmLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 21-Mar-17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "SubjectDB.sqlite";

    // any time you make changes to your database objects, you may have to increase the database versdion
    private static final int DATABASE_VERSION = 4;

    // the DAO object we use to access the SimpleData table
    private Dao<Subject, Integer> SubjectDao = null;
    private Dao<SubjectDateTime, Integer> SubjectDateTimeDao = null;
    private Dao<Student, Integer> students = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Subject.class);
            TableUtils.createTable(connectionSource, SubjectDateTime.class);
            TableUtils.createTable(connectionSource, Student.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>();
            /*switch(oldVersion)
            {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");


            }*/
            if(oldVersion<5){
                try {
                    SubjectDao.executeRaw("ALTER TABLE `Subject` ADD COLUMN teacher_office STRING;");
                    SubjectDao.executeRaw("ALTER TABLE `Subject` ADD COLUMN teacher_phone STRING;");
                    SubjectDao.executeRaw("ALTER TABLE `Subject` ADD COLUMN class_section STRING;");
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }

    }

    public Dao<Subject, Integer> getSubjectDao() {
        if (null == SubjectDao) {
            try {
                SubjectDao = getDao(Subject.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return SubjectDao;
    }

    public Dao<SubjectDateTime, Integer> getSubjectDateTimeDao() {
        if (null == SubjectDateTimeDao) {
            try {
                SubjectDateTimeDao = getDao(SubjectDateTime.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return SubjectDateTimeDao;
    }


    public Dao<Student, Integer> getStudentDao() {
        if (null == students) {
            try {
                students = getDao(Student.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return students;
    }


}
