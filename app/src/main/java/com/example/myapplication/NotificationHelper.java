package com.example.myapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import android.util.Log;

public class NotificationHelper {
    private Context context;
    public static final String TAG = "Notification Helper";
    public NotificationHelper(Context context) {
        this.context = context;

    }

    public void createNotificationTunnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ObsidianNotif Channel";
            String description = "description for notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyObsidian", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setTimeAlarm(timeGeneration.Time time, String body, int i) {
        Log.d(TAG, "setting custom alarm");

        createNotificationTunnel();
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.putExtra("notification_body", body);
        URIGenerator uriGenerator = new URIGenerator();
        String uri = uriGenerator.Generator();
        intent.putExtra("URI", uri + i);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int hour = (int) (double) time.hour;
        int minute = (int) (double) time.minute;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        if (Calendar.getInstance().before(calendar)) {
            long scheduledTimeMillis = calendar.getTimeInMillis();
            Log.d(TAG, "Scheduled Time" + scheduledTimeMillis);
            Log.d(TAG, "set notification for " + hour + ":" + minute);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduledTimeMillis, pendingIntent);
        }

    }
}