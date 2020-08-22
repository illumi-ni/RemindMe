package com.example.googleintegration;

public class ProjectList {
    private String projectname;
    private String projectdesc;

    public ProjectList(){

    }

    public ProjectList(String projectname, String projectdesc) {
        this.projectname = projectname;
        this.projectdesc = projectdesc;
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
}
