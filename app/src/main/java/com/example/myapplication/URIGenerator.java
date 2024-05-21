package com.example.myapplication;

import android.util.Log;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class URIGenerator {
    private String TAG = "URi Generator";
    Object[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    private String format;

    public URIGenerator(String userInput) {
        if(userInput.length()<1){
            Log.d(TAG,userInput);
            userInput="Main Obsidian Vault";
        }

        format = "obsidian://open?vault="+userInput.replace(" ","%20")+"&file=surveys%2F";
        Log.d(TAG,"URI: "+ format);
    }

    public String Generator(){
        LocalDate now = LocalDate.now();
        int day = now.getDayOfMonth();
        int year = now.getYear();
        int month =  now.get(ChronoField.MONTH_OF_YEAR);
        String urlFormat =this.format+ 2024+"%2F"
                +months[month-1]+"%2F"+day+"%2FSurvey%20";
        return urlFormat;
    }
}
