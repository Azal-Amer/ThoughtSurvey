package com.example.myapplication;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amer.obsididianSurvey.R;

import androidx.annotation.NonNull;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationBody = intent.getStringExtra("notification_body");
        String url = intent.getStringExtra("URI");
        // Create an intent that opens a browser to your URL.
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(url));
        // Wrap the intent in a PendingIntent.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openUrlIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyObsidian")
                .setSmallIcon(androidx.transition.R.drawable.abc_ic_arrow_drop_right_black_24dp)
                .setContentTitle("Obsidian")
                .setContentText(notificationBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(200, builder.build());
    }
}
