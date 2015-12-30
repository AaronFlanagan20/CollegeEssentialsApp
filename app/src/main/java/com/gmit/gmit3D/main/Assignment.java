package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Assignment extends AppCompatActivity implements View.OnClickListener {

    private Button backButton;

    /*Runs when activity first loads*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        /*Look for button on screen by id and assign listener*/
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    //Go back to main screen
    private void goBack(){
        startActivity(new Intent(this,HomeScreen.class));//open Locate class
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.backButton://if back button go to home screen
                goBack();
                break;
        }
    }
}
