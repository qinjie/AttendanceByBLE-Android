package edu.np.ece.attendancetakingapplication.OrmLite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hoanglong on 28-Mar-17.
 */
@DatabaseTable(tableName="student")
public class Student {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String student_id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String card;

    @DatabaseField
    private String beacon_id;

    @DatabaseField
    private String beacon_major;

    @DatabaseField
    private String beacon_minor;

    @DatabaseField(foreign=true,foreignAutoRefresh=true)
    private Subject subject;

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    public String getBeacon_major() {
        return beacon_major;
    }

    public void setBeacon_major(String beacon_major) {
        this.beacon_major = beacon_major;
    }

    public String getBeacon_minor() {
        return beacon_minor;
    }

    public void setBeacon_minor(String beacon_minor) {
        this.beacon_minor = beacon_minor;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}


