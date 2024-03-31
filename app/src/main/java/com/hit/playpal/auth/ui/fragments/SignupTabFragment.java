package com.hit.playpal.auth.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.validations.AuthValidations;
import com.hit.playpal.auth.ui.viewmodels.AuthViewModel;
import com.hit.playpal.utils.Out;

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
        boolean isValid = performInputValidation(email, username, displayName, password, confirmPassword);

        if (isValid) {
            mAuthViewModel.signup(email, username, displayName, password);
        }
    }

    private void initAndSubscribeToViewModels() {
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        mAuthViewModel.getSignupFailure().observe(getViewLifecycleOwner(), signupFailure -> {
            if (signupFailure!= null) {
                switch (signupFailure) {
                    case EMAIL_ALREADY_TAKEN:
                        mEmailTextInputLayout.setError("Email already belongs to an existing account, try logging in");
                        break;
                    case USERNAME_ALREADY_TAKEN:
                        mUsernameTextInputLayout.setError("Username already belongs to an existing account, try logging in");
                        break;
                    case INTERNAL_AUTH_ERROR:
                        Toast.makeText(requireContext(), "Signup failed due to an internal error, please try again later", Toast.LENGTH_SHORT).show();
                        Log.e("SignupTabFragment", "Internal error occurred during Authentication while signing up");
                        break;
                    case INTERNAL_DB_ERROR:
                        Toast.makeText(requireContext(), "Signup failed due to an internal error, please try again later", Toast.LENGTH_SHORT).show();
                        Log.e("SignupTabFragment", "Internal error occurred during Database operation while signing up");
                        break;
                    default:
                        Toast.makeText(requireContext(), "Signup failed due to an unknown error, please try again later", Toast.LENGTH_SHORT).show();
                        Log.e("SignupTabFragment", "Unknown error occurred while signing up (might mixed up with login errors)");
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

    private boolean performInputValidation(String iEmail, String iUsername, String iDisplayName, String iPassword, String iConfirmPassword) {
        boolean isValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isEmailValid(iEmail, invalidationReason)) {
            mEmailTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mEmailTextInputLayout.setError(null);
        }

        if (!AuthValidations.isUsernameValid(iUsername, invalidationReason)) {
            Log.d("SignupTabFragment", "Username is invalid: " + invalidationReason.get());
            mUsernameTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mUsernameTextInputLayout.setError(null);
        }

        if (!AuthValidations.isDisplayNameValid(iDisplayName, invalidationReason)) {
            mDisplayNameTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mDisplayNameTextInputLayout.setError(null);
        }

        if (!AuthValidations.isPasswordValid(iPassword, invalidationReason)) {
            mPasswordTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mPasswordTextInputLayout.setError(null);

        }

        if (!AuthValidations.isConfirmPasswordValid(iPassword, iConfirmPassword, invalidationReason)) {
            mConfirmPasswordTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mConfirmPasswordTextInputLayout.setError(null);
        }

        return isValid;
    }
}