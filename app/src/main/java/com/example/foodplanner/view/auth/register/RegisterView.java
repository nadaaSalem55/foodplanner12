package com.example.foodplanner.view.auth.register;


import com.google.firebase.auth.FirebaseUser;

public interface RegisterView {
    void showSuccessMessage();
    void showErrorMessage(String error);

    void onGoogleSignUpSuccess(FirebaseUser user);

    void onGoogleSignUpError(String message);
}
