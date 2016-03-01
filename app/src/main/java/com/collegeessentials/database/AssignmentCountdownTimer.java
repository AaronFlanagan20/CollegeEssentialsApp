package com.collegeessentials.database;

import android.text.format.Time;

/**
 * This class gets the current time in milliseconds, normalizes it and passes it back to our Assignment class.
 *
 * @version 1.0
 * @see com.collegeessentials.main.Assignment
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
