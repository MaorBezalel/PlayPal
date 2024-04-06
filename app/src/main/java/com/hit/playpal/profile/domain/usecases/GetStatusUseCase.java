package com.hit.playpal.profile.domain.usecases;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hit.playpal.profile.data.repositories.ProfileRepository;
import androidx.annotation.NonNull;

public class GetStatusUseCase {
    private ProfileRepository mProfileRepository;

    public GetStatusUseCase() {
        this.mProfileRepository = new ProfileRepository();
    }

    public Task<String> execute(String iUid, String iOtherUserUid) {
        Log.d("GetStatusUseCase", "execute called with Uid: " + iUid + " and otherUserUid: " + iOtherUserUid);
        return mProfileRepository.getStatus(iUid, iOtherUserUid);
    }
}
