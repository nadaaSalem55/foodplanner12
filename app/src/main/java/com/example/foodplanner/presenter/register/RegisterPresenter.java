package com.example.foodplanner.presenter.register;

public interface RegisterPresenter {
    void addAccountToFirebase(String email, String password);
    void signUpWithGoogle(String idToken);
}
