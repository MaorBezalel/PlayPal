package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.UserPrivate;

import java.util.concurrent.CompletableFuture;

public class GetUserPrivateByUidUseCase {
    private IAuthRepository mRepository;

    public GetUserPrivateByUidUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<UserPrivate, AuthServerFailure>> execute(@NonNull String iUid) {
        CompletableFuture<UseCaseResult<UserPrivate, AuthServerFailure>> future = new CompletableFuture<>();

        mRepository.getUserPrivateByUid(iUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserPrivate userPrivate = task.getResult().toObject(UserPrivate.class);
                future.complete(new UseCaseResult<UserPrivate, AuthServerFailure>(userPrivate));
            } else {
                future.complete(new UseCaseResult<UserPrivate, AuthServerFailure>(AuthServerFailure.INTERNAL_DB_ERROR));
            }
        });

        return future;
    }
}
