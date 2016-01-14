package com.gmit.gmit3D.databases;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gmit.gmit3D.main.R;

import java.sql.SQLException;

public class ShowAssignmentDates extends AppCompatActivity {

    private TextView view1, view2;
    private ApplicationDatabase ad;
    private AssignmentCountdownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assignment_dates);

        ad = new ApplicationDatabase(getBaseContext());//get database object
        ad.createDatabase();

        view1 = (TextView) findViewById(R.id.moduleText);
        view1.setText("Due");
        try {
            Cursor cursor = ad.returnAssignmentData();//cursor returns everythig from result set
            if(cursor.moveToFirst()){
                String name = cursor.getString(0);
                String date = cursor.getString(1);

                //begin split
                String[] seperate = date.split("/");
                String day = seperate[0];
                String month = seperate[1];
                date = seperate[2];//date will equal year SPACE time

                //must split again because 2016 13:00
                seperate = date.split(" ");//split space between year and time of day
                String year = seperate[0];
                String time = seperate[1];

                //begin spliting time
                seperate = time.split(":");
                String hour = seperate[0];//eg 12, 1, 16
                String minute = seperate[1];

                //get integer values of strings
                int minuteInt = Integer.parseInt(minute);
                int hourInt = Integer.parseInt(hour);
                int yearInt = Integer.parseInt(year);
                int monthInt = Integer.parseInt(month);
                int dayInt = Integer.parseInt(day);

                ct = new AssignmentCountdownTimer(0, minuteInt, hourInt, dayInt, monthInt, yearInt);
                CountDownTimer countDownTimer = new CountDownTimer(ct.getIntervalMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int days = (int) ((millisUntilFinished / 1000) / 86400);
                        int hours = (int) (((millisUntilFinished / 1000)
                                - (days * 86400)) / 3600);
                        int minutes = (int) (((millisUntilFinished / 1000)
                                - (days * 86400) - (hours * 3600)) / 60);
                        int seconds = (int) ((millisUntilFinished / 1000) % 60);

                        String countdown = String.format("%02dd %02dh %02dm %02ds", days,
                                hours, minutes, seconds);
                        view1.setText(countdown);
                    }

                    @Override
                    public void onFinish() {
                        view1.setText("Time is up, Assignment due date is upon us");
                    }
                }.start();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}


//seperate[0] is day
//seperate[1] is month
//seperate[2] is year + time