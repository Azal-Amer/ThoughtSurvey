package com.example.myapplication;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;

public class URIGenerator {
    Object[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    public String Generator(){
        LocalDate now = LocalDate.now();
        int day = now.getDayOfMonth();
        int year = now.getYear();
        int month =  now.get(ChronoField.MONTH_OF_YEAR);
        String urlFormat = "obsidian://open?vault=Obsidian%20Vault&file=surveys%2F"+2024+"%2F"
                +months[month-1]+"%2F"+day+"%2FSurvey%20";
        return urlFormat;
    }
}
