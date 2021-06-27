package com.opsc7311.mapple.auth.data;

import android.webkit.ValueCallback;

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

    private LoggedInUser user;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;

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

    public static LoginRepository getInstance() {
        if (instance == null) {
            System.out.println("User: Not logged in");
        }
        return instance;
    }

    public boolean isLoggedIn(ValueCallback<LoggedInUser> doOnDone) {
        if (user != null) {
            doOnDone.onReceiveValue(user);
            return true;
        }
        FirebaseUser userT = dataSource.isLoggedIn();
        if (userT != null) {
            LoggedInUser tmp = new LoggedInUser(userT);
            tmp.afterLogin(value -> {
                MyData.myUser = value;
                user = value;
                doOnDone.onReceiveValue(value);
            });
            return true;
        }
        return false;
    }

    public void logout() {
        MyData.myUser = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        MyData.myUser = user;
//        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }


    //The following code was taken from:
    //Author: raywenderlich/Prateek Sharma
    //link: https://www.raywenderlich.com/10391019-livedata-tutorial-for-android-deep-dive
    public LiveData<Result<LoggedInUser>> login(String username, String password) {
        // handle login
        LiveData<Result<LoggedInUser>> result = dataSource.login(username, password);
        result.observeForever(loggedInUserResult -> {
            if (loggedInUserResult instanceof Result.Success) {
                setLoggedInUser(((Result.Success<LoggedInUser>) loggedInUserResult).getData());
            }
        });
        return result;
    }

    public LiveData<Result<LoggedInUser>> register(String username, String password) {
        // handle login
        LiveData<Result<LoggedInUser>> result = dataSource.register(username, password);
        result.observeForever(loggedInUserResult -> {
            if (loggedInUserResult instanceof Result.Success) {
                setLoggedInUser(((Result.Success<LoggedInUser>) loggedInUserResult).getData());
            }
        });
        return result;
    }

}