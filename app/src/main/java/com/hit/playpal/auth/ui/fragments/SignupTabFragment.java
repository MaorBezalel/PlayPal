package com.hit.playpal.auth.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.ui.validations.AuthValidations;
import com.hit.playpal.auth.ui.viewmodels.AuthViewModel;
import com.hit.playpal.utils.Out;

/**
 * <p>A Fragment representing the Signup tab in the authentication process.</p>
 * <p>It handles user input validation and communicates with the AuthViewModel to perform the signup operation.</p>
 * @see AuthViewModel
 */
public class SignupTabFragment extends Fragment {

    private AuthViewModel mAuthViewModel;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mUsernameTextInputLayout;
    private TextInputLayout mDisplayNameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;
    private ProgressBar mProgressBar;

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
            beginLoadingState();
            mAuthViewModel.signup(email, username, displayName, password);
        }
    }

    /**
     * <p>Changes the UI to a loading state, hiding the signup button and showing a progress bar.</p>
     */
    private void beginLoadingState() {
        mProgressBar.setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.button_signup).setVisibility(View.GONE);
    }

    /**
     * <p>Changes the UI back to its original state, hiding the progress bar and showing the signup button.</p>
     */
    private void endLoadingState() {
        mProgressBar.setVisibility(View.GONE);
        requireActivity().findViewById(R.id.button_signup).setVisibility(View.VISIBLE);
    }

    private void initAndSubscribeToViewModels() {
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        mAuthViewModel.getSignupFailure().observe(getViewLifecycleOwner(), signupFailure -> {
            endLoadingState();
            handleSignupFailure(signupFailure);
        });
    }

    private void handleSignupFailure(AuthServerFailure iSignupFailure) {
        if (iSignupFailure != null) {
            switch (iSignupFailure) {
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
    }

    private void initViews(@NonNull View iView) {
        mEmailTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_email);
        mUsernameTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_username);
        mDisplayNameTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_display_name);
        mPasswordTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_password);
        mConfirmPasswordTextInputLayout = iView.findViewById(R.id.textinputlayout_signup_confirm_password);
        mProgressBar = iView.findViewById(R.id.progressbar_signup);
    }

    private void setListeners(@NonNull View iView) {
        iView.findViewById(R.id.button_signup).setOnClickListener(this::handleSignupButtonClick);

        mDisplayNameTextInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
           mDisplayNameTextInputLayout.setHelperText(hasFocus ? "This is how others see you. You can use special characters and emoji." : null);
        });
    }

    private boolean performInputValidation(String iEmail, String iUsername, String iDisplayName, String iPassword, String iConfirmPassword) {
        boolean isEmailValid = performEmailValidation(iEmail);
        boolean isUsernameValid = performUsernameValidation(iUsername);
        boolean isDisplayNameValid = performDisplayNameValidation(iDisplayName);
        boolean isPasswordValid = performPasswordValidation(iPassword);
        // boolean isConfirmPasswordValid = performConfirmPasswordValidation(iPassword, iConfirmPassword);

        return isEmailValid &&
                isUsernameValid &&
                isDisplayNameValid &&
                isPasswordValid;
    }

    private boolean performEmailValidation(String iEmail) {
        boolean isEmailValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isEmailValid(iEmail, invalidationReason)) {
            mEmailTextInputLayout.setError(invalidationReason.get());
            isEmailValid = false;
        } else {
            mEmailTextInputLayout.setError(null);
        }

        return isEmailValid;
    }

    private boolean performUsernameValidation(String iUsername) {
        boolean isUsernameValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isUsernameValid(iUsername, invalidationReason)) {
            Log.d("SignupTabFragment", "Username is invalid: " + invalidationReason.get());
            mUsernameTextInputLayout.setError(invalidationReason.get());
            isUsernameValid = false;
        } else {
            mUsernameTextInputLayout.setError(null);
        }

        return isUsernameValid;
    }

    private boolean performDisplayNameValidation(String iDisplayName) {
        boolean isDisplayNameValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isDisplayNameValid(iDisplayName, invalidationReason)) {
            mDisplayNameTextInputLayout.setError(invalidationReason.get());
            isDisplayNameValid = false;
        } else {
            mDisplayNameTextInputLayout.setError(null);
        }

        return isDisplayNameValid;
    }

    private boolean performPasswordValidation(String iPassword) {
        boolean isPasswordValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isPasswordValid(iPassword, invalidationReason)) {
            mPasswordTextInputLayout.setError(invalidationReason.get());
            isPasswordValid = false;
        } else {
            mPasswordTextInputLayout.setError(null);
        }

        return isPasswordValid;
    }

    private boolean performConfirmPasswordValidation(String iPassword, String iConfirmPassword) {
        boolean isConfirmPasswordValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isConfirmPasswordValid(iPassword, iConfirmPassword, invalidationReason)) {
            mConfirmPasswordTextInputLayout.setError(invalidationReason.get());
            isConfirmPasswordValid = false;
        } else {
            mConfirmPasswordTextInputLayout.setError(null);
        }

        return isConfirmPasswordValid;
    }
}