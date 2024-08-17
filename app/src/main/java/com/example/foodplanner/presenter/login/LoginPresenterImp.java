package com.example.foodplanner.presenter.login;

import android.content.Intent;

import com.example.foodplanner.Constants;
import com.example.foodplanner.db.FirebaseUtils;
import com.example.foodplanner.model.repo.MealRepo;
import com.example.foodplanner.view.auth.login.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenterImp implements LoginPresenter{
    LoginView loginView;
    MealRepo mealRepo;
 private Disposable disposable;

    public LoginPresenterImp(LoginView loginView,MealRepo mealRepo) {
        this.loginView = loginView;
       // compositeDisposable=new CompositeDisposable();
        this.mealRepo=mealRepo;
    }

    @Override
    public void signInWithFirebaseAuth(String email, String password) {
        FirebaseUtils.signIn(email, password, task -> {
            if(task.isSuccessful()){
                Constants.CURRENT_USER= FirebaseUtils.getFirebaseInstance().getCurrentUser();
                loginView.showSuccessMessage();
            }
            else{
                loginView.showErrorMessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });
    }

   /* @Override
    public void signInWithGoogle(Activity activity) {
        disposable=mealRepo.signInWithGoogle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(user->loginView.
                        showSuccessGoogleAuthMessage(user),
                        error->loginView.showFailGoogleAuthMessage(error.getLocalizedMessage()));
    }*/


    @Override
    public void handleGoogleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            loginView.showFailGoogleAuthMessage(e.getMessage());
        }
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
         mealRepo.signInWithGoogle(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> loginView.showSuccessGoogleAuthMessage(user),
                        throwable -> loginView.showFailGoogleAuthMessage(throwable.getMessage()));
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseUtils.getFirebaseInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseUtils.getFirebaseInstance().getCurrentUser();
                        loginView.showSuccessGoogleAuthMessage(user);
                    } else {
                        loginView.showFailGoogleAuthMessage(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
}
