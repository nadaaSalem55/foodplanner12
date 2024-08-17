package com.example.foodplanner.presenter.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginPresenter {
    void signInWithFirebaseAuth(String email,String password);
   //  void signInWithGoogle(Activity activity);
    void handleGoogleSignInResult(Intent data);
     void signInWithGoogle(GoogleSignInAccount account);
}
