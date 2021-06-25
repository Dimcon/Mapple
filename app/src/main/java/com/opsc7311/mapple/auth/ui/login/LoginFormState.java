package com.opsc7311.mapple.auth.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */

//Following code was taken from:
//Author: Git/marcamsn
//Link: https://github.com/dhis2/dhis2-android-skeleton-app/blob/master/app/src/main/java/com/example/android/androidskeletonapp/ui/login/LoginFormState.java
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}