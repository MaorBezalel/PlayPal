package com.hit.playpal.entities.users;

import com.hit.playpal.entities.users.enums.RelationshipStatus;


public class Relationship {
    private User mOther;
    public User getOther() { return mOther; }
    public void setOther(User iOther) { mOther = iOther; }

    private RelationshipStatus mStatus;
    public RelationshipStatus getStatus() { return mStatus; }
    public void setStatus(RelationshipStatus iStatus) { mStatus = iStatus; }

    public Relationship() { }
    public Relationship(User iOther, RelationshipStatus iStatus) {
        mOther = iOther;
        mStatus = iStatus;
    }
}
