package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.chats.enums.ChatType;

import java.util.HashMap;
import java.util.List;

public abstract class Chat {

    private ChatType mType;
    public ChatType getType() {
        return mType;
    }
    public void setType(ChatType iType) {
        mType = iType;
    }


    private List<String> mMembersUid;
    public List<String> getMembersUid() {
        return mMembersUid;
    }
    public void setMembersUid(List<String> iMembersUid) {
        mMembersUid = iMembersUid;
    }

    private Message mLastMsg;
    public Message getLastMsg() { return mLastMsg; }
    public void setLastMsg(Message iLastMsg) { mLastMsg = iLastMsg; }

    public Chat() {

    }
    public Chat(ChatType mType, List<String> mMembersUid) {
        this.mType = mType;
        this.mMembersUid = mMembersUid;
    }
}
