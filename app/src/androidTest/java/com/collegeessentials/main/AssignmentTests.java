package com.collegeessentials.main;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 Only button action can be tested because AlertDialog is built in class and not xml.
 Same rule applies to listview of assignments. Child will always return null in test.
 ApplicationDatabaseTests will test the insertion and deletion of the assignment database
 */
public class AssignmentTests extends ActivityInstrumentationTestCase2<Assignment>{

    private Assignment mAssignment;
    private View addButton;
    private ListView listView;

    public AssignmentTests(){
        super(Assignment.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        mAssignment = getActivity();

        addButton = (View) mAssignment.findViewById(R.id.add_icon);
        listView = (ListView) mAssignment.findViewById(R.id.lvItems);
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();

        mAssignment = null;
        addButton = null;
    }

    @Test
    public void testHittingAddButton() throws Exception {

        mAssignment.runOnUiThread(new Runnable() {
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
    }
}