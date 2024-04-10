package com.hit.playpal.home.domain.utils;

import com.hit.playpal.entities.users.enums.RelationshipStatus;

public class RelationshipUpdateFailedException extends Exception{
    public RelationshipUpdateFailedException(RelationshipStatus iNewStatus) {
        super("Failed to update status to " + iNewStatus + "!");
    }
}
