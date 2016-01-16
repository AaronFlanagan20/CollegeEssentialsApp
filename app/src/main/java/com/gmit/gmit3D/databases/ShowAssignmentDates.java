package com.gmit.gmit3D.databases;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gmit.gmit3D.main.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/*
 * Creates a list of assignments due and keeps track by counting down
 */
public class ShowAssignmentDates extends AppCompatActivity {

    private ApplicationDatabase ad;
    private AssignmentCountdownTimer ct;
    private ListView lvItems;
    private List<Display> list;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assignment_dates);

        ad = new ApplicationDatabase(getBaseContext());//get database object
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

                    lvItems.setAdapter(new CountdownAdapter(ShowAssignmentDates.this, list));
                }while (cursor.moveToNext());
            }
            }catch(NullPointerException e){
                e.printStackTrace();
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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = lf.inflate(R.layout.list_item, parent, false);
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

            return convertView;
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

        public void updateTimeRemaining(long currentTime) {
            ViewHolder holder;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.deleteButton){
                        ad.deleteFromAssignment(mDisplay.name);
                        ShowAssignmentDates.super.recreate();
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
            } else {
                tvTimeRemaining.setText("Expired!!");
            }
        }
    }
}
