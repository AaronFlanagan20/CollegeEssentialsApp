package com.gmit.gmit3D.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Locate extends Activity implements View.OnClickListener {

    private Button cameraButton;
    private Button backButton;

    /* called when activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        /*Find buttons on screen and set listeners*/
        cameraButton = (Button) findViewById(R.id.openCamera);
        cameraButton.setOnClickListener(this);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    //Open camera
    private void openCamera() {
        startActivity(new Intent("com.gmit.gmit3D.camera.CameraActivity"));//open cameraActivity class
        this.finish();
    }

    //Go back to main screen
    private void goBack(){
        startActivity(new Intent(this,HomeScreen.class));//open home screen class
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.openCamera://if locate button open camera activity
                openCamera();
                break;
            case R.id.backButton://if back button go to home screen
                goBack();
                break;
        }
    }
}
