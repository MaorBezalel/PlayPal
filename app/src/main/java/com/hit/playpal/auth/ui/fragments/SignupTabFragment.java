package com.hit.playpal.auth.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.validations.AuthValidations;
import com.hit.playpal.auth.ui.viewmodels.AuthViewModel;

public class SignupTabFragment extends Fragment {

    private AuthViewModel mAuthViewModel;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mUsernameTextInputLayout;
    private TextInputLayout mDisplayNameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_signup_tab, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);
        initAndSubscribeToViewModels();
        initViews(iView);
        setListeners(iView);
    }

    public void handleSignupButtonClick(View iView) {
        String email = mEmailTextInputLayout.getEditText().getText().toString().trim().toLowerCase();
        String username = mUsernameTextInputLayout.getEditText().getText().toString().trim().toLowerCase();
        String displayName = mDisplayNameTextInputLayout.getEditText().getText().toString().trim();
        String password = mPasswordTextInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = mConfirmPasswordTextInputLayout.getEditText().getText().toString().trim();
        boolean isValid = performInputValidation(email, username, displayName, password);

        if (isValid) {
            mAuthViewModel.signup(email, username, displayName, password);
            //mAuthViewModel.nestedSignup(email, username, displayName, password);
        }
    }

    private void initAndSubscribeToViewModels() {
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        mAuthViewModel.getSignupFailure().observe(getViewLifecycleOwner(), signupFailure -> {
            if (signupFailure!= null) {
                switch (signupFailure) {
                    case EMAIL_ALREADY_TAKEN:
                        mEmailTextInputLayout.setError("Email already taken");
                        break;
                    case USERNAME_ALREADY_TAKEN:
                        mUsernameTextInputLayout.setError("Username already taken");
                        break;
                    default:
                        Toast.makeText(requireContext(), "Signup failed due to an unknown error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void initViews(@NonNull View iView) {
        mEmailTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_email);
        mUsernameTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_username);
        mDisplayNameTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_display_name);
        mPasswordTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_password);
        mConfirmPasswordTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_confirm_password);
    }

    private void setListeners(@NonNull View iView) {
        iView.findViewById(R.id.button_signup).setOnClickListener(this::handleSignupButtonClick);
    }

    private boolean performInputValidation(String iEmail, String iUsername, String iDisplayName, String iPassword) {
        boolean isValid = true;

        if (!AuthValidations.isEmailValid(iEmail)) {
            mEmailTextInputLayout.setError("Invalid email");
            isValid = false;
        } else {
            mEmailTextInputLayout.setError(null);
        }

        if (!AuthValidations.isUsernameValid(iUsername)) {
            mUsernameTextInputLayout.setError("Invalid username");
            isValid = false;
        } else {
            mUsernameTextInputLayout.setError(null);
        }

        if (!AuthValidations.isDisplayNameValid(iDisplayName)) {
            mDisplayNameTextInputLayout.setError("Invalid display name");
            isValid = false;
        } else {
            mDisplayNameTextInputLayout.setError(null);
        }

        if (!AuthValidations.isPasswordValid(iPassword)) {
            mPasswordTextInputLayout.setError("Invalid password");
            isValid = false;
        } else {
            mPasswordTextInputLayout.setError(null);
        }

        if (!iPassword.equals(mConfirmPasswordTextInputLayout.getEditText().getText().toString().trim())) {
            mConfirmPasswordTextInputLayout.setError("Passwords do not match");
            isValid = false;
        } else {
            mConfirmPasswordTextInputLayout.setError(null);
        }

        return isValid;
    }
}