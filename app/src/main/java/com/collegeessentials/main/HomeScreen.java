package com.collegeessentials.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.collegeessentials.camera.CameraActivity;
import com.collegeessentials.location.MapsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The HomeScreen activity displays the campus picture of the chosen college and the five sections of the app.
 * Assignment page, TimeTable page, Google Maps, About page and the Camera section
 *
 * @version 1.0
 * @see Assignment, TimeTable, About, MapsActivity, CameraActivity
 */

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        File file = new File(getFilesDir() + "/" + CollegeSelection.name);//get image taken down
        InputStream in = null;
        try {
            in = new FileInputStream(file);//convert file to inputstream
            ImageView imageView = (ImageView) findViewById(R.id.frontImage);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());//decode the file path to a bitmap
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 530);//set parent layout
            rl.setMargins(0, 0, 0, 690);//set image positions
            imageView.setLayoutParams(rl);//set image to parent layout
            imageView.setImageBitmap(bitmap);//set image on screen
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in !=null){
                try {
                    in.close();//close the inputstream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraActivity);
        cameraButton.setOnClickListener(this);

        ImageButton timetableButton = (ImageButton)findViewById(R.id.timetable);
        timetableButton.setOnClickListener(this);

        ImageButton assignmentButton = (ImageButton)findViewById(R.id.assignment);
        assignmentButton.setOnClickListener(this);

        ImageButton aboutButton = (ImageButton)findViewById(R.id.about);
        aboutButton.setOnClickListener(this);

        ImageButton mapButton = (ImageButton)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
    }

    /* Methods to start each individual activity */
    private void openCamera() {
        startActivity(new Intent(this, CameraActivity.class));//open camera activity
    }

    private void openTimetable(){
        startActivity(new Intent(this, TimeTable.class));//open timetable activity
    }

    private void openAssignment(){
        startActivity(new Intent(this, Assignment.class));//open assignment activity
    }

    private void openAbout(){
        startActivity(new Intent(this, About.class));//open about activity
    }

    private void openMap(){
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraActivity://open locate activity
                openCamera();
                break;
            case R.id.timetable://open timetable activity
                openTimetable();
                break;
            case R.id.assignment://open assignment activity
                openAssignment();
                break;
            case R.id.about://open about activity
                openAbout();
                break;
            case R.id.mapButton://open google maps
                openMap();
                break;
        }
    }
}
