package com.gmit.gmit3D.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class Timetable extends AppCompatActivity {

    private Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableToolbar);
        setSupportActionBar(toolbar);


        /*Look for button on screen by id and assign listener*/
        //viewButton = (Button) findViewById(R.id.viewButton);
        //viewButton.setOnClickListener(this);
    }

//    private void openView(){
//        startActivity(new Intent(this, TimetableSchedule.class));
//    }
//
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.viewButton:
//                openView();
//                break;
//        }
//    }
}
