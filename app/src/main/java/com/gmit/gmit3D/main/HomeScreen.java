package com.gmit.gmit3D.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gmit.gmit3D.database.ApplicationDatabase;
import com.gmit.gmit3D.location.PhoneLocation;

import java.util.Locale;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cameraButton, timetableButton, assignmentButton, aboutButton, mapButton;
    ApplicationDatabase ad;

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

        mapButton = (ImageButton)findViewById(R.id.mapButton);//link to gmaps
        mapButton.setOnClickListener(this);

        ad = new ApplicationDatabase(getBaseContext());
        ad.createDatabase();
    }

    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.main.Locate"));//open Locate activity
    }

    private void openTimetable(){
        startActivity(new Intent("com.gmit.gmit3D.main.TimeTable"));//open timetable activity
    }

    private void openAssignment(){
        startActivity(new Intent("com.gmit.gmit3D.main.Assignment"));//open assignment activity
    }

    private void openAbout(){
        startActivity(new Intent("com.gmit.gmit3D.main.About"));//open about activity
    }

    private void openMap(){
        PhoneLocation loc = new PhoneLocation(this);
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install the google maps application", Toast.LENGTH_LONG).show();
            }
        }
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
            case R.id.mapButton:
                openMap();
                break;
        }
    }
}
