package com.gmit.gmit3D.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmit.gmit3D.database.ApplicationDatabase;
import com.gmit.gmit3D.database.AssignmentCountdownTimer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Assignment extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private AlertDialog.Builder alert;
    private ApplicationDatabase ad;
    private String name,time, dueDate;
    private AssignmentCountdownTimer ct;
    private ListView lvItems;
    private List<Display> list;
    private ImageButton deleteButton;
    private ArrayList<Integer> colours;

    /*Runs when activity first loads*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);

        ad = new ApplicationDatabase(this);
        ad.createDatabase();

        lvItems = (ListView) findViewById(R.id.lvItems);
        list = new ArrayList<>();

        try {
            Cursor cursor = ad.returnAssignmentData();//cursor returns everythig from result set
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    String date = cursor.getString(1);

                    //begin split
                    String[] seperate = date.split("/");
                    String day = seperate[0];
                    String month = seperate[1];
                    date = seperate[2];//date will equal year SPACE time

                    //must split again because 2016 13:00
                    seperate = date.split(" ");//split space between year and time of day
                    String year = seperate[0];
                    String time = seperate[1];

                    //begin spliting time
                    seperate = time.split(":");
                    String hour = seperate[0];//eg 12, 1, 16
                    String minute = seperate[1];

                    //get integer values of strings
                    int minuteInt = Integer.parseInt(minute);
                    int hourInt = Integer.parseInt(hour);
                    int yearInt = Integer.parseInt(year);
                    int monthInt = Integer.parseInt(month);
                    int dayInt = Integer.parseInt(day);

                    ct = new AssignmentCountdownTimer(0, minuteInt, hourInt, dayInt, monthInt, yearInt);
                    list.add(new Display(name, ct.getIntervalMillis()));

                    lvItems.setAdapter(new CountdownAdapter(Assignment.this, list));
                }while (cursor.moveToNext());
            }
        }catch(NullPointerException e){
            e.printStackTrace();
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
            enterDate();
            enterTime();
            enterName();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void enterTime(){
        final EditText editText = new EditText(this);
        alert = new AlertDialog.Builder(this);
        alert.setView(editText);
        alert.setTitle("Enter a time");
        alert.setMessage("Time must be in 24hr format. eg. 13:00  ");
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                time = editText.getText().toString();
                if(!time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")){
                    dialog.cancel();
                    enterTime();
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

    private void insertIntoDatabase(){
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

    public class CountdownAdapter extends ArrayAdapter<Display> {

        private LayoutInflater lf;
        private List<ViewHolder> lstHolders;
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
            ViewHolder holder = null;
            RelativeLayout relativeLayout;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = lf.inflate(R.layout.assignment_list_items, parent, false);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.tvTimeRemaining = (TextView) convertView.findViewById(R.id.timeRemaining);
                deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
                convertView.setTag(holder);
                synchronized (lstHolders) {
                    lstHolders.add(holder);
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.setData(getItem(position));

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

    private String getColour(int day){

        if(day <= 7){
            return "red";
        }else if(day <= 14){
            return "orange";
        }else{
            return "green";
        }
    }

    private class ViewHolder {
        TextView name;
        TextView tvTimeRemaining;
        Display mDisplay;

        public void setData(Display item) {
            mDisplay = item;
            name.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public int updateTimeRemaining(long currentTime) {
            ViewHolder holder;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.deleteButton){
                        ad.deleteFromAssignment(mDisplay.name);
                        CharSequence text = mDisplay.name + " has been deleted!";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        Assignment.super.recreate();
                    }
                }
            });

            long timeDiff = mDisplay.expirationTime - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int days = (int) ((timeDiff / 1000) / 86400);
                tvTimeRemaining.setText(days + " days " + hours + " hrs " + minutes + " mins " + seconds + " sec");
                return days;
            } else {
                tvTimeRemaining.setText("Expired!!");
                return 0;
            }
        }
    }
}
