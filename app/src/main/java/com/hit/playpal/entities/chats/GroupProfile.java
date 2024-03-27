package com.hit.playpal.entities.chats;

import java.util.List;

public class GroupProfile {
    private String mDescription;
    private List<Participant> mParticipants;

    public GroupProfile() {
    }

    public GroupProfile(String iDescription, List<Participant> iParticipants) {
        this.mDescription = iDescription;
        this.mParticipants = iParticipants;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String iDescription) {
        this.mDescription = iDescription;
    }

    public List<Participant> getParticipants() {
        return mParticipants;
    }

    public void setParticipants(List<Participant> iParticipants) {
        this.mParticipants = iParticipants;
    }
}
