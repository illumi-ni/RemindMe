package com.example.googleintegration;

public class ProjectTaskList {
    private String taskDesc1;
    private String taskDesc2;

    public ProjectTaskList() {
        //keep it empty
    }


    public ProjectTaskList(String taskDesc1, String taskDesc2) {
        this.taskDesc1 = taskDesc1;
        this.taskDesc2 = taskDesc2;
    }


    public String getTaskDesc1() {
        return taskDesc1;
    }

    public String getTaskDesc2() {
        return taskDesc2;
    }
}

