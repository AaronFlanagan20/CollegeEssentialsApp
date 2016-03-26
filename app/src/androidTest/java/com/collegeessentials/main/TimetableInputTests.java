package com.collegeessentials.main;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.collegeessentials.database.TimetableInput;

import org.junit.*;

public class TimetableInputTests extends ActivityInstrumentationTestCase2<TimetableInput>{

    TimetableInput mTimetableInput;

    public TimetableInputTests(){
        super(TimetableInput.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        mTimetableInput = getActivity();
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();

        mTimetableInput = null;
    }

    @Test
    public void testInsertingClass(){

        final EditText  name = (EditText) mTimetableInput.findViewById(R.id.name);
        final EditText room = (EditText) mTimetableInput.findViewById(R.id.room);
        final EditText teacher = (EditText) mTimetableInput.findViewById(R.id.teacher);

        final TextView day = (TextView) mTimetableInput.findViewById(R.id.dayPick);

        final Button time = (Button) mTimetableInput.findViewById(R.id.timeButton);

        final View save = (View) mTimetableInput.findViewById(R.id.save_icon);

        mTimetableInput.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText("Distributed Systems");
                room.setText("209");
                teacher.setText("John Healy");
                time.setText("10");
                day.setText("Tuesday");
            }
        });

        getInstrumentation().waitForIdleSync();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mTimetableInput.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
    }
}
