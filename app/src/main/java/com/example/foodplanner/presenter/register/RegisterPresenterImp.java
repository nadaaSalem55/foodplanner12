package com.example.foodplanner.presenter.register;

import com.example.foodplanner.db.FirebaseUtils;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.view.auth.register.RegisterView;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenterImp implements RegisterPresenter{
    RegisterView registerView;
    private Disposable disposable;
    MealRepo mealRepo;

    public RegisterPresenterImp(RegisterView registerView,MealRepo mealRepo) {
        this.registerView = registerView;
        this.mealRepo=mealRepo;
    }

    @Override
    public void addAccountToFirebase(String email, String password) {
        FirebaseUtils.createAccount(email, password, task -> {
            if(task.isSuccessful()){
                registerView.showSuccessMessage();
            }
            else{
                registerView.showErrorMessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });
    }

    @Override
    public void signUpWithGoogle(String idToken) {
        disposable = mealRepo.signUpWithGoogle(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        authResult -> registerView.onGoogleSignUpSuccess(authResult.getUser()),
                        error -> registerView.onGoogleSignUpError(error.getMessage())
                );
    }
}

