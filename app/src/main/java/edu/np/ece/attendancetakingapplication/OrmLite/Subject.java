package edu.np.ece.attendancetakingapplication.OrmLite;

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

    @DatabaseField(columnName = "lesson_id")
    private String lesson_id;

    @DatabaseField
    private String subject_area;

    @DatabaseField
    private String catalog_number;

    @DatabaseField
    private String location;

    @DatabaseField
    private String uuid;

    @DatabaseField
    private String teacher_id;

    @DatabaseField
    private String teacher_name;

    @DatabaseField
    private String teacher_acad;

    @DatabaseField
    private String teacher_email;

    @DatabaseField(columnName = "teacher_office")
    private  String teacher_office;

    @DatabaseField(columnName = "teacher_phone")
    private  String teacher_phone;

    @DatabaseField(columnName = "class_section")
    private String class_section;

    @DatabaseField
    private String teacher_major;

    @DatabaseField
    private String teacher_minor;


    @ForeignCollectionField
    private ForeignCollection<SubjectDateTime> subject_datetime;

    @ForeignCollectionField
    private ForeignCollection<Student> student;

    public Subject() {
    }

    public Subject(int id, String lesson_id, String subject_area, String catalog_number, String location, String uuid, ForeignCollection<SubjectDateTime> subject_datetime, ForeignCollection<Student> student_list) {
        this.id = id;
        this.lesson_id = lesson_id;
        this.subject_area = subject_area;
        this.catalog_number = catalog_number;
        this.location = location;
        this.uuid = uuid;
        this.subject_datetime = subject_datetime;
        this.student = student_list;
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


    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }
    public void  setTeacher_office(String teacher_office){this.teacher_office=teacher_office;}

    public String  getTeacher_office(){
        return teacher_office;
    }


    public void  setTeacher_phone(String teacher_phone){this.teacher_phone=teacher_phone;}

    public String  getTeacher_phone(){
        return teacher_phone;
    }

    public void setClass_section(String class_section){this.class_section=class_section;}
    public String getClass_section(){
        return class_section;
    }


    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_acad() {
        return teacher_acad;
    }

    public void setTeacher_acad(String teacher_acad) {
        this.teacher_acad = teacher_acad;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }


    public String getTeacher_major() {
        return teacher_major;
    }

    public void setTeacher_major(String teacher_major) {
        this.teacher_major = teacher_major;
    }

    public String getTeacher_minor() {
        return teacher_minor;
    }

    public void setTeacher_minor(String teacher_minor) {
        this.teacher_minor = teacher_minor;
    }

    public List<SubjectDateTime> getSubject_Datetime(){
        ArrayList<SubjectDateTime> itemList = new ArrayList<SubjectDateTime>();
        for(SubjectDateTime aSubjectDateTime : subject_datetime){
            itemList.add(aSubjectDateTime);
        }
        return itemList;
    }

    public List<Student> getStudent_list(){
        ArrayList<Student> itemList = new ArrayList<Student>();
        for(Student aStudent : student){
            itemList.add(aStudent);
        }
        return itemList;
    }
}
