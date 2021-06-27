package com.opsc7311.mapple.auth.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.webkit.ValueCallback;

import com.google.firebase.database.ValueEventListener;
import com.opsc7311.mapple.auth.data.LoginRepository;
import com.opsc7311.mapple.auth.data.Result;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;
import com.opsc7311.mapple.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public boolean isLoggedIn(ValueCallback<LoggedInUser> doOnDone) {
        return loginRepository.isLoggedIn(doOnDone);
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        LiveData<Result<LoggedInUser>> LDResult = loginRepository.login(username, password);
        LDResult.observeForever(new Observer<Result<LoggedInUser>>() {
            @Override
            public void onChanged(Result<LoggedInUser> loggedInUserResult) {
                if (loggedInUserResult instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) loggedInUserResult).getData();
                    loginResult.setValue(new LoginResult(data));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });
    }


    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        LiveData<Result<LoggedInUser>> LDResult = loginRepository.register(username, password);
        LDResult.observeForever(new Observer<Result<LoggedInUser>>() {
            @Override
            public void onChanged(Result<LoggedInUser> loggedInUserResult) {
                if (loggedInUserResult instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) loggedInUserResult).getData();
                    loginResult.setValue(new LoginResult(data));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}