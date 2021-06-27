package com.opsc7311.mapple.main;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;
// Classes needed to initialize the map
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
// Classes needed to handle location permissions
// Classes needed to add the location engine
// Classes needed to add the location component
import com.opsc7311.mapple.PointOfInterest;
import com.opsc7311.mapple.R;
import com.opsc7311.mapple.SettingsActivity;
import com.opsc7311.mapple.auth.data.LoginRepository;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;
import com.opsc7311.mapple.auth.ui.login.LoginActivity;
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
    public FloatingActionButton SettingsButton;
    public FloatingActionButton PoiButton;
    public FloatingActionButton BacktomeButton;
    public FloatingActionButton LogoutButton;
//    public LoginViewModel LVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        LoginRepository.getInstance().isLoggedIn(new ValueCallback<LoggedInUser>() {
            @Override
            public void onReceiveValue(LoggedInUser value) {

            }
        });
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // Needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        SettingsButton = findViewById(R.id.btnSetting);
        PoiButton = findViewById(R.id.btnSearch);
        BacktomeButton =findViewById(R.id.btnCurrentLocation);
        LogoutButton = findViewById(R.id.btnLogout);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mymapFeatures = new MapFeatures(mapView, this);
        myMapBox = new MainMapbox(mapView, this);
        SettingsButton.setOnClickListener(v -> {
            Intent switchToSettingsActivity = new Intent(MainActivity.this , SettingsActivity.class);
            startActivity(switchToSettingsActivity);
        });
        PoiButton.setOnClickListener(v -> {
            Intent switchToPoiActivity = new Intent(MainActivity.this , PointOfInterest.class);
            startActivity(switchToPoiActivity);
        });

        BacktomeButton.setOnClickListener(v -> {
            Intent switchToPoiActivity = new Intent(MainActivity.this , MainActivity.class);
            startActivity(switchToPoiActivity);
        });
        LogoutButton.setOnClickListener(v -> {
            LoginRepository.getInstance().logout();
            Intent switchToPoiActivity = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(switchToPoiActivity);
        });
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
