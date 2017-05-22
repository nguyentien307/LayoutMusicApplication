package com.example.tiennguyen.layoutmusicapplication.utility;

/**
 * Created by Quyen Hua on 5/19/2017.
 */

public class Utility {
    public static String convertDuration(long duration){
        long minutes = (duration/1000)/60;
        long seconds = (duration/1000)%60;
        String converted = String.format("%d:%02d", minutes, seconds);
        return converted;
    }
}
