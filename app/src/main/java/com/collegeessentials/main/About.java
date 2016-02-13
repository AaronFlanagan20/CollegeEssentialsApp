package com.collegeessentials.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class About extends AppCompatActivity {

    private TextView contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        contact = (TextView) findViewById(R.id.contactButton);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"aaronflanagan044@hotmail.com", "ciaranb1992@hotmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CollegeEssential app query");

                startActivity(emailIntent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);
    }

}
