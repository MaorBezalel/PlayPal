package com.hit.playpal.auth.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.validations.AuthValidations;
import com.hit.playpal.auth.ui.viewmodels.AuthViewModel;

public class LoginTabFragment extends Fragment {
    private AuthViewModel mAuthViewModel;
    private TextInputLayout mEmailOrUsernameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer,
                             Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_login_tab, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);
        initAndSubscribeToViewModels();
        initViews(iView);
        setListeners(iView);
    }

    public void onLoginButtonClicked(View iView) {
        String emailOrUsername = mEmailOrUsernameTextInputLayout.getEditText().getText().toString().trim().toLowerCase();
        String password = mPasswordTextInputLayout.getEditText().getText().toString().trim();
        boolean isValid = performInputValidation(emailOrUsername, password);

        if (isValid) {
            if (isEmail(emailOrUsername)) {
                mAuthViewModel.loginWithEmail(emailOrUsername, password);
            } else {
                mAuthViewModel.loginWithUsername(emailOrUsername, password);
            }
        }
    }

    private void initAndSubscribeToViewModels() {
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        mAuthViewModel.getLoginFailure().observe(getViewLifecycleOwner(), loginFailure -> {
            if (loginFailure != null) {
                switch (loginFailure) {
                    case INVALID_DETAILS:
                        mEmailOrUsernameTextInputLayout.setError("Invalid email/username or password");
                        mPasswordTextInputLayout.setError("Invalid email/username or password");
                        break;
                    case UNKNOWN_ERROR:
                        Toast.makeText(requireContext(), "Internal error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void initViews(@NonNull View iView) {
        mEmailOrUsernameTextInputLayout = iView.findViewById(R.id.textinputlayout_login_email_or_username);
        mPasswordTextInputLayout = iView.findViewById(R.id.textinputlayout_login_password);
    }

    private void setListeners(@NonNull View iView) {
        iView.findViewById(R.id.button_login).setOnClickListener(this::onLoginButtonClicked);
    }

    private boolean performInputValidation(String iEmailOrUsername, String iPassword) {
        boolean isValid = true;

        if (!AuthValidations.isEmailValid(iEmailOrUsername) && !AuthValidations.isUsernameValid(iEmailOrUsername)) {
            mEmailOrUsernameTextInputLayout.setError("Invalid email or username");
            isValid = false;
        } else {
            mEmailOrUsernameTextInputLayout.setError(null);
        }

        if (!AuthValidations.isPasswordValid(iPassword)) {
            mPasswordTextInputLayout.setError("Invalid password");
            isValid = false;
        } else {
            mPasswordTextInputLayout.setError(null);
        }

        return isValid;
    }

    private boolean isEmail(String iEmailOrUsername) {
        return Patterns.EMAIL_ADDRESS.matcher(iEmailOrUsername).matches();
    }
}