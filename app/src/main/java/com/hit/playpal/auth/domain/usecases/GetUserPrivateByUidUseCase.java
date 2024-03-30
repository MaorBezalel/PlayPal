package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.LoginFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.UserPrivate;

import java.util.concurrent.CompletableFuture;

public class GetUserPrivateByUidUseCase {
    private IAuthRepository mRepository;

    public GetUserPrivateByUidUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<UserPrivate, LoginFailure>> execute(@NonNull String iUid) {
        CompletableFuture<UseCaseResult<UserPrivate, LoginFailure>> future = new CompletableFuture<>();

        mRepository.getUserPrivateByUid(iUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserPrivate userPrivate = task.getResult().toObject(UserPrivate.class);
                future.complete(new UseCaseResult<UserPrivate, LoginFailure>(userPrivate));
            } else {
                future.complete(new UseCaseResult<UserPrivate, LoginFailure>(LoginFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
