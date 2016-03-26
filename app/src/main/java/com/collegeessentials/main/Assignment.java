package com.collegeessentials.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.collegeessentials.database.ApplicationDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.String.format;

/**
 * Assignment page allows users to input their current assignments
 * and it will countdown how long is left till it's due
 *
 * Assignments with 7 days or less are coloured red
 * Assignments with 14 days or less are coloured orange
 * Assignments with 21 days or less are coloured green
 *
 * @version 1.0
 * @see ApplicationDatabase, Display, CountdownAdapter
 */
public class Assignment extends AppCompatActivity {

    private ApplicationDatabase ad;
    private String name,time, dueDate;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);

        ad = new ApplicationDatabase(this, CollegeSelection.name);//initialize database
        ad.createDatabase();

        ListView lvItems = (ListView) findViewById(R.id.lvItems);//ListView to hold all assignments
        List<Display> list = new ArrayList<>();//list to populate with the current assignments

        try {//loop through database row by row and take out details
            Cursor cursor = ad.returnAssignmentData();
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    String date = cursor.getString(1);

                    //begin split
                    String[] separate = date.split("/");
                    String day = separate[0];
                    String month = separate[1];
                    date = separate[2];//date will equal year SPACE time

                    //must split again because 2016 13:00
                    separate = date.split(" ");//split space between year and time of day
                    String year = separate[0];
                    String time = separate[1];

                    //begin splitting time
                    separate = time.split(":");
                    String hour = separate[0];//eg 12, 1, 16
                    String minute = separate[1];

                    //get integer values of strings
                    int minuteInt = Integer.parseInt(minute);
                    int hourInt = Integer.parseInt(hour);
                    int yearInt = Integer.parseInt(year);
                    int monthInt = Integer.parseInt(month);
                    int dayInt = Integer.parseInt(day);

                    list.add(new Display(name, calculateTime(0, minuteInt, hourInt, dayInt, monthInt, yearInt)));
                    lvItems.setAdapter(new CountdownAdapter(Assignment.this, list));
                }while (cursor.moveToNext());
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    private long calculateTime(int second, int minute, int hour, int monthDay, int month, int year){
        Time futureTime = new Time();

        //set time to due date
        futureTime.set(second, minute, hour, monthDay, month, year);
        futureTime.normalize(true);
        long futureMillis = futureTime.toMillis(true);

        return futureMillis;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.add_icon){//if + sign call these methods
            enterDate();
            enterTime();
            enterName();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enterName(){//input dialog for name
        final EditText editText = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(editText);
        alert.setTitle("Assignment name");
        alert.setMessage("Enter name of the assignment ");
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                name = editText.getText().toString();
                if(!name.matches("[a-zA-Z0-9 ]+")){
                    dialog.cancel();
                    enterName();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.create().show();
    }
    //TODO: Replace dialog with time picker
    private void enterTime(){//input dialog for Time
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Assignment.this,  new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
//        final EditText editText = new EditText(this);
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setView(editText);
//        alert.setTitle("Enter a time");
//        alert.setMessage("Time must be in 24hr format. eg. 13:00  ");
//        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                time = editText.getText().toString();
//                if(!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
//                    dialog.cancel();
//                    enterTime();
//                }
//            }
//        });
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        alert.create().show();
    }

    private void enterDate(){//DatePickerDialog for picking date assignment is due
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
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

    private void insertIntoDatabase(){//insert assignment into database
        if (name != null && !name.equals("") && dueDate != null && !dueDate.equals("")) {
            try {
                ad.insertIntoAssignment(name, dueDate);
                CharSequence text = name + " has been added!";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                name = null;
                time = null;
                dueDate = null;
                this.recreate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    private class Display {
        String name;
        long expirationTime;

        public Display(String name, long expirationTime) {
            this.name = name;
            this.expirationTime = expirationTime;
        }
    }

    /**
     * CountdownAdapter acts as a custom adapter for our list
     * It populates the screen with our assignments and constantly updates their times
     */
    public class CountdownAdapter extends ArrayAdapter<Display> {

        private LayoutInflater lf;
        private final List<ViewHolder> lstHolders;
        private Handler mHandler = new Handler();
        private Runnable updateRemainingTimeRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (lstHolders) {
                    long currentTime = System.currentTimeMillis();
                    for (ViewHolder holder : lstHolders) {
                        holder.updateTimeRemaining(currentTime);
                    }
                }
            }
        };

        public CountdownAdapter(Context context, List<Display> objects) {
            super(context, 0, objects);
            lf = LayoutInflater.from(context);
            lstHolders = new ArrayList<>();
            startUpdateTimer();
        }

        private void startUpdateTimer() {
            Timer tmr = new Timer();
            tmr.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(updateRemainingTimeRunnable);
                }
            }, 1000, 1000);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*
                LIST
                holder is an assignment
                holder
                holder
             */
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();//get our view
                convertView = lf.inflate(R.layout.assignment_list_items, parent, false);//add our list
                holder.name = (TextView) convertView.findViewById(R.id.name);//setup out name
                holder.tvTimeRemaining = (TextView) convertView.findViewById(R.id.timeRemaining);//and time left
                deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
                convertView.setTag(holder);//set our view to hold these
                synchronized (lstHolders) {
                    lstHolders.add(holder);//add our holder to the list
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.setData(getItem(position));//set all the data to it

            //decide what colour to set based on remaining time
            String daysLeft = getColour(holder.updateTimeRemaining(System.currentTimeMillis()));
            if(daysLeft.equals("red")){
                convertView.setBackgroundColor(Color.rgb(255,0,50));
            }if(daysLeft.equals("orange")){
                convertView.setBackgroundColor( Color.rgb(255,165,0));
            }if(daysLeft.equals("green")){
                convertView.setBackgroundColor(Color.rgb(0,255,127));
            }

            return convertView;
        }
    }

    //decide what colours to set to time frames
    private String getColour(int day){

        if(day <= 7){
            return "red";
        }else if(day <= 14){
            return "orange";
        }else{
            return "green";
        }
    }

    //holds our assignments
    private class ViewHolder {
        TextView name;//assignment name
        TextView tvTimeRemaining;//assignment time left
        Display mDisplay;//object containing the details

        public void setData(Display item) {
            mDisplay = item;//set object
            name.setText(item.name);//set objects name to ViewHolder view
            updateTimeRemaining(System.currentTimeMillis());//update the assignments time
        }

        public int updateTimeRemaining(long currentTime) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.deleteButton){
                        ad.deleteFromAssignment(mDisplay.name);//delete from database where name is ...
                        CharSequence text = mDisplay.name + " has been deleted!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        Assignment.super.recreate();//refresh screen with removed assignment
                    }
                }
            });

            /* Calculate and set the time left on the assignment */
            long timeDiff = mDisplay.expirationTime - currentTime;
            if(timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int days = (int) ((timeDiff / 1000) / 86400);
                tvTimeRemaining.setText(format("%d days %d hrs %d min's %d sec", days, hours, minutes, seconds));
                return days;
            } else {
                tvTimeRemaining.setText("Expired!!");
                return 0;
            }
        }
    }
}
