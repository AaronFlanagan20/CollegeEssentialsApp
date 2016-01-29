package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gmit.gmit3D.database.ApplicationDatabase;
import com.gmit.gmit3D.database.TimetableInput;

import java.util.ArrayList;

public class TimeTable extends AppCompatActivity implements View.OnClickListener {

    private TableLayout layout;
    private TextView temp;
    private ApplicationDatabase ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableToolbar);
        setSupportActionBar(toolbar);

        layout = (TableLayout) findViewById(R.id.table);

        ad = new ApplicationDatabase(this);
        ad.createDatabase();

        //getAllTextViews();
    }

    private void getAllTextViews(){
        for(int i = 0; i < layout.getChildCount(); i++){
            if(layout.getChildAt(i) instanceof TableRow) {
                TableRow row = (TableRow) layout.getChildAt(i);
                for(int x = 0; x < row.getChildCount(); x++){
                    if(row.getChildAt(x) instanceof TextView){
                        temp = (TextView) row.getChildAt(x);
                        temp.setOnClickListener(this);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_icon){
            startActivity(new Intent(this, TimetableInput.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, TimetableInput.class), 1);
    }
}
