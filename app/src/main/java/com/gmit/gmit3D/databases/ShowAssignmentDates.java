package com.gmit.gmit3D.databases;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gmit.gmit3D.main.R;

import java.sql.SQLException;

public class ShowAssignmentDates extends AppCompatActivity {

    private TextView view1, view2;
    private ApplicationDatabase ad;
    private AssignmentCountdownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assignment_dates);

        ad = new ApplicationDatabase(getBaseContext());
        ad.createDatabase();

        view1 = (TextView) findViewById(R.id.moduleText);

        try {
            Cursor cursor = ad.returnAssignmentData();//cursor returns everythig from result set
            if(cursor.moveToFirst()){
                view1.setText("ASDSSD" + " " + cursor.getString(0));

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
