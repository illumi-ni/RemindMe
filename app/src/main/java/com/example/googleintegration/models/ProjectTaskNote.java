package com.example.googleintegration.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ProjectTaskNote {
    private String TaskDesc;
    private String TaskDesc2;
    private @ServerTimestamp Date timestamp;
    private String task_id;
    private String userId;

}
