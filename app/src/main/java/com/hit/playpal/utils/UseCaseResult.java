package com.hit.playpal.utils;

import com.hit.playpal.auth.domain.utils.enums.SignupFailure;

public class UseCaseResult<TSuccess, TFailure extends Enum<TFailure>> {
    private boolean mIsSuccessful;
    public boolean isSuccessful() {
        return mIsSuccessful;
    }

    private TSuccess mResult;
    public TSuccess getResult() {
        return mResult;
    }

    private TFailure mFailure;
    public TFailure getFailure() { // TODO: Consider using try and catch instead of returning null
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
