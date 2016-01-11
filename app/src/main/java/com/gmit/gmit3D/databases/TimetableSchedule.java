package com.gmit.gmit3D.databases;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.gmit.gmit3D.main.R;

public class TimetableSchedule extends AppCompatActivity implements View.OnClickListener{

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_schedule);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
    }

    private void addToTimetable(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addButton:
                addToTimetable();
                break;
        }
    }
}
