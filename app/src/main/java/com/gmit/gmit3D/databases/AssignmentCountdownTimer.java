package com.gmit.gmit3D.databases;

import android.text.format.Time;

/**
 * Created by Aaron on 12/01/2016.
 */
public class AssignmentCountdownTimer {

    private long intervalMillis;

    public AssignmentCountdownTimer(int second, int minute, int hour, int monthDay, int month, int year){
        Time futureTime = new Time();

        //set time to due date
        futureTime.set(second, minute, hour, monthDay, month, year);
        futureTime.normalize(true);
        long futureMillis = futureTime.toMillis(true);

        //sset date to current date
        Time nowTime = new Time();

        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        // Subtract current milliseconds time from future milliseconds time to retrieve interval
        intervalMillis = futureMillis - nowMillis;
    }

    public long getIntervalMillis(){
        return intervalMillis;
    }
}
