package com.opsc7311.mapple.services;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class StorageService {

    static FirebaseDatabase database;
    private static FirebaseAuth mAuth;
    private static String TAG = "STORAGE";
    private static FirebaseUser fbUser;
    public static boolean isMetric;

    public static void setUser(FirebaseUser userP, final Context ctx) {
        fbUser = userP;
        getUserRef().child("profile").child("units").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isMetric = dataSnapshot.getValue().equals("metric");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // The following was taken from Firebase Realtime Database
// Author: Firebase Realtime Database
//Link: https://firebase.google.com/docs/database/android/read-and-write
    }

    public static FirebaseAuth getFBAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static long getDatetimeLong() {
        return new Date().getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String formatDateFromEpoch(long datep) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(datep);
        String pattern = "h:mma, EEEE, MMM d, yyyy";
        SimpleDateFormat format1 = new SimpleDateFormat(pattern);
        String formatted = format1.format(c.getTime());
        return formatted;
    }

    public static FirebaseUser getUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }


    //The following code was taken from:
    //Author: Androiodhive
    //Link: https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
    public static DatabaseReference getUserReference() {
        return StorageService.getFirestore().getReference("user").child(fbUser.getUid());
    }


    public static FirebaseDatabase getFirestore() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }
    public static DatabaseReference getUserRef() {
        return getFirestore().getReference("user").child(fbUser.getUid());
    }
}
