package com.hit.playpal.profile.domain.usecases;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.hit.playpal.profile.data.repositories.ProfileRepository;

public class RemoveFriendUseCase {

    private final ProfileRepository mProfileRepository;


    public RemoveFriendUseCase() {
        this.mProfileRepository = new ProfileRepository();


}

    public Task<Void> execute(String currentUser, String Uid) {
        Task<Void> task1 = mProfileRepository.removeFriend(Uid, currentUser);
        Task<Void> task2 = mProfileRepository.removeFriend(currentUser, Uid);
        return Tasks.whenAllSuccess(task1, task2).continueWith(task -> null);
    }

}
