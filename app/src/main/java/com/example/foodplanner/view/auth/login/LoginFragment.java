package com.example.foodplanner.view.auth.login;

import static androidx.core.content.ContextCompat.getColor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodplanner.Constants;
import com.example.foodplanner.R;
import com.example.foodplanner.db.FirebaseUtils;
import com.example.foodplanner.db.SharedPreferencesManager;
import com.example.foodplanner.model.repo.MealRepoImp;
import com.example.foodplanner.model.repo.local.MealLocalDatasource;
import com.example.foodplanner.model.repo.remote.MealRemoteDataSource;
import com.example.foodplanner.model.repo.remote.RandomMealRemoteDataSourceImp;
import com.example.foodplanner.presenter.login.LoginPresenter;
import com.example.foodplanner.presenter.login.LoginPresenterImp;
import com.example.foodplanner.view.AlertMessage;
import com.example.foodplanner.view.FragmentNavigator;
import com.example.foodplanner.view.activity.HomeActivity;
import com.example.foodplanner.view.auth.register.RegisterFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment implements LoginView {

    Button signInBtn;
    EditText email, password;
    ImageView googleImg;
    TextView haveAccountText,guestTv;
    LoginPresenter loginPresenter;
    ProgressDialog progressDialog;
    TextInputLayout inputLayoutEmail,inputLayoutPass;
    private GoogleSignInClient googleSignInClient;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        loginPresenter = new LoginPresenterImp(this,new MealRepoImp(new RandomMealRemoteDataSourceImp(),
                new MealLocalDatasource.MealLocalDataSourceImp(requireContext()),
                new MealRemoteDataSource.MealRemoteDataSourceImp(requireContext())));
        initViews(view);

    }

    private void signIn() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, FirebaseUtils.RC_SIGN_IN);
       // SharedPreferencesManager.saveUserEmail(requireContext(),emailText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FirebaseUtils.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginPresenter.signInWithGoogle(account);
                SharedPreferencesManager.saveUserEmail(requireContext(),account.getEmail());

            } catch (ApiException e) {
                showErrorMessage("Google sign in failed");
            }
        }
    }

    private void initViews(View view) {
        googleImg=view.findViewById(R.id.google_img);
        googleImg.setOnClickListener(v -> {
            showProgressDialog();
            signIn();
        });
        guestTv=view.findViewById(R.id.guest_tv);
        guestTv.setOnClickListener(v -> {
            SharedPreferencesManager.saveUserEmail(requireContext(),Constants.GUEST);
            startHomeActivity();
        });
        inputLayoutEmail =view.findViewById(R.id.email_input_layout);
        inputLayoutPass=view.findViewById(R.id.pass_input_layout);
        progressDialog = new ProgressDialog(this.getContext());
        signInBtn = view.findViewById(R.id.sign_in_btn);
        email = view.findViewById(R.id.email_et);
        password = view.findViewById(R.id.pass_et);
        haveAccountText = view.findViewById(R.id.have_account_tv);
        haveAccountText.setOnClickListener(view1 -> navigateToRegisterFragment());
        signInBtn.setOnClickListener(view1 ->
        {
            if(validateFields()) {
                loginWithFirebaseAuth();
                showProgressDialog();
            }
        });
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

    private void navigateToRegisterFragment() {
        FragmentNavigator.addFragment(new RegisterFragment(), this.requireActivity(), R.id.fragment_container);
    }

    private void loginWithFirebaseAuth() {
        loginPresenter.signInWithFirebaseAuth(email.getText().toString(), password.getText().toString());
        String emailText = email.getText().toString();
        SharedPreferencesManager.saveUserEmail(requireContext(),emailText);
    }

    @Override
    public void showSuccessMessage() {

        Log.e("TAG", "show: "+Constants.EMAIL );
        AlertMessage.showToastMessage("Sign in Successfully", this.getContext());
        hideProgressDialog();
        startHomeActivity();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this.getContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(String error) {
        AlertMessage.showToastMessage(error, this.getContext());
        hideProgressDialog();
    }

    @Override
    public void showSuccessGoogleAuthMessage(FirebaseUser user) {
        AlertMessage.showToastMessage("Sign in Successfully", this.getContext());
        hideProgressDialog();
        startHomeActivity();
    }


    @Override
    public void showFailGoogleAuthMessage(String localizedMessage) {
        AlertMessage.showToastMessage(localizedMessage, this.getContext());
        hideProgressDialog();
    }

    boolean validateFields(){
        boolean isValid=true;
        if(email.getText().toString().isEmpty()){
            showInputLayoutError("Please Enter The Email", inputLayoutEmail);
        }
        else {
            showInputLayoutError(null, inputLayoutEmail);
        }
        if(password.getText().toString().isEmpty()){
            showInputLayoutError("Please Enter The Password",inputLayoutPass);
            isValid=false;
        }
        else {
            showInputLayoutError(null,inputLayoutPass);
        }
        return isValid;
    }

    private void showInputLayoutError(String errorMessage, TextInputLayout inputLayout) {
        inputLayout.setError(errorMessage);
        inputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(getColor(this.requireContext(), R.color.red)));

    }
}