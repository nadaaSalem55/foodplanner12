package com.example.foodplanner.view.auth.register;

import static androidx.core.content.ContextCompat.getColor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodplanner.R;
import com.example.foodplanner.db.FirebaseUtils;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.register.RegisterPresenter;
import com.example.foodplanner.presenter.register.RegisterPresenterImp;
import com.example.foodplanner.view.AlertMessage;
import com.example.foodplanner.view.FragmentNavigator;
import com.example.foodplanner.view.auth.login.LoginFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment implements RegisterView{

   Button signUpBtn;
   EditText email,password,confirmPass;
   ImageView googleImg;
   RegisterPresenter registerPresenter;
   TextView haveAccountText;
   ProgressDialog progressDialog;
    TextInputLayout inputLayoutEmail,inputLayoutPass,inputLayoutConfirmPass;
    GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;

    public RegisterFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    private void signUp() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, FirebaseUtils.RC_SIGN_IN);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerPresenter=new RegisterPresenterImp(this,
                new MealRepoImp(new RandomMealRemoteDataSourceImp(),
                        new MealLocalDatasource.MealLocalDataSourceImp(requireContext()),
                        new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())));
        initViews(view);

    }

    private void initViews(View view) {
        googleImg=view.findViewById(R.id.google_img);
        googleImg.setOnClickListener(v -> {
                signUp();
        });
        progressDialog=new ProgressDialog(this.getContext());
        inputLayoutEmail =view.findViewById(R.id.email_input_layout);
        inputLayoutPass=view.findViewById(R.id.pass_input_layout);
        inputLayoutConfirmPass=view.findViewById(R.id.confirm_pass_input_layout);
        signUpBtn=view.findViewById(R.id.signup_btn);
        email=view.findViewById(R.id.email_et);
        password=view.findViewById(R.id.pass_et);
        confirmPass=view.findViewById(R.id.confirm_pass_et);
        haveAccountText=view.findViewById(R.id.have_account_tv);
        haveAccountText.setOnClickListener(view1 -> navigateToLoginFragment());
        signUpBtn.setOnClickListener(view1 -> {
            if(validateFields()){
                addAccountToFirebase();
                showProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FirebaseUtils.RC_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> signedInAccountFromIntent) {
        try {
            GoogleSignInAccount account = signedInAccountFromIntent.getResult(ApiException.class);
            registerPresenter.signUpWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Toast.makeText(requireContext(), "Sign up failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToLoginFragment() {
        FragmentNavigator.addFragment(new LoginFragment(),this.requireActivity(),R.id.fragment_container);
    }

    private void addAccountToFirebase() {
        registerPresenter.addAccountToFirebase(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void showSuccessMessage() {
        AlertMessage.showToastMessage("Success",getContext());
        hideProgressDialog();
        navigateToLoginFragment();
    }

    @Override
    public void showErrorMessage(String error) {
        AlertMessage.showToastMessage(error,getContext());
        hideProgressDialog();

    }

    @Override
    public void onGoogleSignUpSuccess(FirebaseUser user) {
        AlertMessage.showToastMessage("Success",getContext());
        hideProgressDialog();
        navigateToLoginFragment();
    }

    @Override
    public void onGoogleSignUpError(String message) {
        AlertMessage.showToastMessage(message,getContext());
        hideProgressDialog();
    }

    void showProgressDialog() {
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }
    boolean validateFields(){
        boolean isValid=true;
        if(email.getText().toString().isEmpty()){
            showError("Please Enter The Email",inputLayoutEmail);
        }
        else {
            showError(null,inputLayoutEmail);
        }
        if(password.getText().toString().isEmpty()){
            showError("Please Enter The Password",inputLayoutPass);
            isValid=false;
        }
        else {
            showError(null,inputLayoutPass);
        }
        if(confirmPass.getText().toString().isEmpty()){
            showError("Please Enter The Password",inputLayoutConfirmPass);
            isValid=false;
        }
        else {
            if(!confirmPass.getText().toString().equals(password.getText().toString())){
                showError("The Password didn't match",inputLayoutConfirmPass);
                isValid=false;
            }
            else {
                showError(null,inputLayoutConfirmPass);
            }
        }

        return isValid;
    }

    private void showError(String errorMessage, TextInputLayout inputLayout) {
        inputLayout.setError(errorMessage);
        inputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(getColor(this.requireContext(), R.color.red)));
    }

}