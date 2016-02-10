package com.gmit.gmit3D.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gmit.gmit3D.database.HerokuConnection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CollegeSelection extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView list;
    private HerokuConnection db;
    private boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.collegeSelectionToolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.collegeList);

        String[] colleges = {"Galway-Mayo Institute of Technology (Galway)","Galway-Mayo Institute of Technology (Letterfrack)",
                "Galway-Mayo Institute of Technology (Castlebar)", "Athlone Institute of Technology (AIT)", "Dublin Institute of Technology (DIT)",
                "National University of Ireland Galway (NUIG)", "National University of Ireland Maynooth (NUIM)",
                "National College of Ireland (NCI)", "University College Dublin (UCD)", "Dublin City University (DCU)", "Trinity College Dublin (TCD)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, colleges);

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String itemValue = (String) list.getItemAtPosition(position);
        Toast.makeText(getApplicationContext(),
                "" + itemValue, Toast.LENGTH_SHORT)
                .show();
        //progressbar
        Thread connect = new Thread(){
            public void run() {
                super.run();
                db = new HerokuConnection();
            }
        };

        try {
            connect.start();
            connect.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        Thread query = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    ResultSet rs = db.executeQuery("SELECT * FROM selection");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    while (rs.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1)
                                System.out.print(",  ");
                            String columnValue = rs.getString(i);
                            System.out.print(columnValue);
                        }
                        System.out.println("");
                    }
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

            }
        };

        try {
            query.start();
            query.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, HomeScreen.class));
        this.finish();

    }
}
