package com.gmit.gmit3D.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gmit.gmit3D.databases.ApplicationDatabase;
import com.gmit.gmit3D.databases.ShowAssignmentDates;

import java.util.Calendar;

public class Assignment extends AppCompatActivity implements View.OnClickListener {

    private Button addButton, viewButton;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private AlertDialog.Builder alert;
    private ApplicationDatabase ad;
    private String name,time, dueDate;

    /*Runs when activity first loads*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        /*Look for button on screen by id and assign listener*/
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        viewButton = (Button) findViewById(R.id.viewButton);
        viewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.addButton:
                enterDate();
                enterTime();
                enterName();
                break;
            case R.id.viewButton:
                openView();
                break;
        }
    }

    private void insertIntoDatabase(){
            if (name != null && !name.equals("") && dueDate != null && !dueDate.equals("")) {
                try {
                    long id = ad.insertIntoAssignment(name, dueDate);
                    viewButton.setText("" + id);
                }catch(SQLException e){
                    e.printStackTrace();
                }
        }
    }

    private void openView(){
        startActivity(new Intent(this, ShowAssignmentDates.class));
    }

    private void enterName(){
        final EditText editText = new EditText(this);
        alert = new AlertDialog.Builder(this);
        alert.setView(editText);
        alert.setTitle("Assignment name");
        alert.setMessage("Enter name of the assignment ");
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                name = editText.getText().toString();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.create().show();
    }

    private void enterTime(){
        final EditText editText = new EditText(this);
        alert = new AlertDialog.Builder(this);
        alert.setView(editText);
        alert.setTitle("Enter a time");
        alert.setMessage("Time must be between 1-24 or ");
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                time = editText.getText().toString();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.create().show();
    }

    private void enterDate(){
        date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(time != null){
                    dueDate = dayOfMonth + "/" + monthOfYear + "/" + year + " " + time;
                    insertIntoDatabase();
                }
            }
        };

        new DatePickerDialog(Assignment.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
