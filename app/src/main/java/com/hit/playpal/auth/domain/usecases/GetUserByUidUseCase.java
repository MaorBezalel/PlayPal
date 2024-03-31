package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class GetUserByUidUseCase {
    private IAuthRepository mRepository;

    public GetUserByUidUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<User, AuthServerFailure>> execute(String iUid) {
        CompletableFuture<UseCaseResult<User, AuthServerFailure>> future = new CompletableFuture<>();

        mRepository.getUserByUid(iUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                future.complete(new UseCaseResult<>(user));
            } else {
                future.complete(new UseCaseResult<>(AuthServerFailure.INTERNAL_DB_ERROR));
            }
        });

        return future;
    }
}
