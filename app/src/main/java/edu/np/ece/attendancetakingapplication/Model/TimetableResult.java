package edu.np.ece.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sonata on 11/14/2016.
 */

public class TimetableResult {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lesson_id")
    @Expose
    private String lesson_id;

    @SerializedName("student_id")
    @Expose
    private String student_id;

    @SerializedName("lesson")
    @Expose
    private Lesson lesson;

    @SerializedName("lesson_date")
    @Expose
    private LessonDate lesson_date;

    @SerializedName("lecturers")
    @Expose
    private Lecturer lecturers;

    @SerializedName("venue")
    @Expose
    private Venue venue;

    @SerializedName("beaconLesson")
    @Expose
    private LessonBeacon lessonBeacon;


    @SerializedName("students")
    @Expose
    private List<StudentInfo> studentList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonDate getLesson_date() {
        return lesson_date;
    }

    public void setLesson_date(LessonDate lesson_date) {
        this.lesson_date = lesson_date;
    }

    public Lecturer getLecturers() {
        return lecturers;
    }

    public void setLecturers(Lecturer lecturers) {
        this.lecturers = lecturers;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public LessonBeacon getLessonBeacon() {
        return lessonBeacon;
    }

    public void setLessonBeacon(LessonBeacon lessonBeacon) {
        this.lessonBeacon = lessonBeacon;
    }

    public List<StudentInfo> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentInfo> studentList) {
        this.studentList = studentList;
    }
}
