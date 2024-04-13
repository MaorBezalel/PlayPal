package com.hit.playpal.utils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

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

    /**
     * Constructor for a successful use case with no result.
     */
    public UseCaseResult() {
        mResult = null;
        mIsSuccessful = true;
        mFailure = null;
    }

    /**
     * Constructor for a successful use case with a result.
     * @param iResult The result of the use case upon success.
     */
    public UseCaseResult(TSuccess iResult) {
        mResult = iResult;
        mIsSuccessful = true;
        mFailure = null;
    }

    /**
     * Constructor for a failed use case.
     * @param iFailure The result of the use case upon failure.
     */
    public UseCaseResult(TFailure iFailure) {
        mResult = null;
        mIsSuccessful = false;
        mFailure = iFailure;
    }

    /**
     * <p>Factory method for creating a successful <code>UseCaseResult</code> without a result.</p>
     * @param <Void> The type of the result upon success (empty type).
     * @param <TFailure> The type of the result upon failure. Must be an enum (indicating the type of failure).
     * @return A successful <code>UseCaseResult</code> without a result.
     */
    @NonNull
    @Contract(value = " -> new", pure = true)
    public static <Void, TFailure extends Enum<TFailure>> UseCaseResult<Void, TFailure> forSuccessWithoutResult() {
        return new UseCaseResult<>();
    }

    /**
     * <p>Factory method for creating a successful <code>UseCaseResult</code></p>
     * @param iResult The result of the use case upon success.
     * @param <TSuccess> The type of the result upon success.
     * @param <TFailure> The type of the result upon failure. Must be an enum (indicating the type of failure).
     * @return A successful <code>UseCaseResult</code> with the given result.
     */
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <TSuccess, TFailure extends Enum<TFailure>> UseCaseResult<TSuccess, TFailure> forSuccess(TSuccess iResult) {
        return new UseCaseResult<>(iResult);
    }

    /**
     * <p>Factory method for creating a failed <code>UseCaseResult</code></p>
     * @param iFailure The result of the use case upon failure.
     * @param <TSuccess> The type of the result upon success.
     * @param <TFailure> The type of the result upon failure. Must be an enum (indicating the type of failure).
     * @return A failed <code>UseCaseResult</code> with the given failure.
     */
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <TSuccess, TFailure extends Enum<TFailure>> UseCaseResult<TSuccess, TFailure> forFailure(TFailure iFailure) {
        return new UseCaseResult<>(iFailure);
    }
}
