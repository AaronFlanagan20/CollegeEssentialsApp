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
import com.collegeessentials.main.R;

public class CameraActivity extends Activity {

    private CameraPreview mCameraPreview;
    private DrawView drawView;
    FrameLayout alParent;

    private PhoneLocation location;
    private LocationManager locationManager;
    private Location loc;
    public final static int REQUEST_CODE_A = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkGPSIsEnabled();

        location = new PhoneLocation(this);
        location.getPreviousLocations();

        /* Set the screen orientation to landscape, because
         * the camera preview will be in landscape, and if we
         * don't do this, then we will get a streached image.*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

    public void Load(){
        Camera c = getCameraInstance();

        if (c != null){

            alParent = new FrameLayout(this);
            alParent.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));

            // Create a new camera view and add it to the layout
            mCameraPreview = new CameraPreview(this,c);
            alParent.addView(mCameraPreview);

            // Create a new draw view and add it to the layout
            drawView = new DrawView(this);
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

        Load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkGPSIsEnabled();
    }

}
