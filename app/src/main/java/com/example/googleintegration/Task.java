package com.example.googleintegration;

public class Task {
    private String task;
    private String date;

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
}
