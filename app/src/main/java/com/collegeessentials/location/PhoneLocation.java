package com.collegeessentials.location;

import android.content.Context;
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
    public static Location location;

    private int lat, lng;

    public PhoneLocation(Context context){
        this.context = context;
    }

    public void getPreviousLocations(){

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();//criteria for selecting best provider
        String provider = locationManager.getBestProvider(criteria, false);//return any provider whether enabled or not that meets the criteria
        try {
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, this);//receive updates
        }catch (SecurityException e){
            e.printStackTrace();
        }

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (int) (location.getLatitude());
        lng = (int) (location.getLongitude());
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
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}