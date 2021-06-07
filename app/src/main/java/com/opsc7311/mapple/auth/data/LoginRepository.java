package com.opsc7311.mapple.auth.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;

import org.jetbrains.annotations.NotNull;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository  {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public LoggedInUser isLoggedIn() {
        FirebaseUser userT = dataSource.isLoggedIn();
        if (userT != null) {
            this.user = new LoggedInUser(userT);
            return this.user;
        }
        return null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


    //The following code was taken from:
    //Author: raywenderlich/Prateek Sharma
    //link: https://www.raywenderlich.com/10391019-livedata-tutorial-for-android-deep-dive
    public LiveData<Result<LoggedInUser>> login(String username, String password) {
        // handle login
        LiveData<Result<LoggedInUser>> result = dataSource.login(username, password);
        result.observeForever(new Observer<Result<LoggedInUser>>() {
            @Override
            public void onChanged(Result<LoggedInUser> loggedInUserResult) {
                if (loggedInUserResult instanceof Result.Success) {
                    setLoggedInUser(((Result.Success<LoggedInUser>) loggedInUserResult).getData());
                }
            }
        });
        return result;
    }

    public LiveData<Result<LoggedInUser>> register(String username, String password) {
        // handle login
        LiveData<Result<LoggedInUser>> result = dataSource.register(username, password);
        result.observeForever(new Observer<Result<LoggedInUser>>() {
            @Override
            public void onChanged(Result<LoggedInUser> loggedInUserResult) {
                if (loggedInUserResult instanceof Result.Success) {
                    setLoggedInUser(((Result.Success<LoggedInUser>) loggedInUserResult).getData());
                }
            }
        });
        return result;
    }

}