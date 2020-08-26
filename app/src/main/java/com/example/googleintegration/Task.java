package com.example.googleintegration;

public class Task {
    private String DocumentId;
    private String task;
    private String date;
    private String time;
    private String repeat;
    private String desc;

    public Task(){
        //empty constructor needed
    }

    public Task(String DocumentId, String task, String date, String time, String repeat, String desc){
        this.DocumentId = DocumentId;
        this.task = task;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.desc = desc;
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

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}
