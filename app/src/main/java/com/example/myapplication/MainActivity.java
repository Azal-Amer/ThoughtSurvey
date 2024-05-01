package com.example.myapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;

import android.icu.util.Calendar;
import android.os.Build;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.os.Bundle;

import com.amer.obsididianSurvey.R;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import android.util.Log;

import androidx.work.*;
import androidx.work.WorkManager;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    String documentId = "data.json";
    String originalString = "Hello, World!";
    List<timeGeneration.Time> randomTimes;

    private TextView textViewStatus;
    private static final String TAG = "MainActivity";
    public static Object stringToInt(String str) {
        if (str != null && str.length() == 1 && Character.isDigit(str.charAt(0))) {
            return Integer.parseInt(str);
        } else {
            return null; // or throw an IllegalArgumentException if that's more appropriate
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalDate now = LocalDate.now();
        String seed = now.getDayOfMonth() +
                "" + now.getMonthValue() +
                "" + now.getYear();
        String hashedString = HashUtils.sha1(seed);

        Log.d("HASH","OG String "+seed);
        Log.d("HASH","Hash String "+hashedString);
//        Log.d("HASH","Decimal List"+HashUtils.hexToDecimalDigits(hashedString));

        // Initialize Firestore

        textViewStatus = findViewById(R.id.textViewStatus); // Replace with your actual TextView ID
        textViewStatus.setText("Retrieving...");
        Map<String, Object> data = null;
        SharedPreferences sharedPref = this.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String jsonConfig = sharedPref.getString("ObsidianConfig", null);
        if (jsonConfig != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            data = gson.fromJson(jsonConfig, type);
            // Now you have your data back as a Map
        }



        Calendar currentTime = Calendar.getInstance();
        Calendar nextMidnight = Calendar.getInstance();
        nextMidnight.add(Calendar.DAY_OF_MONTH, 1);
        nextMidnight.set(Calendar.HOUR_OF_DAY, 0);
        nextMidnight.set(Calendar.MINUTE, 0);
        nextMidnight.set(Calendar.SECOND, 0);
        nextMidnight.set(Calendar.MILLISECOND, 0);

        long initialDelay = nextMidnight.getTimeInMillis() - currentTime.getTimeInMillis();

        OneTimeWorkRequest initialWorkRequest = new OneTimeWorkRequest.Builder(SyncWork.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncWork.class, 24, TimeUnit.HOURS)
                .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "SyncWork",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        periodicWorkRequest
                );

        Log.d(TAG, "Value for data: " + data);
        StringBuilder sb = new StringBuilder();
        Object activeDay = data.get("activeDay");
        Object callCount = data.get("callCount");
        String timeRange = (String) data.get("timeRange");

        // Do something with the value
        Log.d(TAG, "Value for 'activeDay': " + activeDay);
        Log.d(TAG, "Value for 'callCount': " + callCount);
        Log.d(TAG, "Value for 'timeRange': " + timeRange);
        List<Map<String, Object>> times = null;

        // Enqueue a one-time work request to run SyncWork
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SyncWork.class).build();
        // Enqueue the work request
        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);



        sb.append("ActiveDay").append(activeDay).append("\n").append("Call Count:").append(callCount).append("\n").append("Time Range: ").append((timeRange)).append("\n");
        textViewStatus.setText(sb);

    }

}