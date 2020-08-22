package com.example.googleintegration.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ProjectNote {
    private String projectname;
    private String projectdesc;
    private @ServerTimestamp Date timestamp;
    private String project_id;
    private String user_id;

    public ProjectNote(String projectname, String projectdesc, Date timestamp, String project_id, String user_id) {
        this.projectname = projectname;
        this.projectdesc = projectdesc;
        this.timestamp = timestamp;
        this.project_id = project_id;
        this.user_id = user_id;
    }

    public ProjectNote(){

    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectdesc() {
        return projectdesc;
    }

    public void setProjectdesc(String projectdesc) {
        this.projectdesc = projectdesc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
