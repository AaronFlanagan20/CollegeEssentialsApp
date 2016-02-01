package com.gmit.gmit3D.main;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gmit.gmit3D.database.ApplicationDatabase;
import com.gmit.gmit3D.database.TimetableInput;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeTable extends AppCompatActivity implements View.OnClickListener {

    private TableLayout layout;
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

        try {
            getAllTextViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllTextViews() throws Exception {
        for(int i = 1; i < layout.getChildCount(); i++){
            if(layout.getChildAt(i) instanceof TableRow) {
                TableRow row = (TableRow) layout.getChildAt(i);
                for(int x = 1; x < row.getChildCount(); x++){
                    if(row.getChildAt(x) instanceof TextView){
                        TextView temp = (TextView) row.getChildAt(x);
                        temp.setOnClickListener(this);
                        temp.setMaxWidth(temp.getWidth());
                        temp.setMinWidth(temp.getWidth());
                        temp.setMaxHeight(temp.getWidth());
                        temp.setMaxHeight(temp.getWidth());
                        paintTimetable(temp);
                    }
                }
            }
        }
    }

    public static String getIDName(View view, Class<?> clazz) throws Exception {
        Integer id = view.getId();
        Field[] ids = clazz.getFields();
        for (int i = 0; i < ids.length; i++) {
            Object val = ids[i].get(null);
            if (val != null && val instanceof Integer
                    && ((Integer) val).intValue() == id.intValue()) {
                return ids[i].getName();
            }
        }

        return "";
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

    private void paintTimetable(TextView view) throws Exception {
        String id = getIDName(view, R.id.class).toLowerCase();
        String[] search = id.split("_");

        Cursor c = ad.returnTimetableData();
        String module, room, teacher, day, time;

        if(c.moveToFirst()){
            do{
                module = c.getString(0);
                room = c.getString(1);
                teacher = c.getString(2);
                day = c.getString(3);
                time = c.getString(4);

                if(day.equals(search[0]) && time.equals(search[1])){
                    view.setText(module + "\n" + room + "\n" + teacher);
                }

            }while (c.moveToNext());
        }
    }

    @Override
    public void onClick(View v) {
        final TextView view = (TextView) v;
        AlertDialog.Builder alert;
        if(view.getText().equals("")){
            startActivity(new Intent(this, TimetableInput.class));
        }else{
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete class ?");
            final String[] split = ((String) view.getText()).split("\n");
            TextView label = new TextView(this);
            label.setText("Delete " + split[0]);
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
            getAllTextViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
