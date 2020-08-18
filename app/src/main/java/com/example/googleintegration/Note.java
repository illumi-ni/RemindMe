package com.example.googleintegration;

public class Note {
    private String task;
    private String time;

    public Note(){
        //empty constructor needed
    }

    public Note(String task, String time){
        this.task = task;
        this.time = time;
    }



    public String getTime() {
        return time;
    }

    public String getTask() {
        return task;
    }
}
