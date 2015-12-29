package com.gmit.gmit3D.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class Camera extends Activity implements View.OnClickListener {

    private Button cameraButton;
    private Button backButton;
    private static final int number = 1337;

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, number);
    }

    //Go back to main screen
    private void goBack(){
        startActivity(new Intent(this,HomeScreen.class));//open Camera class
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
