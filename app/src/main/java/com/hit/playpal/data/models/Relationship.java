package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.data.enums.RelationshipStatus;

import java.util.Date;

public class Relationship {

    @PropertyName("other_uid")
    private String mOtherUid;

    @PropertyName("other_username")
    private String mOtherUsername;

    @PropertyName("relationship_started_at")
    private Date mRelationshipStartedAt;

    @PropertyName("status")
    private RelationshipStatus mStatus;

    public Relationship() {}

    public Relationship(String iOtherUid, String iOtherUsername, Date iRelationshipStartedAt, RelationshipStatus iStatus) {
        mOtherUid = iOtherUid;
        mOtherUsername = iOtherUsername;
        mRelationshipStartedAt = iRelationshipStartedAt;
        mStatus = iStatus;
    }

    @PropertyName("other_uid") public String getOtherUid() {
        return mOtherUid;
    }
    @PropertyName("other_uid") public void setOtherUid(String iOtherUid) {
        mOtherUid = iOtherUid;
    }

    @PropertyName("other_username") public String getOtherUsername() {
        return mOtherUsername;
    }
    @PropertyName("other_username") public void setOtherUsername(String iOtherUsername) {
        mOtherUsername = iOtherUsername;
    }

    @PropertyName("relationship_started_at") public Date getRelationshipStartedAt() {
        return mRelationshipStartedAt;
    }
    @PropertyName("relationship_started_at") public void setRelationshipStartedAt(Date iRelationshipStartedAt) {
        mRelationshipStartedAt = iRelationshipStartedAt;
    }

    @PropertyName("status") public RelationshipStatus getStatus() {
        return mStatus;
    }
    @PropertyName("status") public void setStatus(RelationshipStatus iStatus) {
        mStatus = iStatus;
    }
}
