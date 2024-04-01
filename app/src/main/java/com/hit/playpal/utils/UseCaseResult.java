package com.hit.playpal.utils;

import com.hit.playpal.auth.domain.utils.enums.SignupFailure;

/**
 * A class that represents the result of a use case.
 * @param <TSuccess> The type of the result upon success.
 * @param <TFailure> The type of the result upon failure. Must be an enum (indicating the type of failure).
 */
public class UseCaseResult<TSuccess, TFailure extends Enum<TFailure>> {
    /**
     * A flag that indicates whether the use case was successful or not.
     */
    private boolean mIsSuccessful;

    /**
     * @return true if the use case was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return mIsSuccessful;
    }

    /**
     * The result of the use case upon success.
     */
    private TSuccess mResult;

    /**
     * @return The result of the use case upon success.
     * @apiNote This method should only be called if isSuccessful() returns true.
     */
    public TSuccess getResult() {
        return mResult;
    }

    /**
     * The result of the use case upon failure.
     */
    private TFailure mFailure;

    /**
     * @return The result of the use case upon failure.
     * @apiNote This method should only be called if isSuccessful() returns false.
     */
    public TFailure getFailure() {
        return mFailure;
    }

    public UseCaseResult() { // upon success (no result)
        mResult = null;
        mIsSuccessful = true;
        mFailure = null;
    }
    public UseCaseResult(TSuccess iResult) { // upon success (with result)
        mResult = iResult;
        mIsSuccessful = true;
        mFailure = null;
    }
    public UseCaseResult(TFailure iFailure) { // upon failure
        mResult = null;
        mIsSuccessful = false;
        mFailure = iFailure;
    }
}
