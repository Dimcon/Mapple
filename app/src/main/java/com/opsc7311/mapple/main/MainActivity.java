package com.opsc7311.mapple.main;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
// Classes needed to initialize the map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
// Classes needed to handle location permissions
// Classes needed to add the location engine
// Classes needed to add the location component
import com.opsc7311.mapple.R;
import com.opsc7311.mapple.main.mymapbox.MapFeatures;
import com.opsc7311.mapple.main.mymapbox.MainMapbox;

import org.jetbrains.annotations.NotNull;

/**
 * Use the Mapbox Core Library to listen to device location updates
 */
public class MainActivity extends AppCompatActivity {
    public MapboxMap mapboxMap;
    private MapView mapView;
    private MainMapbox myMapBox;
    public MapFeatures mymapFeatures;

    public Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // Needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mymapFeatures = new MapFeatures(mapView, this);
        myMapBox = new MainMapbox(mapView, this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        myMapBox.permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMapBox.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
