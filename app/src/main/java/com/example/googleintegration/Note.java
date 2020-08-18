package com.example.googleintegration;

public class Note {
    private String task;
    private String date;

    public Note(){
        //empty constructor needed
    }

    public Note(String task, String date){
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
