package com.example.myapplication;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;

public class RNG {
    private String seed;
    private List<Float> randomNumbers;
    private float nextRandom;
    private int nextRandomIndex;
    public RNG(String seed) {
        this.seed = seed;
        // Assuming hexToDecimal is a static method that converts the hex string to a list of integers
        String hashedString = HashUtils.sha1(seed);
        Log.d("random numbers in the thing","hashed string is "+ hashedString);
        this.randomNumbers = HashUtils.hexToDecimalDigits(hashedString);
        Log.d("explode","explode first"+getNumbers());
        this.nextRandomIndex = 0;
        this.nextRandom = this.randomNumbers.get(this.nextRandomIndex);
        for (int i = 0; i < this.randomNumbers.size(); i++) {
            Log.d("random numbers in the thing", this.randomNumbers.get(i) + (i < this.randomNumbers.size() - 1 ? ", " : ""));
        }
        Log.d("random numbers in the thing","length is "+ this.randomNumbers.size());

    }
    public float next() {
        Log.d("RANDOM TIME","got nextRandom");
        Log.d("RANDOM TIME","RNG Used"+this.nextRandom);
        float rawNext = this.nextRandom;
        this.nextRandomIndex += 1;
        if (this.nextRandomIndex >= this.randomNumbers.size()) {
            // Handle the case where you're at the end of the list
            // This could throw an exception, return a default value, or reset the index
            // For this example, let's reset the index
            this.nextRandomIndex = 0;
        }
        this.nextRandom = this.randomNumbers.get(this.nextRandomIndex);
        Log.d("RANDOM TIME","RNG Used"+this.nextRandom);
        Log.d("RANDOM TIME","RNGs"+this.randomNumbers);
        return rawNext;
    }
    // Placeholder for the hexToDecimal method

    private static List<Integer> hexToDecimal(String hexSeed) {
        // Your implementation of converting hex string to a list of decimals
        return new ArrayList<>(); // Replace this with the actual conversion logic
    }
    // Getters and setters (if needed)
    public String getNumbers() {

        return randomNumbers.toString();
    }
    public List<Float> getRandomNumbers() {
        return randomNumbers;
    }

    public float getNextRandom() {
        return nextRandom;
    }

    public int getNextRandomIndex() {
        return nextRandomIndex;
    }
};

