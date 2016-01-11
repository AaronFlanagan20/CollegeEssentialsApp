package com.gmit.gmit3D.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cameraButton, timetableButton, assignmentButton, aboutButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        cameraButton = (ImageButton)findViewById(R.id.locate);//represents locate button on home screen
        cameraButton.setOnClickListener(this);

        timetableButton = (ImageButton)findViewById(R.id.timetable);//represents timetable button on home screen
        timetableButton.setOnClickListener(this);

        assignmentButton = (ImageButton)findViewById(R.id.assignment);//represents assignment button on home screen
        assignmentButton.setOnClickListener(this);

        aboutButton = (ImageButton)findViewById(R.id.about);//represents locate about on home screen
        aboutButton.setOnClickListener(this);
    }

    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.main.Locate"));//open Locate activity
    }

    private void openTimetable(){
        startActivity(new Intent("com.gmit.gmit3D.main.Timetable"));//open timetable activity
    }

    private void openAssignment(){
        startActivity(new Intent("com.gmit.gmit3D.main.Assignment"));//open assignment activity
    }

    private void openAbout(){
        startActivity(new Intent("com.gmit.gmit3D.main.About"));//open about activity
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.locate://open locate activity
                openCamera();
                break;
            case R.id.timetable://open timetable activity
                openTimetable();
                break;
            case R.id.assignment://open assignment activity
                openAssignment();
                break;
            case R.id.about://open assignment activity
                openAbout();
                break;
        }
    }
}
