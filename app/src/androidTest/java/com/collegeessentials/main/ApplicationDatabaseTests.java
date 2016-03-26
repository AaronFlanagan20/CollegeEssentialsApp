package com.collegeessentials.main;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.collegeessentials.database.ApplicationDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApplicationDatabaseTests extends AndroidTestCase{

    private ApplicationDatabase ad;

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        ad = new ApplicationDatabase(this.getContext(), "Galway-Mayo Institute of Technology (Galway)");
        ad.createDatabase();
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();

        ad = null;
    }

    @Test
    public void testInsertingToAssignments(){

        ad.insertIntoAssignment("java", "03/17/2015 18:00");

        Cursor c = ad.returnAssignmentData();

        c.moveToFirst();

        assertEquals(c.getString(0), "java");
        assertEquals(c.getString(1), "03/17/2015 18:00");
    }

    @Test
    public void testInsertingToTimetable(){
        ad.insertIntoTimetable("Mobile Dev", "470", "Damien Costello", "mon", "nine");

        Cursor c = ad.returnTimetableData();
        c.moveToFirst();

        assertEquals(c.getString(0), "Mobile Dev");
        assertEquals(c.getString(1), "470");
        assertEquals(c.getString(2), "Damien Costello");
        assertEquals(c.getString(3), "mon");
        assertEquals(c.getString(4), "nine");

    }

    @Test
    public void testInsertingMarkers(){
        ad.insertIntoMarkers("Place", 53.1234f, 45.123f);

        Cursor c = ad.returnMarkers();
        c.moveToFirst();

        assertEquals(c.getString(0), "Place");
        assertEquals(c.getString(1), "53.1234");
        assertEquals(c.getString(2), "45.123");
    }

}
