package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.SignupFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class StoreUserPublicDataUseCase {
    private IAuthRepository mRepository;

    public StoreUserPublicDataUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, SignupFailure>> execute(User iUserPublic) {
        CompletableFuture<UseCaseResult<Void, SignupFailure>> future = new CompletableFuture<>();

        mRepository.storeUserPublicData(iUserPublic).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(new UseCaseResult<Void, SignupFailure>());
            } else {
                future.complete(new UseCaseResult<Void, SignupFailure>(SignupFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
