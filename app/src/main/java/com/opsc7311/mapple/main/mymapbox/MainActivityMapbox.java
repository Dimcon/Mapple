package com.opsc7311.mapple.main.mymapbox;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.opsc7311.mapple.R;
import com.opsc7311.mapple.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivityMapbox implements
        PermissionsListener, OnMapReadyCallback {

    private LocationEngine locationEngine;
    public MapboxMap mapboxMap;
    private MainActivityLocationCallback myLocationCallback;
    private MapView mapView;
    private MainActivity mainActivity;
    public PermissionsManager permissionsManager;


    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    public MainActivityMapbox(MapView mapView, MainActivity mainActivity) {
        this.myLocationCallback = new MainActivityLocationCallback(mainActivity);
        this.mapView = mapView;
        this.mainActivity = mainActivity;
        mapView.getMapAsync(this);
    }

    public void onDestroy() {
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(myLocationCallback);
        }
        mapView.onDestroy();
    }


    @Override
    public void onMapReady(@NonNull @NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                this::enableLocationComponent);
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        mainActivity.mymapFeatures.onMapReadyAndStyleLoaded(this.mapboxMap, loadedMapStyle);
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(mainActivity)) {
            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            LocationComponentOptions locationComponentOptions =
                    LocationComponentOptions.builder(mainActivity)
                            .pulseEnabled(true)
                            .pulseColor(Color.GREEN)
                            .pulseAlpha(.4f)
                            .pulseInterpolator(new BounceInterpolator())
                            .build();
            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(mainActivity, loadedMapStyle)
                            .locationComponentOptions(locationComponentOptions)
                            .build();


            locationComponent.activateLocationComponent(
                    locationComponentActivationOptions
            );

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(mainActivity);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(mainActivity);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, myLocationCallback, mainActivity.getMainLooper());
        locationEngine.getLastLocation(myLocationCallback);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(mainActivity, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(mainActivity, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            mainActivity.finish();
        }
    }
}
