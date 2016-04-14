package com.collegeessentials.database;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.collegeessentials.main.CollegeSelection;
import com.collegeessentials.main.R;
import com.collegeessentials.main.TimeTable;

import java.util.Calendar;

/**
 * This activity gives the user the option to input a module name, room number and teacher name
 * then select the day and time of the class. It inputs all the details into the database
 *
 * @version 1.0
 * @see ApplicationDatabase
 */
public class TimetableInput extends AppCompatActivity implements View.OnClickListener {

    private EditText name, room, teacher;
    private TextView dayPicker;
    private ApplicationDatabase ad;
    private Button time;
    private String selectedTime = "nine", selectedDay = "mon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.timetableinputToolbar);
        setSupportActionBar(toolbar);

        /* Usual setup */
        name = (EditText) findViewById(R.id.name);
        room = (EditText) findViewById(R.id.room);
        teacher = (EditText) findViewById(R.id.teacher);

        time = (Button) findViewById(R.id.timeButton);
        time.setOnClickListener(this);

        dayPicker = (TextView) findViewById(R.id.dayPick);
        dayPicker.setOnClickListener(this);

        ad = new ApplicationDatabase(this, CollegeSelection.name);
        ad.createDatabase();

        if(TimeTable.feildClick) {
            String id = getIntent().getStringExtra("day_time");
            String[] split = id.split("_");

            switch (split[0]) {
                case "mon":
                    selectedDay = "mon";
                    dayPicker.setText("Monday");
                    break;
                case "tue":
                    selectedDay = "tue";
                    dayPicker.setText("Tuesday");
                    break;
                case "wed":
                    selectedDay = "wed";
                    dayPicker.setText("Wednesday");
                    break;
                case "thu":
                    selectedDay = "thu";
                    dayPicker.setText("Thursday");
                    break;
                case "fri":
                    selectedDay = "fri";
                    dayPicker.setText("Friday");
                    break;
            }

            switch (split[1]) {
                case "nine":
                    selectedTime = "nine";
                    time.setText("9:00");
                    break;
                case "ten":
                    selectedTime = "ten";
                    time.setText("10:00");
                    break;
                case "eleven":
                    selectedTime = "eleven";
                    time.setText("11:00");
                    break;
                case "twelve":
                    selectedTime = "twelve";
                    time.setText("12:00");
                    break;
                case "one":
                    selectedTime = "one";
                    time.setText("13:00");
                    break;
                case "two":
                    selectedTime = "two";
                    time.setText("14:00");
                    break;
                case "three":
                    selectedTime = "three";
                    time.setText("15:00");
                    break;
                case "four":
                    selectedTime = "four";
                    time.setText("16:00");
                    break;
                case "five":
                    selectedTime = "five";
                    time.setText("17:00");
                    break;
                case "six":
                    selectedTime = "six";
                    time.setText("18:00");
                    break;
            }
        }

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
                CharSequence text = " Name must not be empty";//error notification
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else{
                nameMatch = true;
            }

            String roomText = room.getText().toString();
            if(!roomText.equals("") && !roomText.matches("[a-zA-Z0-9 ]+")) {
                roomMatch = false;
                CharSequence text = " Room must only contain numbers or letters";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();//error notification
            }else if(roomText.equals("")){
                roomMatch = false;
                CharSequence text = " Room must not be empty";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();//error notification
            }else {
                roomMatch = true;
            }

            String tText = teacher.getText().toString();
            if(tText.matches("[a-zA-Z ]+") && nameMatch && roomMatch){
                ad.insertIntoTimetable(nameText, roomText, tText, selectedDay, selectedTime);
                this.finish();
            }else if(tText.equals("")){
                CharSequence text = " Teacher must not be empty";//error notification
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }else{
                CharSequence text = " Teacher must only contain letters";//error notification
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickTime(){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TimetableInput.this,  new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch (selectedHour){//settings time based on user input
                    case 9: selectedTime = "nine"; break;
                    case 10: selectedTime = "ten"; break;
                    case 11: selectedTime = "eleven"; break;
                    case 12: selectedTime = "twelve"; break;
                    case 13: selectedTime = "one"; break;
                    case 14: selectedTime = "two"; break;
                    case 15: selectedTime = "three"; break;
                    case 16: selectedTime = "four"; break;
                    case 17: selectedTime = "five"; break;
                    case 18: selectedTime = "six"; break;
                    default: CharSequence text = "Time 7 pm to 8am are not allowed";
                             Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show(); break;//error notification
                }
                time.setText("" + selectedHour);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickDay(){//popup list with days to select
        final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        final ListPopupWindow listPopupWindow = new ListPopupWindow(TimetableInput.this);
        listPopupWindow.setAdapter(new ArrayAdapter<>(TimetableInput.this, R.layout.timetable_list_items, days));
        listPopupWindow.setAnchorView(dayPicker);

        listPopupWindow.setWidth(900);
        listPopupWindow.setHeight(800);

        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = days[position];
                dayPicker.setText(selected);

                switch (position){//set strings based on day selected
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
