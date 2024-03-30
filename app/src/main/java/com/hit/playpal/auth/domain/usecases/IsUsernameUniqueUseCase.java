package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.SignupFailure;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class IsUsernameUniqueUseCase {
    private IAuthRepository mRepository;

    public IsUsernameUniqueUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, SignupFailure>> execute(String iUsername) {
        CompletableFuture<UseCaseResult<Void, SignupFailure>> future = new CompletableFuture<>();

        mRepository.getUserByUsername(iUsername).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    future.complete(new UseCaseResult<Void, SignupFailure>());
                } else {
                    future.complete(new UseCaseResult<Void, SignupFailure>(SignupFailure.USERNAME_ALREADY_TAKEN));
                }
            } else {
                future.complete(new UseCaseResult<Void, SignupFailure>(SignupFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
