package com.gmit.gmit3D.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PhoneLocation implements LocationListener{

    public Context context;
    private LocationManager locationManager;
    public static Location location;
    private String provider;

    private int lat, lng;

    public PhoneLocation(Context context){
        this.context = context;
    }

    public void getPreviousLocations(){

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        try {
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, this);
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