package edu.np.ece.attendancetakingapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 07-Mar-17.
 */

public class LessonBeacon {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
