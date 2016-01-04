package com.gmit.gmit3D.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button cameraButton, timetableButton, assignmentButton, aboutButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        cameraButton = (Button)findViewById(R.id.locate);//represents locate button on home screen
        cameraButton.setOnClickListener(this);

        timetableButton = (Button)findViewById(R.id.timetable);//represents timetable button on home screen
        timetableButton.setOnClickListener(this);

        assignmentButton = (Button)findViewById(R.id.assignment);//represents assignment button on home screen
        assignmentButton.setOnClickListener(this);

        aboutButton = (Button)findViewById(R.id.about);//represents locate about on home screen
        aboutButton.setOnClickListener(this);
    }

    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.main.Locate"));//open Locate activity
        this.finish();
    }

    private void openTimetable(){
        startActivity(new Intent("com.gmit.gmit3D.main.Timetable"));//open timetable activity
        this.finish();
    }

    private void openAssignment(){
        startActivity(new Intent("com.gmit.gmit3D.main.Assignment"));//open assignment activity
        this.finish();
    }

    private void openAbout(){
        startActivity(new Intent("com.gmit.gmit3D.main.About"));//open about activity
        this.finish();
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
