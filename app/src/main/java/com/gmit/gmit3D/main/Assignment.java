package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Assignment extends AppCompatActivity implements View.OnClickListener {

    /*Runs when activity first loads*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        /*Look for button on screen by id and assign listener*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons

        }
    }
}
