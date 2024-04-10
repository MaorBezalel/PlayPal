package com.hit.playpal.profile.domain.usecases;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

public class SendFriendRequestUseCase {
    private final IProfileRepository mProfileRepository;

    public SendFriendRequestUseCase(IProfileRepository iProfileRepository) {
        mProfileRepository = iProfileRepository;
    }

    public Task<Void> sendFriendRequest(String iReceiverId, String iSenderUid, String iSenderDisplayName, String iSenderProfileImage) {
        return mProfileRepository.sendFriendRequest(iReceiverId, iSenderUid, iSenderDisplayName, iSenderProfileImage);
    }
}
