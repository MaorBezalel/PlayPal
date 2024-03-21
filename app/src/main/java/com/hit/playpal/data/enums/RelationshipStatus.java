package com.hit.playpal.data.enums;

public enum RelationshipStatus {
    PENDING, // to avoid sending friend requests to the same person multiple times
    FRIENDS,
    BLOCKED
}
