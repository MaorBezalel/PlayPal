package com.hit.playpal.auth.domain.usecases;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class GetUserByUsernameUseCase {
    private IAuthRepository mRepository;
    
    public GetUserByUsernameUseCase(IAuthRepository iRepository) {
        mRepository = iRepository;
    }
    
    public CompletableFuture<UseCaseResult<User, AuthServerFailure>> execute(@NonNull String iUsername) {
        CompletableFuture<UseCaseResult<User, AuthServerFailure>> future = new CompletableFuture<>();
        
        mRepository.getUserByUsername(iUsername).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() == 1) {
                    User user = task.getResult().toObjects(User.class).get(0);
                    future.complete(new UseCaseResult<User, AuthServerFailure>(user));
                } else {
                    future.complete(new UseCaseResult<User, AuthServerFailure>(AuthServerFailure.INVALID_DETAILS));
                }
            } else {
                future.complete(new UseCaseResult<User, AuthServerFailure>(AuthServerFailure.EMAIL_ALREADY_TAKEN));
            }
        });
        
        return future;
    }
}
