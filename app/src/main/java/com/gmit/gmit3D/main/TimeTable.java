package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class TimeTable extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        /*Look for button on screen by id and assign listener*/
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        view = (TextView) findViewById(R.id.text);
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
