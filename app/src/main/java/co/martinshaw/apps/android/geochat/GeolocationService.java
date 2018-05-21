/*
 * GeolocationFunctionality
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/GeolocationFunctionality.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 21/05/18 10:51 <martin>
 * Last Compilation: 21/05/18 10:51
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class GeolocationService extends Service {

    private final String TAG = GeolocationService.class.getSimpleName();

    private final int TWO_MINUTES = 1000 * 60 * 2;
    int time_interval = 2000;

    private LocationManager mLocationManager;

    private MyLocationListener mLocationListeners[] = new MyLocationListener[]{
            new MyLocationListener(LocationManager.NETWORK_PROVIDER),
            new MyLocationListener(LocationManager.GPS_PROVIDER)
    };

    private Location mFinalLocation;

    private class MyLocationListener implements LocationListener {
        private String mProvider;

        public MyLocationListener(String provider) {
            Log.d(TAG, "LocationListener : " + provider);
            mProvider = provider;
        }

        public String getProvider() {
            return mProvider;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged : " + location);

            if (isBetterLocation(location, mFinalLocation)) {
                Log.d(TAG, "Setting current Final Location to recent most Location for Provider : " + location.getProvider());
                Log.d(TAG, "Setting current Final Location to : " + location);
                mFinalLocation = location;
            } else {
                Log.d(TAG, "Keeping current Final Location to previous Final Location");
            }

            Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged provider " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled provider " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled provider " + provider);
        }
    }

    private Handler mStopServiceHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    stopSelf();
                }
                break;
            }
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        requestLocation();
        mStopServiceHandler.sendEmptyMessageDelayed(1, TWO_MINUTES);
    }

    private void requestLocation() {
        // Acquire a reference to the system Location Manager
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d(TAG, "Fetching Cached Location for Provider : " + LocationManager.NETWORK_PROVIDER);
                Location cachedNetworkLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (cachedNetworkLocation != null) {
                    Log.d(TAG, "Setting Final Location to Cached Location for Provider : " + LocationManager.NETWORK_PROVIDER);
                    Log.d(TAG, "Setting Final Location to : " + cachedNetworkLocation);
                    mFinalLocation = cachedNetworkLocation;
                } else {
                    Log.d(TAG, "Cached Location for Provider : " + LocationManager.NETWORK_PROVIDER + " is NULL");
                }

                Log.d(TAG, "Requesting Location Update for Provider : " + LocationManager.NETWORK_PROVIDER);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time_interval, 0, mLocationListeners[0]);
            }

        } catch (SecurityException se) {
            Log.e(TAG, se.getMessage(), se);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, iae.getMessage(), iae);
        }

        try {
            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d(TAG, "Fetching Cached Location for Provider : " + LocationManager.GPS_PROVIDER);
                Location cachedGPSLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (cachedGPSLocation != null) {
                    if (isBetterLocation(cachedGPSLocation, mFinalLocation)) {
                        Log.d(TAG, "Setting Final Location to Cached Location for Provider : " + LocationManager.GPS_PROVIDER);
                        Log.d(TAG, "Setting Final Location to : " + cachedGPSLocation);
                        mFinalLocation = cachedGPSLocation;
                    }
                } else {
                    Log.d(TAG, "Cached Location for Provider : " + LocationManager.GPS_PROVIDER + " is NULL");
                }

                Log.d(TAG, "Requesting Location Update for Provider : " + LocationManager.GPS_PROVIDER);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time_interval, 0, mLocationListeners[1]);
            }

        } catch (SecurityException se) {
            Log.e(TAG, se.getMessage(), se);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, iae.getMessage(), iae);
        }


    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    Log.d(TAG, "Removing Location Update for Provider : " + mLocationListeners[i].getProvider());
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.e(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }
}