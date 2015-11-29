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
import com.softopers.asaedr.ui.BaseActivity;
import com.softopers.asaedr.ui.MessageListActivity;
import com.softopers.asaedr.ui.user.ReportingActivity;

public class GcmBroadcastReceiver extends BroadcastReceiver {

    public final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    private static void generateNotification(Context context, notification message) {

//        int notificationId = new Random().nextInt();

        if (message.getNotificationType().equalsIgnoreCase("Message")) {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification();

            Notification.Builder builder = new Notification.Builder(context);

            Intent notificationIntent = new Intent(context, MessageListActivity.class);
            // set intent so it does not start a new activity
            notificationIntent.putExtra("message", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent =
                    PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.setContentTitle(context.getString(R.string.app_name));
            builder.setContentText(message.getAdminName() + " messaged");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(intent);
            builder.setAutoCancel(true);
            builder.setOngoing(true);
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

            notification = builder.getNotification();
            notificationManager.notify(0, notification);
        } else if (message.getNotificationType().equalsIgnoreCase("EndDay")) {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification();

            Notification.Builder builder = new Notification.Builder(context);

            Intent notificationIntent = new Intent(context, BaseActivity.class);
            // set intent so it does not start a new activity

            notificationIntent.putExtra("endday", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent =
                    PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.setContentTitle(context.getString(R.string.app_name));
            builder.setContentText(message.getMessageContent());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(intent);
            builder.setAutoCancel(true);
            builder.setOngoing(true);
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

            notification = builder.getNotification();
            notificationManager.notify(2, notification);
        } else {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification();

            Notification.Builder builder = new Notification.Builder(context);

            Intent notificationIntent = new Intent(context, ReportingActivity.class);
            // set intent so it does not start a new activity

            notificationIntent.putExtra("comment", true);
            notificationIntent.putExtra("DayStatusId", message.getDayStatusId());
            notificationIntent.putExtra("date", message.getDate());
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent =
                    PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.setContentTitle(context.getString(R.string.app_name));
            builder.setContentText(message.getAdminName() + " commented on " + message.getDate());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(intent);
            builder.setAutoCancel(true);
            builder.setOngoing(true);
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

            notification = builder.getNotification();
            notificationManager.notify(1, notification);
        }

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