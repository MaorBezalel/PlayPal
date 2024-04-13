package com.hit.playpal.entities.relationships;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

/**
 * Entity class that represents a one-to-one chat relationship.
 */
public class OneToOneChatRelationship {
    @DocumentId private String mId;
    @DocumentId public String getId() { return mId; }
    @DocumentId public void setId(String iId) { mId = iId; }

    @PropertyName("joined_uids") private String mJoinedUids;
    @PropertyName("joined_uids") public String getJoinedUids() { return mJoinedUids; }
    @PropertyName("joined_uids") public void setJoinedUids(String iJoinedUids) { mJoinedUids = iJoinedUids; }

    @PropertyName("chat_room_id") private String mChatRoomId;
    @PropertyName("chat_room_id") public String getChatRoomId() { return mChatRoomId; }
    @PropertyName("chat_room_id") public void setChatRoomId(String iChatRoomId) { mChatRoomId = iChatRoomId; }

    public OneToOneChatRelationship() { }
    public OneToOneChatRelationship(String iJoinedUids, String iChatRoomId) {
        mJoinedUids = iJoinedUids;
        mChatRoomId = iChatRoomId;
    }

    /**
     * Joins two UIDs with an underscore.
     * @param iUid1 The first UID.
     * @param iUid2 The second UID.
     * @return The two UIDs joined with an underscore.
     */
    static public String joinUids(String iUid1, String iUid2) {
        return iUid1 + "_" + iUid2;
    }
}
