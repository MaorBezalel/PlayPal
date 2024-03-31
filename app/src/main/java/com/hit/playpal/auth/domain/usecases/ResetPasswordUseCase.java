package com.hit.playpal.auth.domain.usecases;

import android.util.Log;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.domain.utils.exceptions.InvalidDetailsException;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class ResetPasswordUseCase {
    private IAuthRepository mAuthRepository;

    public ResetPasswordUseCase(IAuthRepository iAuthRepository) {
        mAuthRepository = iAuthRepository;
    }

    public CompletableFuture<UseCaseResult<Void, AuthServerFailure>> execute(String iEmail) {
        CompletableFuture<UseCaseResult<Void, AuthServerFailure>> future = new CompletableFuture<>();

        mAuthRepository.resetPassword(iEmail)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("ResetPasswordUseCase", "Password reset email sent to " + iEmail);
                    future.complete(new UseCaseResult<>());
                } else if (task.getException() instanceof InvalidDetailsException) {
                    future.complete(new UseCaseResult<>(AuthServerFailure.INVALID_DETAILS));
                } else {
                    future.complete(new UseCaseResult<>(AuthServerFailure.INTERNAL_AUTH_ERROR));
                }
            });

        return future;
    }
}
