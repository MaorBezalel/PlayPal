package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.SignupFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.Settings;

import java.util.concurrent.CompletableFuture;

public class StoreUserDefaultSettingsDataUseCase {
    private IAuthRepository mRepository;

    public StoreUserDefaultSettingsDataUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, SignupFailure>> execute(String iUid, Settings iSettings) {
        CompletableFuture<UseCaseResult<Void, SignupFailure>> future = new CompletableFuture<>();

        mRepository.storeUserDefaultSettingsData(iUid, iSettings).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(new UseCaseResult<Void, SignupFailure>());
            } else {
                future.complete(new UseCaseResult<Void, SignupFailure>(SignupFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
