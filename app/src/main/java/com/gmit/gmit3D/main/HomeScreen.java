package com.gmit.gmit3D.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends Activity implements View.OnClickListener {

    private Button cameraButton, timetableButton, assignmentButton, aboutButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        cameraButton = (Button)findViewById(R.id.locate);//represents locate button on home scrren
        cameraButton.setOnClickListener(this);

        timetableButton = (Button)findViewById(R.id.timetable);//represents locate button on home scrren
        timetableButton.setOnClickListener(this);

        assignmentButton = (Button)findViewById(R.id.assignment);//represents locate button on home scrren
        assignmentButton.setOnClickListener(this);

        aboutButton = (Button)findViewById(R.id.about);//represents locate button on home scrren
        aboutButton.setOnClickListener(this);
    }

    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.main.Camera"));//open Camera activity
        this.finish();
    }

    private void openTimetable(){
        startActivity(new Intent("com.gmit.gmit3D.main.Timetable"));//open Camera activity
        this.finish();
    }

    private void openAssignment(){
        startActivity(new Intent("com.gmit.gmit3D.main.Assignment"));//open Camera activity
        this.finish();
    }

    private void openAbout(){
        startActivity(new Intent("com.gmit.gmit3D.main.About"));//open Camera activity
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
