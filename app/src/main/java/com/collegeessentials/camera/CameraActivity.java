package com.collegeessentials.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import android.widget.FrameLayout;

import com.collegeessentials.location.PhoneLocation;

/**
 * Our tests camera activity.
 * This class accesses the phones tests camera and create a CameraPreview object to sit on top.
 *
 * @version 1.0
 * @see CameraPreview
 */

public class CameraActivity extends Activity {

    private CameraPreview mCameraPreview;
    FrameLayout alParent;

    private LocationManager locationManager;
    public final static int REQUEST_CODE_A = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkGPSIsEnabled();

        PhoneLocation location = new PhoneLocation(this);//create object
        location.getPreviousLocations();//intialize locationManager

        locationManager = location.getLocationManager();//return location manager

        /* Set the screen orientation to landscape, because
         * the camera preview will be in landscape, and if we
         * don't do this, then we will get a streached image.*/
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // requesting to turn the title OFF
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void load(){
        Camera c = getCameraInstance();

        if (c != null){

            alParent = new FrameLayout(this);//create parent layout
            alParent.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));//fill whole screen

            // Create a new camera view and add it to the layout
            mCameraPreview = new CameraPreview(this,c);
            alParent.addView(mCameraPreview);

            // Create a new draw view and add it to the layout
            DrawView drawView = new DrawView(this);
            alParent.addView(drawView);

            // Set the layout as the apps content view
            setContentView(alParent);
        }
        // If the camera was not received, close the app
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Unable to find camera. Closing.", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    /* Checks if the phone has it's location setting on, location must be on to use camera */
    //:TODO Runtime permission instead
    public boolean checkGPSIsEnabled(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.putExtra("GPS", enabled);
            startActivityForResult(intent, REQUEST_CODE_A);
        }

        return enabled;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraPreview != null){
            mCameraPreview.onPause();
            mCameraPreview = null;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkGPSIsEnabled();
    }

}
