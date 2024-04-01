package com.hit.playpal.auth.ui.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

public class ForgotPasswordFragment extends Fragment {
    private static final String TAG = "ForgotPasswordFragment";
    private AuthViewModel mAuthViewModel;
    private TextInputLayout mEmailTextInputLayout;
    private ProgressBar mProgressBar;
    private OnBackPressedCallback mOnBackPressedCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer,
                             Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_forgot_password, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);
        mAuthViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        initViews(iView);
        setListeners(iView);
        initOnBackPressedCallback();
        observeResetPasswordSuccess(iView);
        observeResetPasswordFailure(iView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOnBackPressedCallback.remove();
    }

    private void initViews(@NonNull View iView) {
        mEmailTextInputLayout = iView.findViewById(R.id.textinputlayout_forgot_password_email);
        mProgressBar = iView.findViewById(R.id.progressbar_forgot_password);
    }

    private void setListeners(@NonNull View iView) {
        setBackButtonListener(iView);
        setResetButtonListener(iView);
    }

    private void setBackButtonListener(@NonNull View iView) {
        iView.findViewById(R.id.button_forgot_password_back).setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            getParentFragmentManager().popBackStack();
            requireActivity().findViewById(R.id.tablayout_auth).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.viewpager2_auth).setVisibility(View.VISIBLE);
        });
    }

    private void setResetButtonListener(@NonNull View iView) {
        iView.findViewById(R.id.button_forgot_password_reset).setOnClickListener(v -> {
            String email = mEmailTextInputLayout.getEditText().getText().toString().trim().toLowerCase();
            boolean isValid = performInputValidation(email);

            if (isValid) {
                beginLoadingState();
                mAuthViewModel.forgotPassword(email);
            } else {
                mEmailTextInputLayout.setError("Invalid email address.");
            }
        });
    }

    private void initOnBackPressedCallback() {
        mOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (requireActivity().findViewById(R.id.tablayout_auth).getVisibility() == View.GONE) {
                    getParentFragmentManager().popBackStack();
                    requireActivity().findViewById(R.id.tablayout_auth).setVisibility(View.VISIBLE);
                    requireActivity().findViewById(R.id.viewpager2_auth).setVisibility(View.VISIBLE);
                } else {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), mOnBackPressedCallback);
    }

    private void observeResetPasswordSuccess(@NonNull View iView) {
        mAuthViewModel.getResetPasswordSuccess().observe(getViewLifecycleOwner(), resetPasswordSuccess -> {
            mProgressBar.setVisibility(View.GONE);
            iView.findViewById(R.id.button_forgot_password_reset).setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "Password reset email sent successfully", Toast.LENGTH_LONG).show();

            // Navigate back to login fragment
            getParentFragmentManager().popBackStack();
            requireActivity().findViewById(R.id.tablayout_auth).setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.viewpager2_auth).setVisibility(View.VISIBLE);
        });
    }

    private void observeResetPasswordFailure(@NonNull View iView) {
        mAuthViewModel.getResetPasswordFailure().observe(getViewLifecycleOwner(), resetPasswordFailure -> {
            endLoadingState();

            if (resetPasswordFailure == AuthServerFailure.INVALID_DETAILS) {
                mEmailTextInputLayout.setError("Email not found, please make sure you entered the correct email.");
                Log.e(TAG, "Forgot password failed: Email not found");
            } else {
                mEmailTextInputLayout.setError("An error occurred, please try again later.");
                Log.e(TAG, "Forgot password failed: Firebase Auth unknown error");
            }
        });
    }

    private boolean performInputValidation(String email) {
        boolean isValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!AuthValidations.isEmailValid(email, invalidationReason)) {
            mEmailTextInputLayout.setError(invalidationReason.get());
            isValid = false;
        } else {
            mEmailTextInputLayout.setError(null);
        }

        return isValid;
    }

    private void beginLoadingState() {
        mProgressBar.setVisibility(View.VISIBLE);
        requireView().findViewById(R.id.button_forgot_password_reset).setVisibility(View.GONE);
    }

    private void endLoadingState() {
        mProgressBar.setVisibility(View.GONE);
        requireView().findViewById(R.id.button_forgot_password_reset).setVisibility(View.VISIBLE);
    }
}