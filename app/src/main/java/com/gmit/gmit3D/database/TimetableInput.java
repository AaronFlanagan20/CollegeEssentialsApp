package com.gmit.gmit3D.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gmit.gmit3D.main.R;

public class TimetableInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableToolbar);
        setSupportActionBar(toolbar);

    }

}
