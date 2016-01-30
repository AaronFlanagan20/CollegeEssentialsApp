package com.gmit.gmit3D.database;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmit.gmit3D.main.R;

import java.sql.Time;
import java.util.Calendar;

public class TimetableInput extends AppCompatActivity implements View.OnClickListener {

    private EditText name, room, teacher;
    private TextView dayPicker;
    private ApplicationDatabase ad;
    private Button time, colour;
    private String selectedTime = "nine", selectedDay = "mon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableinputToolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.name);
        room = (EditText) findViewById(R.id.room);
        teacher = (EditText) findViewById(R.id.teacher);

        time = (Button) findViewById(R.id.timeButton);
        time.setOnClickListener(this);

        colour = (Button) findViewById(R.id.colourButton);
        colour.setOnClickListener(this);

        dayPicker = (TextView) findViewById(R.id.dayPick);
        dayPicker.setOnClickListener(this);

        ad = new ApplicationDatabase(this);
        ad.createDatabase();

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean nameMatch, roomMatch;
        int id = item.getItemId();

        if(id == R.id.save_icon){
            String nameText = name.getText().toString();
            if(nameText.equals("")) {
                nameMatch = false;
                CharSequence text = " Name must not be empty";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else{
                nameMatch = true;
            }

            String roomText = room.getText().toString();
            if(!roomText.equals("") && !roomText.matches("[a-zA-Z0-9 ]+")) {
                roomMatch = false;
                CharSequence text = " Room must only contain numbers or letters";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else if(roomText.equals("")){
                roomMatch = false;
                CharSequence text = " Room must not be empty";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else {
                roomMatch = true;
            }

            String tText = teacher.getText().toString();
            if(tText.matches("[a-zA-Z ]+") && nameMatch && roomMatch){
                ad.insertIntoTimetable(nameText, roomText, tText, selectedDay, selectedTime);
                this.finish();
            }else if(tText.equals("")){
                CharSequence text = " Teacher must not be empty";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else{
                CharSequence text = " Teacher must only contain letters";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TimetableInput.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch (selectedHour){
                    case 9: selectedTime = "nine"; break;
                    case 10: selectedTime = "ten"; break;
                    case 11: selectedTime = "eleven"; break;
                    case 12: selectedTime = "twelve"; break;
                    case 1: selectedTime = "one"; break;
                    case 2: selectedTime = "two"; break;
                    case 3: selectedTime = "three"; break;
                    case 4: selectedTime = "four"; break;
                    case 5: selectedTime = "five"; break;
                    case 6: selectedTime = "six"; break;
                }
                time.setText("" + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickDay(){
        final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        final ListPopupWindow listPopupWindow = new ListPopupWindow(TimetableInput.this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(TimetableInput.this, R.layout.timetable_list_items, days));
        listPopupWindow.setAnchorView(dayPicker);

        listPopupWindow.setWidth(900);
        listPopupWindow.setHeight(800);

        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = days[position];
                dayPicker.setText(selected);

                switch (position){
                    case 0: selectedDay = "mon"; break;
                    case 1: selectedDay = "tue"; break;
                    case 2: selectedDay = "wed"; break;
                    case 3: selectedDay = "thu"; break;
                    case 4: selectedDay = "fri"; break;
                }
                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timeButton:
                pickTime();
                break;
            case R.id.dayPick:
                pickDay();
                break;
        }
    }
}
