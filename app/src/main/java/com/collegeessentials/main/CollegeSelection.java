package com.collegeessentials.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.collegeessentials.database.ApplicationDatabase;
import com.collegeessentials.database.HerokuConnection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * The CollegeSelection activity is the initial activity on start-up.
 * It supplies the user with a list of colleges to select.
 * Each college contains it's own database and campus picture that is brought down from our heroku server
 * and then displayed on the HomeScreen
 *
 * @version 1.0
 * @see HomeScreen, HerokuConnection, ApplicationDatabase
 */

public class CollegeSelection extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView list;
    private HerokuConnection server;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.collegeSelectionToolbar);
        toolbar.setTitle("Select a college");
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.collegeList);//initialiie list

        String[] colleges = {"Galway-Mayo Institute of Technology (Galway)","Galway-Mayo Institute of Technology (Letterfrack)",
                "Galway-Mayo Institute of Technology (Castlebar)", "Athlone Institute of Technology (AIT)", "Dublin Institute of Technology (DIT)",
                "National University of Ireland Galway (NUIG)", "National University of Ireland Maynooth (NUIM)",
                "National College of Ireland (NCI)", "University College Dublin (UCD)", "Dublin City University (DCU)", "Trinity College Dublin (TCD)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, colleges);//converts an ArrayList of objects into View items loaded into the ListView container.

        list.setAdapter(adapter);//set adapter to list
        list.setOnItemClickListener(this);//set each item clickable
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        name = (String) list.getItemAtPosition(position);
        Toast.makeText(getApplicationContext(),
                "" + name, Toast.LENGTH_SHORT)
                .show();//on screen notification

        ApplicationDatabase ad = new ApplicationDatabase(getBaseContext(), name);//initialize database
        ad.createDatabase();

        Thread connect = new Thread(){
            public void run() {
                super.run();
                server = new HerokuConnection();//attempt a connection to heroku postgres database
            }
        };

        try {
            connect.start();//start thread
            connect.join();//forcing UI thread to wait untill connection is complete
        }catch (InterruptedException e){
            e.printStackTrace();
        }

//        Thread query = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                try {
//                    ResultSet rs = server.executeQuery("SELECT * FROM selection");
//                    ResultSetMetaData rsmd = rs.getMetaData();//all results from database query
//                    int columnsNumber = rsmd.getColumnCount();
//                    while (rs.next()) {
//                        for (int i = 1; i <= columnsNumber; i++) {
//                            if (i > 1)
//                                System.out.print(",  ");
//                            String columnValue = rs.getString(i);
//                            System.out.print(columnValue);
//                        }
//                        System.out.println("");
//                    }
//                    }catch(SQLException e){
//                        e.printStackTrace();
//                    }
//            }
//        };
//
//        try {
//            query.start();
//            query.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread image = new Thread() {
            @Override
            public void run() {
                super.run();
                server.getImagesFromDB(getApplicationContext(), name);//pull images from server with the name passed in
            }
        };

        try {
            image.start();
            image.join();//force threads to wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, HomeScreen.class));
        this.finish();//finish this activity
    }
}
