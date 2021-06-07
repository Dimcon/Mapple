package com.opsc7311.mapple.auth.data.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private FirebaseUser fbUser;

    public LoggedInUser(FirebaseUser fbUser) {
        this.fbUser = fbUser;
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
}