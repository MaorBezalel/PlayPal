package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class IsUsernameUniqueUseCase {
    private IAuthRepository mRepository;

    public IsUsernameUniqueUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, AuthServerFailure>> execute(String iUsername) {
        CompletableFuture<UseCaseResult<Void, AuthServerFailure>> future = new CompletableFuture<>();

        mRepository.getUserByUsername(iUsername).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    future.complete(new UseCaseResult<Void, AuthServerFailure>());
                } else {
                    future.complete(new UseCaseResult<Void, AuthServerFailure>(AuthServerFailure.USERNAME_ALREADY_TAKEN));
                }
            } else {
                future.complete(new UseCaseResult<Void, AuthServerFailure>(AuthServerFailure.INTERNAL_DB_ERROR));
            }
        });

        return future;
    }
}
