package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.chats.enums.AppearancePolicy;
import com.hit.playpal.entities.chats.enums.JoiningPolicy;

import java.util.HashMap;

public class GroupSettings {
    private HashMap<JoiningPolicy, Boolean> mJoiningPolicy;
    public HashMap<JoiningPolicy, Boolean> getJoiningPolicy() { return mJoiningPolicy; }
    public void setJoiningPolicy(HashMap<JoiningPolicy, Boolean> iJoiningPolicy) { mJoiningPolicy = iJoiningPolicy; }

    private HashMap<AppearancePolicy, Boolean> mAppearancePolicy;
    public HashMap<AppearancePolicy, Boolean> getAppearancePolicy() { return mAppearancePolicy; }
    public void setAppearancePolicy(HashMap<AppearancePolicy, Boolean> iAppearancePolicy) { mAppearancePolicy = iAppearancePolicy; }

    public GroupSettings() { }
    public GroupSettings(HashMap<JoiningPolicy, Boolean> iJoiningPolicy, HashMap<AppearancePolicy, Boolean> iAppearancePolicy) {
        mJoiningPolicy = iJoiningPolicy;
        mAppearancePolicy = iAppearancePolicy;
    }
}