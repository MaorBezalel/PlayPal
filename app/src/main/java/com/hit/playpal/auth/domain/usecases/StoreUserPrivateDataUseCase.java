package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.SignupFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.UserPrivate;

import java.util.concurrent.CompletableFuture;

public class StoreUserPrivateDataUseCase {
    private IAuthRepository mRepository;

    public StoreUserPrivateDataUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, SignupFailure>> execute(String iUid, UserPrivate iUserPrivate) {
        CompletableFuture<UseCaseResult<Void, SignupFailure>> future = new CompletableFuture<>();

        mRepository.storeUserPrivateData(iUid, iUserPrivate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(new UseCaseResult<Void, SignupFailure>());
            } else {
                future.complete(new UseCaseResult<Void, SignupFailure>(SignupFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
