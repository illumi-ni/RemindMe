package com.example.googleintegration;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String notificationId = "notificationId";
    public static final String notificationChannel = "notificationChannel";
    public NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(notificationId, notificationChannel, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.design_default_color_on_primary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getNotificationChannel(String Title, String description) {
        //setting up alarm sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION );
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        mp.start();

        //pending intent for notification tap action
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TaskList.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //pending intent for snooze on action button click
//        Intent intentSnooze = new Intent(getApplicationContext(), AlertReceiver.class);
//        intentSnooze.putExtra("snooze","snooze");
//        PendingIntent pendingIntentSnooze = PendingIntent.getBroadcast(getApplicationContext(),1,
//                intentSnooze, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), notificationId)
                .setContentTitle(Title)
                .setContentText(description)
                .setSmallIcon(R.drawable.logo)
                .setColor(Color.CYAN)
                .setAutoCancel(true)

                //setting snooze button and snooze intent
                .addAction(R.drawable.logo, getString(R.string.snooze), pendingIntent)
                .setOngoing(true)

                //onClick intent
                .setContentIntent(pendingIntent);
    }
}

