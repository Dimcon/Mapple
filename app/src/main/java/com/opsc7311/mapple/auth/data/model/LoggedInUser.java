package com.opsc7311.mapple.auth.data.model;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opsc7311.mapple.PointOfInterest;
import com.opsc7311.mapple.R;

import static androidx.core.content.ContextCompat.startActivity;
import static com.google.api.ResourceProto.resource;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements AdapterView.OnItemClickListener {

    private String userId;
    private String displayName;
    private FirebaseUser fbUser;
    private String sLandmarks;
    private ListView savedLandmarks;
    public String SelectedFilter = "all";

    private String status;

    public LoggedInUser(FirebaseUser fbUser) {

        this.fbUser = fbUser;

        //The following code was taken from:
        //Author: YouTube/Code with Cal
        //Link: https://www.youtube.com/watch?v=M73Vec1oieM&list=WL&index=7
        public boolean onQueryTextChange (String s){
            ArrayList<String> SavedLandmarks = new ArrayList<String>();
            for (String landmark : SavedLandmarks) {
                if (landmark.getsavedLandmarks().toLowerCase().contains(s.toLowerCase())) {
                    SavedLandmarks.add(sLandmarks);
                }
                ArrayAdapter<String> LandmarkAdapter = new ArrayAdapter<String>(getApplicationContext(), resource: 0, savedLandmarks);
                ListView.setAdapter(LandmarkAdapter);
                savedLandmarks.setOnItemClickListener(this);
            }
        }
        private void filterList (String status){
            SelectedFilter = status;
            ArrayList<String> SavedLandmarks = new ArrayList<String>();
            for (String landmark : SavedLandmarks) {
                if (landmark.getsavedLandmark().toLowerCase().contains(status)) {
                    SavedLandmarks.add(sLandmarks);
                }
            }
            ArrayAdapter<String> LandmarkAdapter = new ArrayAdapter<String>(getApplicationContext(), resource: 0, savedLandmarks);
            ListView.setAdapter(LandmarkAdapter);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return fbUser.getEmail();
    }

    public DatabaseReference getUserRef() {
        return FirebaseDatabase.getInstance().getReference("user").child(fbUser.getUid());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String savedLandmarks = parent.getItemAtPosition(position).toString();
    }
}