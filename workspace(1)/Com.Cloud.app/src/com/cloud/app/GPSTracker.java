package com.cloud.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSTracker extends Service implements LocationListener {

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private final Context mContext;
	boolean isGPSEnabled=false;
	boolean isNetworkEnabled=false;
	boolean canGetLocation=false;
	
	Location location;
	double longitude;
	double latitude;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=10;
	private static final long MIN_TIME_BW_UPDATES=1000*60*1;
	
	protected LocationManager locationManager;
	
	public GPSTracker(Context context)
	{
		this.mContext=context;
		getLocation();
	}
	
	
	public Location getLocation()
	{
		try
		{
				locationManager=(LocationManager)mContext.getSystemService(LOCATION_SERVICE);
				isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				//getting GPS status
				isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				//getting network status
				if(!isGPSEnabled && !isNetworkEnabled)
				{
				//No network Provider is enabled
				}
				else
				{
					this.canGetLocation=true;
					//Get location from Network Provider
					if(isNetworkEnabled)
					{
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,                    MIN_TIME_BW_UPDATES,
	                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if(locationManager!=null)
						{
							location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if(location!=null)
							{
								latitude=location.getLatitude();
								longitude=location.getLongitude();
							}
						}
					}
					
				if(isGPSEnabled)
				{
					if (location == null) 
					{
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        
                        if (locationManager != null) 
                        {
                        	location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) 
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
					}	
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return location;
	}

	//Function to get latitude
         public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     
    //Function to get longitude
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
    
    
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }      
    }
    
}
		

