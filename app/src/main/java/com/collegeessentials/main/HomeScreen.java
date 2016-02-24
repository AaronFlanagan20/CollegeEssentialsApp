package com.collegeessentials.main;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.collegeessentials.camera.CameraActivity;
import com.collegeessentials.location.MapsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cameraButton, timetableButton, assignmentButton, aboutButton, mapButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        File file = new File(getFilesDir() + "/" + CollegeSelection.name);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            ImageView imageView = (ImageView) findViewById(R.id.frontImage);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 530);
            rl.setMargins(0, 0, 0, 690);
            imageView.setLayoutParams(rl);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in !=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        cameraButton = (ImageButton)findViewById(R.id.locate);
        cameraButton.setOnClickListener(this);

        timetableButton = (ImageButton)findViewById(R.id.timetable);
        timetableButton.setOnClickListener(this);

        assignmentButton = (ImageButton)findViewById(R.id.assignment);
        assignmentButton.setOnClickListener(this);

        aboutButton = (ImageButton)findViewById(R.id.about);
        aboutButton.setOnClickListener(this);

        mapButton = (ImageButton)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
    }

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
