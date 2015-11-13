package com.softopers.asaedr.gcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.gson.Gson;
import com.softopers.asaedr.R;
import com.softopers.asaedr.model.notification;
import com.softopers.asaedr.ui.user.ReportingActivity;

import java.util.Random;

public class GcmBroadcastReceiver extends BroadcastReceiver {

    public final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    private static void generateNotification(Context context, notification message) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        Notification notification = new Notification();
        Intent notificationIntent = new Intent(context, ReportingActivity.class);
        // set intent so it does not start a new activity

        notificationIntent.putExtra("comment", true);
        notificationIntent.putExtra("DayStatusId", message.getDayStatusId());
        notificationIntent.putExtra("date", message.getDate());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(message.getAdminName() + " commented on " + message.getDate());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(intent);
        builder.setAutoCancel(true);
        builder.setOngoing(true);

//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notification = builder.getNotification();
        notificationManager.notify(0, notification);

        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vibrator vibrator;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Push Received.");
        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            Log.d(TAG, String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }
        Gson gson = new Gson();
        notification notification = gson.fromJson(intent.getExtras().getString("notification"), notification.class);
        generateNotification(context, notification);
    }

}