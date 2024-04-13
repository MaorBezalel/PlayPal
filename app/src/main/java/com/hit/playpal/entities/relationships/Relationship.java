package com.hit.playpal.entities.relationships;

import com.hit.playpal.entities.relationships.enums.RelationshipStatus;
import com.hit.playpal.entities.users.User;

/**
 * Entity class that represents a relationship between two users.
 */
public class Relationship {
    private User mOther_user;
    public User getOther_user() { return mOther_user; }
    public void setOther_user(User iOther_user) { mOther_user = iOther_user; }

    private RelationshipStatus mStatus;
    public RelationshipStatus getStatus() { return mStatus; }
    public void setStatus(RelationshipStatus iStatus) { mStatus = iStatus; }

    public Relationship() { }
    public Relationship(User iOther_user, RelationshipStatus iStatus) {
        mOther_user = iOther_user;
        mStatus = iStatus;
    }
}