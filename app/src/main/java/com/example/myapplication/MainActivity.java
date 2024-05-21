package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.amer.obsididianSurvey.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    String documentId = "data.json";
    String originalString = "Hello, World!";
    List<timeGeneration.Time> randomTimes;
    TableLayout notificationTable;

    public static TextView textViewStatus;
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
        notificationTable = findViewById(R.id.notificationTable);


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

//        the below was written because we wanted to make sure that we were properly saving the firestore config
//        for situations of no internet access
        if (jsonConfig != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            data = gson.fromJson(jsonConfig, type);
            // Now you have your data back as a Map
        }

        NotificationHelper notificationHelper = new NotificationHelper(notificationTable);
        


//        This below logic makes our periodicWorkRequest is enabled at the correct time
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
                .setInputData(new Data.Builder()
                        .putString("notificationTableId", String.valueOf(notificationTable.getId()))
                        .build())
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncWork.class, 24, TimeUnit.HOURS)
                .setInputData(new Data.Builder()
                    .putString("notificationTableId", String.valueOf(notificationTable.getId()))
                    .build())
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
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SyncWork.class)
                .setInputData(new Data.Builder()
                .putString("notificationTableId", String.valueOf(notificationTable.getId()))
                .build()).build();
        // Enqueue the work request
        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);


//making the visuals of the app
        sb.append("ActiveDay").append(activeDay).append("\n").append("Call Count:").append(callCount).append("\n").append("Time Range: ").append((timeRange)).append("\n");
        textViewStatus.setText(sb);


//        testing debug table

        TableRow row = new TableRow(this);

        TextView hourTextView = new TextView(this);
        hourTextView.setText(String.valueOf(1));
        hourTextView.setPadding(8, 8, 8, 8);
        row.addView(hourTextView);

        TextView minuteTextView = new TextView(this);
        minuteTextView.setText(String.valueOf(10));
        minuteTextView.setPadding(8, 8, 8, 8);
        row.addView(minuteTextView);
        TextView uriTextView = new TextView(this);
        uriTextView.setText("google.com");
        uriTextView.setPadding(8, 8, 8, 8);
        row.addView(uriTextView);
        this.notificationTable.addView(row);

    }

}