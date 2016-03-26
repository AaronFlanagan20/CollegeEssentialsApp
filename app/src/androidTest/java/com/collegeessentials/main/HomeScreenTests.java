package com.collegeessentials.main;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;

import com.collegeessentials.camera.CameraActivity;
import com.collegeessentials.location.MapsActivity;

import org.junit.*;

public class HomeScreenTests extends ActivityInstrumentationTestCase2<HomeScreen> {

    private ImageButton cameraButton, timetableButton, assignmentButton, aboutButton, mapButton;
    private HomeScreen mHomeScreen;

    public HomeScreenTests() {
        super(HomeScreen.class);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();

        mHomeScreen = getActivity();

        cameraButton = (ImageButton) mHomeScreen.findViewById(R.id.cameraActivity);
        timetableButton = (ImageButton) mHomeScreen.findViewById(R.id.timetable);
        assignmentButton = (ImageButton) mHomeScreen.findViewById(R.id.assignment);
        aboutButton = (ImageButton) mHomeScreen.findViewById(R.id.about);
        mapButton = (ImageButton) mHomeScreen.findViewById(R.id.mapButton);
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();

        cameraButton = null;
        timetableButton = null;
        assignmentButton = null;
        aboutButton = null;
        mapButton = null;
        mHomeScreen = null;
    }

    @Test
    public void testCameraButtonClick() throws Exception {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CameraActivity.class.getName(), null, false);

        mHomeScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraButton.performClick();
            }
        });

        CameraActivity cameraActivity = (CameraActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(cameraActivity);
        cameraActivity.finish();

        tearDown();
        setUp();
    }

    @Test
    public void testTimetableButtonClick() throws Exception {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(TimeTable.class.getName(), null, false);

        mHomeScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timetableButton.performClick();
            }
        });

        TimeTable timeTableActivity = (TimeTable) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(timeTableActivity);
        timeTableActivity.finish();

        tearDown();
        setUp();
    }

    @Test
    public void testAssignmentButtonClick() throws Exception {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Assignment.class.getName(), null, false);

        mHomeScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assignmentButton.performClick();
            }
        });

        Assignment assignmentActivity = (Assignment) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(assignmentActivity);
        assignmentActivity.finish();

        tearDown();
        setUp();
    }

    @Test
    public void testAboutButtonClick() throws Exception {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(About.class.getName(), null, false);

        mHomeScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aboutButton.performClick();
            }
        });

        About aboutActivity = (About) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(aboutActivity);
        aboutActivity.finish();

        tearDown();
        setUp();
    }

    @Test
    public void testMapButtonClick() throws Exception {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MapsActivity.class.getName(), null, false);

        mHomeScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapButton.performClick();
            }
        });

        MapsActivity mapsActivity = (MapsActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        assertNotNull(mapsActivity);
        mapsActivity.finish();

        tearDown();
        setUp();
    }
}
