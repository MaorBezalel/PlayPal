package com.hit.playpal.auth.domain.utils.enums;

public enum SignupFailure {
    // User input errors which can only be detected in the domain layer
    EMAIL_ALREADY_TAKEN,
    USERNAME_ALREADY_TAKEN,

    // Internal errors for debugging purposes (should not be exposed to the user)
    ERROR_CREATING_USER_IN_AUTH,
    ERROR_ADDING_USER_PUBLIC_DATA,
    ERROR_ADDING_USER_PRIVATE_DATA,
    ERROR_ADDING_USER_DEFAULT_SETTINGS,


    // Return this error if the error is internal or indeed unknown
    INTERNAL_ERROR
}
