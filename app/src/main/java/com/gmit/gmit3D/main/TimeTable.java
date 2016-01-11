package com.gmit.gmit3D.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmit.gmit3D.databases.TimetableSchedule;

public class TimeTable extends AppCompatActivity implements View.OnClickListener {

    private Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        /*Look for button on screen by id and assign listener*/
        viewButton = (Button) findViewById(R.id.viewButton);
        viewButton.setOnClickListener(this);
    }

    private void openView(){
        startActivity(new Intent(this, TimetableSchedule.class));
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.viewButton:
                openView();
                break;
        }
    }
}
