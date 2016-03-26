package com.collegeessentials.main;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.collegeessentials.database.HerokuConnection;

import org.junit.*;

public class CollegeSelectionTests extends ActivityInstrumentationTestCase2<CollegeSelection>  {

    private ListView listView;
    private View child0;
    private CollegeSelection mCollegeSelection;
    private HerokuConnection conn;

    public CollegeSelectionTests(){
        super(CollegeSelection.class);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        mCollegeSelection = getActivity();

        listView = (ListView) mCollegeSelection.findViewById(R.id.collegeList);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        listView = null;
        child0 = null;
        mCollegeSelection = null;
        conn = null;
    }

    @Test
    public void testClickListItem() throws Exception {
        child0 = listView.getChildAt(0);
        child0.performClick();

        TextView view = (TextView) child0;
        assertEquals("Galway-Mayo Institute of Technology (Galway)", (String) view.getText());

        child0 = listView.getChildAt(3);
        child0.performClick();

        view = (TextView) child0;
        assertEquals("Athlone Institute of Technology (AIT)", (String) view.getText());

        tearDown();//destroy
        setUp();//and reset for next test
    }

    @Test
    public void testHerokuConnectionNotNull() throws Exception {
        conn = new HerokuConnection();

        assertNotNull(conn);

        tearDown();//destroy
        setUp();//and reset for next test
    }

    @Test
    public void testGetImagesFromHeroku(){
        conn = new HerokuConnection();

        conn.getImagesFromDB(mCollegeSelection.getApplicationContext(), "Athlone Institute of Technology (AIT)");//create the image

        assertNotNull(mCollegeSelection.getFilesDir() + "/" + "Athlone Institute of Technology (AIT)");
    }
}
