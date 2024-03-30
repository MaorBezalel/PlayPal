package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.LoginFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class GetUserByUsernameUseCase {
    private IAuthRepository mRepository;
    
    public GetUserByUsernameUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }
    
    public CompletableFuture<UseCaseResult<User, LoginFailure>> execute(@NonNull String iUsername) {
        CompletableFuture<UseCaseResult<User, LoginFailure>> future = new CompletableFuture<>();
        
        mRepository.getUserByUsername(iUsername).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() != 1) {
                    future.complete(new UseCaseResult<User, LoginFailure>(LoginFailure.INVALID_DETAILS));
                } else {
                    User user = task.getResult().toObjects(User.class).get(0);
                    future.complete(new UseCaseResult<User, LoginFailure>(user));
                }
            } else {
                future.complete(new UseCaseResult<User, LoginFailure>(LoginFailure.UNKNOWN_ERROR));
            }
        });
        
        return future;
    }
}
