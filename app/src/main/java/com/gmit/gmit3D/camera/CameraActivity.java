package com.gmit.gmit3D.camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gmit.gmit3D.gps.PhoneLocation;
import com.gmit.gmit3D.main.R;

public class CameraActivity extends Activity {

    private Camera mCamera;
    private Preview mPreview;
    private static final int number = 1337;

    private PhoneLocation location;
    private LocationManager locationManager;
    private Location loc;
    public final static int REQUEST_CODE_A = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkGPSIsEnabled();

        location = new PhoneLocation(this);
        location.getPreviousLocations();

        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkGPSIsEnabled();
    }

}
