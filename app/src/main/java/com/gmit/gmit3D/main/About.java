package com.gmit.gmit3D.main;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class About extends AppCompatActivity implements View.OnClickListener {

    private ImageButton facebookButton, githubButton;

    /*Runs when activity first loads*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*Look for button on screen by id and assign listener*/
        facebookButton = (ImageButton) findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(this);

        githubButton = (ImageButton) findViewById(R.id.githubButton);
        githubButton.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.assignmentToolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){//used to check for all buttons
            case R.id.facebookButton:
                openFacebookApp();
                break;
            case R.id.githubButton:
                openGithub();
                break;
        }
    }

    private Intent connectToFacebookApp(Context context) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/GMITOfficial")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            e.printStackTrace();
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/GMITOfficial")); //catches and opens a url to the desired page
        }
    }

    public void openFacebookApp() {
        Intent facebookIntent = connectToFacebookApp(this);
        startActivity(facebookIntent);
    }

    private void openGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AaronFlanagan20/GMIT3D"));
        startActivity(intent);
    }
}
