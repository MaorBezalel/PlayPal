package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.domain.utils.exceptions.DisabledAccountException;
import com.hit.playpal.auth.domain.utils.exceptions.InvalidDetailsException;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class LoginWithEmailUseCase {
    private IAuthRepository mRepository;

    public LoginWithEmailUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<String, AuthServerFailure>> execute(@NonNull String iEmail, @NonNull String iPassword) {
        CompletableFuture<UseCaseResult<String, AuthServerFailure> > future = new CompletableFuture<>();

        mRepository.loginWithEmail(iEmail, iPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                future.complete(new UseCaseResult<String, AuthServerFailure>(uid));
            } else if (task.getException() instanceof InvalidDetailsException) {
                future.complete(new UseCaseResult<String, AuthServerFailure>(AuthServerFailure.INVALID_DETAILS));
            } else if (task.getException() instanceof DisabledAccountException) {
                future.complete(new UseCaseResult<String, AuthServerFailure>(AuthServerFailure.DISABLED_ACCOUNT));
            }
            else {
                future.complete(new UseCaseResult<String, AuthServerFailure>(AuthServerFailure.INTERNAL_AUTH_ERROR));
            }
        });

        return future;
    }
}