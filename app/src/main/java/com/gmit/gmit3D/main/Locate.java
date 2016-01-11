package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Locate extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cameraButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        /*Find buttons on screen and set listeners*/
        cameraButton = (ImageButton) findViewById(R.id.openCamera);
        cameraButton.setOnClickListener(this);
    }

    //Open camera
    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.camera.CameraActivity"));//open cameraActivity class
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.openCamera://if locate button open camera activity
                openCamera();
                break;
        }
    }
}
