package com.collegeessentials.main;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.collegeessentials.database.TimetableInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeTableTests extends ActivityInstrumentationTestCase2<TimeTable>{

    TimeTable mTimeTable;
    View addButton;
    TimetableInput timetableInput;

    public TimeTableTests(){
        super(TimeTable.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        mTimeTable = getActivity();

        addButton = (View) mTimeTable.findViewById(R.id.add_icon);
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();
        mTimeTable = null;
        timetableInput = null;
    }

    @Test
    public void testOpeningTimetableInput() throws Exception {

        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(TimetableInput.class.getName(), null, false);

        mTimeTable.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addButton.requestFocus();
                assertTrue(addButton.performClick());
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getInstrumentation().waitForIdleSync();

        timetableInput = (TimetableInput) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(timetableInput);

        timetableInput.finish();
    }

    @Test
    public void testClickEmptyView() throws Exception {

        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(TimetableInput.class.getName(), null, false);

        final TextView view = (TextView) mTimeTable.findViewById(R.id.mon_Ten);

        mTimeTable.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        timetableInput = (TimetableInput) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(timetableInput);

        timetableInput.finish();
    }

    @Test
    public void testSettingTextToView() throws Exception {

        final TextView view = (TextView) mTimeTable.findViewById(R.id.mon_Nine);

        mTimeTable.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText("asss");
            }
        });
    }
}
