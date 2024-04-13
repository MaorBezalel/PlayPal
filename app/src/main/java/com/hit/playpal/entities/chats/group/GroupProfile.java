package com.hit.playpal.entities.chats.group;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.group.enums.GroupChatRoomRole;
import com.hit.playpal.entities.users.User;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Entity class that represents a group profile.
 */
public class GroupProfile {
    @PropertyName("description") private String mDescription;
    @PropertyName("description") public String getDescription() {
        return mDescription;
    }
    @PropertyName("description") public void setDescription(String iDescription) {
        this.mDescription = iDescription;
    }

    @PropertyName("participants") private List<Participant> mParticipants;
    @PropertyName("participants") public List<Participant> getParticipants() {
        return mParticipants;
    }
    @PropertyName("participants") public void setParticipants(List<Participant> iParticipants) {
        this.mParticipants = iParticipants;
    }

    public GroupProfile() { }
    public GroupProfile(String iDescription, List<Participant> iParticipants) {
        this.mDescription = iDescription;
        this.mParticipants = iParticipants;
    }

    public void convertUsersToParticipants(@NonNull User iOwner, @NonNull Set<User> iRegularMembers) {
        mParticipants = new ArrayList<>(iRegularMembers.size() + 1);

        mParticipants.add(new Participant(iOwner.getUid(), iOwner.getDisplayName(), iOwner.getProfilePicture(), GroupChatRoomRole.OWNER, iOwner.getUsername()));
        iRegularMembers.forEach(user -> mParticipants.add(new Participant(user.getUid(), user.getDisplayName(), user.getProfilePicture(), GroupChatRoomRole.REGULAR, user.getUsername())));
    }

    /**
     * Entity class that represents a participant in a group chat room.
     */
    public static class Participant {
        @PropertyName("uid") private String mUserUid;
        @PropertyName("uid")  public String getUserUid() {
            return mUserUid;
        }
        @PropertyName("uid")  public void setUserUid(String iUserUid) {
            mUserUid = iUserUid;
        }

        @PropertyName("display_name") private String mDisplayName;
        @PropertyName("display_name") public String getDisplayName() {
            return mDisplayName;
        }
        @PropertyName("display_name") public void setDisplayName(String iDisplayName) {
            mDisplayName = iDisplayName;
        }

        @PropertyName("profile_image") private String mProfilePicture;
        @PropertyName("profile_image") public String getProfilePicture() {
            return mProfilePicture;
        }
        @PropertyName("profile_image") public void setProfilePicture(String iProfilePicture) {
            mProfilePicture = iProfilePicture;
        }

        @PropertyName("chat_role") private GroupChatRoomRole mGroupChatRoomRole;
        @PropertyName("chat_role")  public GroupChatRoomRole getUserChatRole() {
            return mGroupChatRoomRole;
        }
        @PropertyName("chat_role")  public void setUserChatRole(GroupChatRoomRole iGroupChatRoomRole) {
            mGroupChatRoomRole = iGroupChatRoomRole;
        }

        @PropertyName("username") private String mUsername;
        @PropertyName("username") public String getUsername() {
            return mUsername;
        }
        @PropertyName("username") public void setUsername(String iUsername) {
            mUsername = iUsername;
        }

        public Participant() { }
        public Participant(String iUserUid, String iDisplayName, String iProfilePicture, GroupChatRoomRole iGroupChatRoomRole, String iUsername) {
            this.mUserUid = iUserUid;
            this.mDisplayName = iDisplayName;
            this.mProfilePicture = iProfilePicture;
            this.mGroupChatRoomRole = iGroupChatRoomRole;
            this.mUsername = iUsername;
        }

        @NonNull
        @Contract("_, _ -> new")
        public static Participant parseUser(@NonNull User iUser, GroupChatRoomRole iGroupChatRoomRole) {
            return new Participant(iUser.getUid(), iUser.getDisplayName(), iUser.getProfilePicture(), iGroupChatRoomRole, iUser.getUsername());
        }
    }
}
