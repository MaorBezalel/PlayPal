package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.domain.utils.exceptions.EmailAlreadyTakenException;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class CreateUserUseCase {
    private IAuthRepository mRepository;

    public CreateUserUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<String, AuthServerFailure>> execute(String iEmail, String iPassword) {
        CompletableFuture<UseCaseResult<String, AuthServerFailure>> future = new CompletableFuture<>();

        mRepository.createUser(iEmail, iPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(new UseCaseResult<>(task.getResult().getUser().getUid()));
            } else if (task.getException() instanceof EmailAlreadyTakenException) {
                future.complete(new UseCaseResult<>(AuthServerFailure.EMAIL_ALREADY_TAKEN));
            } else {
                future.complete(new UseCaseResult<>(AuthServerFailure.INTERNAL_AUTH_ERROR));
            }
        });

        return future;
    }
}
