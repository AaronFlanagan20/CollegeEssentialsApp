package com.collegeessentials.database;

import android.text.format.Time;

/**
 * Gets current time in milliseconds
 */
public class AssignmentCountdownTimer {

    private long intervalMillis;

    public AssignmentCountdownTimer(int second, int minute, int hour, int monthDay, int month, int year){
        Time futureTime = new Time();

        //set time to due date
        futureTime.set(second, minute, hour, monthDay, month, year);
        futureTime.normalize(true);
        long futureMillis = futureTime.toMillis(true);

        intervalMillis = futureMillis;
    }

    public long getIntervalMillis(){
        return intervalMillis;
    }
}
