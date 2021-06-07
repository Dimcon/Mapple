package com.opsc7311.mapple.auth.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

import com.opsc7311.mapple.auth.data.LoginRepository;
import com.opsc7311.mapple.auth.data.Result;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;
import com.opsc7311.mapple.R;

public class LoginViewModel extends ViewModel implements Parcelable {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    protected LoginViewModel(Parcel in) {
    }

    public static final Creator<LoginViewModel> CREATOR = new Creator<LoginViewModel>() {
        @Override
        public LoginViewModel createFromParcel(Parcel in) {
            return new LoginViewModel(in);
        }

        @Override
        public LoginViewModel[] newArray(int size) {
            return new LoginViewModel[size];
        }
    };

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public LoggedInUser isLoggedIn() {
        return loginRepository.isLoggedIn();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}