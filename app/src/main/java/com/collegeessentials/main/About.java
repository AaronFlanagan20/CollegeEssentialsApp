package com.collegeessentials.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * The About page is a small simple page explaining the apps tests features.
 * It also has a link to contact us with any queries regarding the app.
 *
 * @version 1.0
 */

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView contact = (TextView) findViewById(R.id.contactButton);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);//new intent asking android for a sending app, usually android gives options

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"aaronflanagan044@hotmail.com", "ciaranb1992@hotmail.com"});//attach emails
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CollegeEssential app query");//attach subject

                startActivity(emailIntent);//open email app
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);
    }

}
