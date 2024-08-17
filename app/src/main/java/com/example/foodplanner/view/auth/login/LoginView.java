package com.example.foodplanner.view.auth.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginView {
    void showSuccessMessage();
    void showErrorMessage(String error);

    void showSuccessGoogleAuthMessage(FirebaseUser user);

    void showFailGoogleAuthMessage(String localizedMessage);
}
