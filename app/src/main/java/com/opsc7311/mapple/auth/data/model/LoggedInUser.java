package com.opsc7311.mapple.auth.data.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private String userId;
    private String displayName;
    private FirebaseUser fbUser;
    public boolean isMetric = false;

    public static LoggedInUser getFromString(String s, FirebaseUser fbAuth) {
        LoggedInUser tmp;
        try {
            InputStream targetStream = new ByteArrayInputStream(s.getBytes());
            ObjectInputStream in = new ObjectInputStream(targetStream);
            tmp = (LoggedInUser) in.readObject();
            in.close();
            return tmp;
        } catch (Exception e) {
            System.out.println("You f'ed up dude...");
            return new LoggedInUser(fbAuth);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getString() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( this );
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
        }
        return null;
    }


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save() {
        FirebaseDatabase.getInstance().getReference("user").child(fbUser.getUid())
                .child("settingsobj").setValue(
            this.getString()
        );
    }

    public boolean isMetric() {
//        getUserRef().child("settings").child("isFreedomUnits")
        return true;
    }
}