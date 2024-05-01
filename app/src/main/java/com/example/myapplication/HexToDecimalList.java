package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HexToDecimalList {
//    yay this works good

    public static List<Float> hexStringToDecimalList(String hexString) {
        List<Float> decimalValues = new ArrayList<>();
        for (int i = 0; i < hexString.length(); i++) {
            // Extract each character as a substring
            String hexChar = hexString.substring(i, i + 1);

            // Parse the hex character as a base-16 integer and add to list
            float decimalValue = ((float) Integer.parseInt(hexChar, 16))/16;
            decimalValues.add(decimalValue);
        }
        Log.d("explode","size" + decimalValues.size());
        return decimalValues;
    }

    public static void main(String[] args) {
        String hexString = "1A3F"; // Example hex string
        List<Float> decimalValues = hexStringToDecimalList(hexString);

        System.out.println("Decimal values: " + decimalValues);
    }
}