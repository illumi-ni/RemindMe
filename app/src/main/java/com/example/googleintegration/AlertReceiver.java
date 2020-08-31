package com.example.googleintegration;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public  class AlertReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        String task = intent.getStringExtra("task");
        String description = intent.getStringExtra("description");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getNotificationChannel(task, description);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
