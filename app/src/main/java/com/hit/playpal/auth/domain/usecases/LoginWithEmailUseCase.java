package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.LoginFailure;
import com.hit.playpal.auth.domain.utils.exceptions.InvalidDetailsException;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class LoginWithEmailUseCase {
    private IAuthRepository mRepository;

    public LoginWithEmailUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<String, LoginFailure>> execute(@NonNull String iEmail, @NonNull String iPassword) {
        CompletableFuture<UseCaseResult<String, LoginFailure> > future = new CompletableFuture<>();

        mRepository.loginWithEmail(iEmail, iPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                future.complete(new UseCaseResult<String, LoginFailure>(uid));
            } else if (task.getException() instanceof InvalidDetailsException) {
                future.complete(new UseCaseResult<String, LoginFailure>(LoginFailure.INVALID_DETAILS));
            } else {
                future.complete(new UseCaseResult<String, LoginFailure>(LoginFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}