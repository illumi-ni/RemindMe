package com.example.googleintegration;

public class Task {
    private String task;
    private String date;
    private String time;
    private String repeat;
    private String desc;

    public Task(){
        //empty constructor needed
    }

    public Task(String task, String date){
        this.task = task;
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getDesc() {
        return desc;
    }
}
