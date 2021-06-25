package com.opsc7311.mapple.auth.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.opsc7311.mapple.R;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    //The following code was taken from:
    //Author: Developers
    //Link: https://developer.android.com/topic/libraries/architecture/livedata
    MutableLiveData<Result<LoggedInUser>> loginResult = new MutableLiveData<>();
    MutableLiveData<Result<LoggedInUser>> registerResult = new MutableLiveData<>();
    MutableLiveData<Result<LoggedInUser>> logoutResult = new MutableLiveData<>();

    public MutableLiveData<Result<LoggedInUser>> login(String username, String password) {
        // Handle logging the user in from the auth service.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("user")
                                .child(user.getUid()).child("settingsobj").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                DataSnapshot snap = task.getResult();
                                String settings = (String) snap.getValue();
                                LoggedInUser myUser = LoggedInUser.getFromString(settings, user);
                                loginResult.setValue(new Result.Success<LoggedInUser>(myUser));
                            }
                        });
                    } else {
                        Result.Error err = new Result.Error(new IOException("Error logging in", task.getException()));
                        loginResult.setValue(err);
                    }
                });
        return loginResult;
    }

    public FirebaseUser isLoggedIn() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        return currentUser;
    }

    public MutableLiveData<Result<LoggedInUser>> register(String username, String password) {
        // Handle user registration.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        LoggedInUser myUser = new LoggedInUser(user);
                        registerResult.setValue(new Result.Success<LoggedInUser>(myUser));
                    } else {
                        Result.Error err = new Result.Error(new IOException("Error registering user", task.getException()));
                        registerResult.setValue(err);
                    }
                });
        return loginResult;
    }

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }
}