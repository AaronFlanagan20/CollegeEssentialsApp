package com.gmit.gmit3D.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.gmit.gmit3D.main.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float last_x, last_y, last_z;

    long lastUpdate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setUpMapIfNeeded();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //setup the accelerometer
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



    }

    public void onSensorChanged(SensorEvent event){
        Sensor mysensor = event.sensor;
        if(mysensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0]; //xvalue
            float y = event.values[1]; //yvalue
            float z = event.values[2]; //zvalue

            long curTime = System.currentTimeMillis();

            if(Math.abs(curTime - lastUpdate) > 2000){
                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                String currentDateTime = date.format(new Date());

                lastUpdate = curTime;

                if (Math.abs(last_x - x) > 10){
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(37.23062, -80.42178))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title("Hey you move the x axis on " + currentDateTime));
                }

                if (Math.abs(last_y - y) > 10){
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(37.263062, -80.42188))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title("Hey you move the y axis on " + currentDateTime));
                }

                if (Math.abs(last_z - z) > 10){
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(37.24062, -80.43178))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Hey you move the z axis on " + currentDateTime));
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }

        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    protected void onResume (){
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void setUpMapIfNeeded(){
        if(mMap == null){
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if(mMap != null){

                setUpMap();
            }
        }

    }

    private void setUpMap(){
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.229, -80.424)).title("Virginia Tech"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.229, -80.42), 14.9f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng GMIT = new LatLng(53.277189, -9.010039);
        mMap.addMarker(new MarkerOptions().position(GMIT).title("GMIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(GMIT));

        LatLng bus = new LatLng(53.2734576, -9.0458391);
        mMap.addMarker(new MarkerOptions().position(bus).title("Bus and Train station"));

        LatLng dunnes = new LatLng(53.2739132, -9.0515865);
        mMap.addMarker(new MarkerOptions().position(dunnes).title("Dunne's stores"));

        LatLng eye = new LatLng(53.2813322, -9.0334867);
        mMap.addMarker(new MarkerOptions().position(eye).title("Eye cinema"));
    }
}
