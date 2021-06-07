package com.opsc7311.mapple.main.mymapbox;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.opsc7311.mapple.main.MainActivity;

import java.lang.ref.WeakReference;

public class MapLocationCallback implements LocationEngineCallback<LocationEngineResult> {

    private final WeakReference<MainActivity> activityWeakReference;
    private boolean didthething = false;

    MapLocationCallback(MainActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    /**
     * The LocationEngineCallback interface's method which fires when the device's location has changed.
     *
     * @param result the LocationEngineResult object which has the last known location within it.
     */
    @Override
    public void onSuccess(LocationEngineResult result) {
        MainActivity activity = activityWeakReference.get();

        if (activity != null) {
            Location location = result.getLastLocation();

            if (location == null) {
                return;
            }
            activity.lastLocation = location;
            if (!didthething) {
                new LoadGeoJsonDataTask(activity.mymapFeatures).execute();
                didthething = true;
            }

//            Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
//                    String.valueOf(result.getLastLocation().getLatitude()),
//                    String.valueOf(result.getLastLocation().getLongitude())),
//                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The LocationEngineCallback interface's method which fires when the device's location can not be captured
     *
     * @param exception the exception message
     */
    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.d("LocationChangeActivity", exception.getLocalizedMessage());
        MainActivity activity = activityWeakReference.get();
        if (activity != null) {
            Toast.makeText(activity, exception.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}