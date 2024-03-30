package com.hit.playpal.auth.domain.usecases;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.LoginFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class GetUserByUidUseCase {
    private IAuthRepository mRepository;

    public GetUserByUidUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }

    public CompletableFuture<UseCaseResult<User, LoginFailure>> execute(String iUid) {
        CompletableFuture<UseCaseResult<User, LoginFailure>> future = new CompletableFuture<>();

        mRepository.getUserByUid(iUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                future.complete(new UseCaseResult<>(user));
            } else {
                future.complete(new UseCaseResult<>(LoginFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }
}
