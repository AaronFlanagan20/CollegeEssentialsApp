package com.collegeessentials.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * PhoneLocation checks the gps of the phone to get the longitude and latitude of the phone
 *
 * @version 1.0
 */
public class PhoneLocation implements LocationListener{

    public Context context;
    private Location fromLocation, toLocation;
    private LocationManager locationManager;

    private double lat, lng;

    public PhoneLocation(Context context){
        this.context = context;
    }

    public void getPreviousLocations(){

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();//criteria for selecting best provider
        String provider = locationManager.getBestProvider(criteria, false);//return any provider whether enabled or not that meets the criteria
        try {
            fromLocation = locationManager.getLastKnownLocation(provider);
            toLocation = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, this);//receive updates
        }catch (SecurityException e){
            e.printStackTrace();
        }

        // Initialize the fromLocation fields
        if (fromLocation != null) {
            onLocationChanged(fromLocation);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        lat =  (location.getLatitude());
        lng = (location.getLongitude());
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public String getLat(){
        String latitude = String.valueOf(lat);
        return latitude;
    }

    public String getLng(){
        String longtidue = String.valueOf(lng);
        return longtidue;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}