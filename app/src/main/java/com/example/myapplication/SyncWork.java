package com.example.myapplication;

import static com.example.myapplication.MainActivity.stringToInt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class SyncWork extends Worker {
    public static final String TAG = "MyPeriodicWork";
    String documentId = "data.json";
    String callCount;
    String timeRange;
    public SyncWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }



    private interface OnDataLoadedCallback {
        void onDataLoaded();
    }
    public Result doWork() {
        Log.d(TAG,"doing scheduled work");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        loadDB(db,new OnDataLoadedCallback() {
            @Override
                    public void onDataLoaded() {
                        Log.d(TAG, "got firestore1, with callCount " + callCount + " and time range " + timeRange);
                        timeGeneration();
                        updateDB(db);
                    }
                }
                );
//        loadDB modifies callCount and timeRange
        return Result.retry();



    }

    //    below we will open the firestore config,and update the active day, we will also read the

    private Result loadDB(FirebaseFirestore db, final OnDataLoadedCallback callback){
//        final String[] callCount = new String[1];
//        final String[] timeRange = new String[1];
        db.collection("obsidianConfig").document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Map<String, Object> data = document.getData();
                                // Access a value with a known key
                                if (data != null) {
                                    // Log success
                                    List<Map<String, Object>> times = (List<Map<String, Object>>) document.get("times");
                                    Log.d(TAG,"timesWorkSync" + times);
                                    Log.d(TAG,"callCount " + data.get("callCount"));
                                    callCount = (String) data.get("callCount");
                                    timeRange = (String) data.get("timeRange");
                                    callback.onDataLoaded();
// Now you can iterate over the list or access its elements by index
                                    if (times != null) {
                                        Gson gson = new Gson();
                                        String jsonConfig = gson.toJson(data);
                                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("ObsidianConfig", jsonConfig);
                                        editor.apply();
                                        Log.d(TAG, "Configuration saved to Shared Preferences.");
                                    }
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                callback.onDataLoaded();
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                            callback.onDataLoaded();
                        }
                    }
                });
        Log.d(TAG, "doWork: Work has been done");
        return Result.retry();
    }

    public Result updateDB(FirebaseFirestore db){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        db.collection("obsidianConfig").document(documentId)
                .update("activeDay", currentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "activeDay updated successfully");
                        } else {
                            Log.w(TAG, "Error updating activeDay", task.getException());
                        }
                    }
                });
        return Result.success();
//        the purpose of this one is that when the activity is run, which is supposed to be
//        midnight, will update the active day, and if I'm feeling quirky, will also update the times.
    }

    private void timeGeneration(){
        List<timeGeneration.Time> randomTimes = null;
        String localTAG = "Time Generator";
        try{
//            below just defines key variables required for us to create the random times
            Log.d(localTAG,"got here2");
            timeGeneration RandomTimeGenerator= new timeGeneration();
            Log.d(localTAG,"got here3");

//            above just reads callCount, we can just take that as an input

//          parse timeRange
            Log.d(localTAG,"test"+timeRange);

//            time range string parsing, the format  HH:MM-HH:MM
            String[] parts = timeRange.split("-");
            String[] beggining = parts[0].split(":");
            String[] end = parts[1].split(":");

//            declaring the time as a Time object as specified in timeGeneration, for easier manipulation later
            timeGeneration.Time lowerrange = new timeGeneration.Time(Integer.parseInt(beggining[0]),Integer.parseInt(beggining[1]));
            timeGeneration.Time upperrange = new timeGeneration.Time(Integer.parseInt(end[0]),Integer.parseInt(end[1]));
            Log.d(localTAG,"lower range"+lowerrange.toString());
            Log.d(localTAG,"lower range"+upperrange.toString());

            List<timeGeneration.Time> typedTimeRange = new ArrayList<>();
            typedTimeRange.add(lowerrange);
            typedTimeRange.add(upperrange);
//

            randomTimes = timeGeneration.randomTimeRange(typedTimeRange, (Integer) stringToInt(callCount));


            for (int i = 0;i<randomTimes.size();i++){
                timeGeneration.Time time = randomTimes.get(i);
                String timeString = time.toString();
                Log.d("RANDOM TIME",timeString);
            }

        }
        catch (Exception exception){
            Log.d(TAG, String.valueOf(exception));
            Log.e("Time Generator", String.valueOf(exception));
        }


        Context applicationContext = getApplicationContext();
        if (applicationContext instanceof MainActivity) {
            int notificationTableId = Integer.parseInt(getInputData().getString("notificationTableId"));
            TableLayout notificationTable = ((MainActivity) applicationContext).findViewById(notificationTableId);
            NotificationHelper notificationHelper = new NotificationHelper(notificationTable);
            notificationHelper.createNotificationTunnel();

            // Sort randomTimes by time
            for (int i = 0; i < randomTimes.size(); i++) {
                notificationHelper.setTimeAlarm(randomTimes.get(i), "Azal's scheduled test for " + i, i);
            }
        } else {
            Log.e(TAG, "Application context is not an instance of MainActivity");
        }





//        the purpose of this will, from top bottom, schedule the random times
    }




}


