package com.example.myapplication;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class timeGeneration {
    static class Time {
        int hour;
        int minute;

        Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public String toString() {
            return String.format("%02d:%02d", hour, minute);
        }
    }
    public static List<Time> randomTimeRange(List<Time> times, int frequency) {
        Log.d("RANDOM TIME","got here4");
        // Split the range into start and end times


        // Parse the start and end times
        int startHour = (int) (double) times.get(0).hour;

        int startMinute = (int) (double)times.get(0).minute;
        Log.d("RANDOM TIME",startHour + ":"+startMinute);
        int endHour = (int) (double)times.get(1).hour;
        int endMinute = (int) (double)times.get(1).minute;
        Log.d("RANDOM TIME",endHour + ":.."+endMinute);
        // Create a list to hold the random times
        List<Time> randomTimes = new ArrayList<>();

        // Initialize random number generator
        LocalDate now = LocalDate.now();
        String seed = now.getDayOfMonth() +
                "" + now.getMonthValue() +
                "" + now.getYear();
        RNG rand = new RNG(seed);
        Log.d("explode","explode seed: "+seed);
        Log.d("explode","explode time"+rand.getNumbers());

        for (int i = 0; i < frequency; i++) {
            float randomFloat = rand.next();
            int randomHour = (int) (Math.floor(
                    randomFloat*(endHour-startHour)) + startHour);
            Log.d("RANDOM TIME GENERATION","Hour:"+startHour+"-"+endHour+"with float of : "+randomFloat);
            Log.d("RANDOM TIME GENERATION","Hour:"+randomHour);
            float minuteFloat = rand.next();
            int randomMinute = (int) Math.floor(minuteFloat*60);
            Log.d("RANDOM TIME GENERATION","Minute:"+randomMinute + "with float of :" + minuteFloat);

            // Adjust random minute and hour if the time is out of range
            randomTimes.add(new Time(randomHour, randomMinute));
        }
        return randomTimes;
    }

    public static void main(String[] args) {
//        Map<String, Object> test1 = new HashMap<>();
//        test1.put("hour", 1L); // Note: Firestore returns Long values
//        test1.put("minute", 57L);
//        Map<String, Object> test2 = new HashMap<>();
//        test2.put("hour", 1L); // Note: Firestore returns Long values
//        test2.put("minute", 58L);
//        List[] hi = new List[0];
//
//
//        Object[] testTimes= new Object[]{test1,test2};
//        List<Time> times = randomTimeRange(testTimes, 10);
//        for (Time time : times) {
//            System.out.println(time);
//        }
    }

}
