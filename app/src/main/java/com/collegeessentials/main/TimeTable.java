package com.collegeessentials.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.collegeessentials.database.ApplicationDatabase;
import com.collegeessentials.database.TimetableInput;

import java.lang.reflect.Field;

/**
 * This class supplies the user with the option to enter in their class timetable
 * The module name, room number and teacher can all be entered in.
 *
 * If a field is clicked and it's empty the input section will open.
 * If it's used the option to delete it will open up
 *
 * @version 1.0
 * @see ApplicationDatabase
 */
public class TimeTable extends AppCompatActivity implements View.OnClickListener {

    private TableLayout layout;
    private ApplicationDatabase ad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableToolbar);
        setSupportActionBar(toolbar);

        layout = (TableLayout) findViewById(R.id.table);

        ad = new ApplicationDatabase(this, CollegeSelection.name);
        ad.createDatabase();

        try {
            getAllTextViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllTextViews() throws Exception {
        for(int i = 1; i < layout.getChildCount(); i++){//loop through parent layout
            if(layout.getChildAt(i) instanceof TableRow) {//get every child that's a TableRow
                TableRow row = (TableRow) layout.getChildAt(i);//create temp TableRow
                for(int x = 1; x < row.getChildCount(); x++){//loop through temp TableRow
                    if(row.getChildAt(x) instanceof TextView){//get every TextView in temp TableRow
                        TextView temp = (TextView) row.getChildAt(x);//set child to temp
                        temp.setOnClickListener(this);
                        temp.setMaxWidth(temp.getWidth());//set size's
                        temp.setMinWidth(temp.getWidth());
                        temp.setMaxHeight(temp.getWidth());
                        temp.setMaxHeight(temp.getWidth());
                        paintTimetable(temp);//fill child with details stored in database
                    }
                }
            }
        }
    }

    public static String getIDName(View view, Class<?> clazz) throws Exception {
        Integer id = view.getId();//get id of the view item
        Field[] ids = clazz.getFields();//get all field's in the class
        for (Field id1 : ids) {// loop through every item in field and create a temp object
            Object val = id1.get(null);
            if (val != null && val instanceof Integer
                    && ((Integer) val).intValue() == id.intValue()) {//testing object's integer value
                return id1.getName();//return temp's id
            }
        }

        return "";//else return empty
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();//get item id
        if(id == R.id.add_icon){//if + sign is hit
            startActivity(new Intent(this, TimetableInput.class));//start activity
        }
        return super.onOptionsItemSelected(item);
    }

    private void paintTimetable(TextView view) throws Exception {
        String id = getIDName(view, R.id.class).toLowerCase();//pass in an object and get it's id
        String[] search = id.split("_");//split day from time (MON) _NINE

        Cursor c = ad.returnTimetableData();//return all data from database

        if(c.moveToFirst()){
            do{
                // Collect each rows data
                String module = c.getString(0);
                String room = c.getString(1);
                String teacher = c.getString(2);
                String day = c.getString(3);
                String time = c.getString(4);

                if(day.equals(search[0]) && time.equals(search[1])){// if day = "mon" and time = "nine"
                    view.setText(String.format("%s\n%s\n%s", module, room, teacher));//paint details on screen
                }

            }while (c.moveToNext());//move to next row in database
        }
    }

    @Override
    public void onClick(View v) {
        final TextView view = (TextView) v;//convert view item to sub-child TextView
        AlertDialog.Builder alert;
        if(view.getText().equals("")){
            startActivity(new Intent(this, TimetableInput.class));//if field is null start input activity
        }else{//if not null open up a dialog page with an option to delete the selected field
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete class ?");
            final String[] split = ((String) view.getText()).split("\n");
            TextView label = new TextView(this);
            label.setText(String.format("Delete %s", split[0]));
            alert.setCustomTitle(label);
            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ad.deleteFromTimetable(split[0]);
                    view.setText("");
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alert.create().show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getAllTextViews();//called to do the repaint methods
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
