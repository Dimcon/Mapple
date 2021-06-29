package com.opsc7311.mapple.auth.data.model;

import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private String userId;
    private String displayName;
    static private FirebaseUser fbUser;
    public boolean isMetric = false;
    public List<String> favourites;
    public List<String> filtered;



    public static LoggedInUser getFromString(String s, FirebaseUser fbUser) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(s, LoggedInUser.class);
        } catch (Exception e) {
            System.out.println("Something went wrong during deserialization");
            return new LoggedInUser(fbUser);
        }
    }

    public String getString() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            return null;
        }
    }


    public LoggedInUser(FirebaseUser fbUser) {
        this.favourites = new ArrayList<>();
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

    public void afterLogin(ValueCallback<LoggedInUser> doOnDone) {
        DatabaseReference userRef = getUserRef();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild("settingsobj")){
                    String settings = snapshot.child("settingsobj").getValue(String.class);
                    LoggedInUser newUser = LoggedInUser.getFromString(
                            settings,
                            LoggedInUser.this.fbUser
                    );
                    doOnDone.onReceiveValue(newUser);
                } else {
                    LoggedInUser newUser = new LoggedInUser(
                            LoggedInUser.this.fbUser
                    );
                    doOnDone.onReceiveValue(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
//        save();
    }

    public void save() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FirebaseDatabase.getInstance().getReference("user")
                    .setValue(fbUser.getUid());
            FirebaseDatabase.getInstance().getReference("user")
                    .child(fbUser.getUid())
                    .setValue("settingsobj");
            String json = LoggedInUser.this.getString();
            FirebaseDatabase.getInstance().getReference("user")
                .child(fbUser.getUid())
                .child("settingsobj")
                .setValue(
                        json
                );
        }
    }

    public boolean isMetric() {
//        getUserRef().child("settings").child("isFreedomUnits")
        return this.isMetric;
    }
}