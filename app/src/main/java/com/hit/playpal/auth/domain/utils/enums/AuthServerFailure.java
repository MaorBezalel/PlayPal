package com.hit.playpal.auth.domain.utils.enums;

/**
 * <p>Represents the possible failures that can occur in the data layer of <code>auth</code> package.</p>
 * <p>These failures are used to communicate the result of the operation to the ui layer.</p>
 */
public enum AuthServerFailure {
    /**
     * <p>When the user tries to signup with an email that is already taken.</p>
     */
    EMAIL_ALREADY_TAKEN,

    /**
     * <p>When the user tries to signup with a username that is already taken.</p>
     */
    USERNAME_ALREADY_TAKEN,

    /**
     * <p>When the user provides invalid details during login or forgot password. Can be due to:</p>
     * <ul>
     *     <li>Unknown email/username during login</li>
     *     <li>Incorrect password during login</li>
     *     <li>Unknown email during forgot password</li>
     * </ul>
     */
    INVALID_DETAILS,

    /**
     * <p>When the user tries to login with an account that is disabled. Can be due to:</p>
     * <ul>
     *     <li>Account was manually disabled by development team in the firebase console</li>
     *     <li>Account was disabled due to few unsuccessful login attempts</li>
     * </ul>
     */
    DISABLED_ACCOUNT,

    /**
     * <p>When an internal or unknown error occurs in Firebase Authentication.</p>
     */
    INTERNAL_AUTH_ERROR,

    /**
     * <p>When an internal or unknown error occurs in Firebase Firestore.</p>
     */
    INTERNAL_DB_ERROR
}
