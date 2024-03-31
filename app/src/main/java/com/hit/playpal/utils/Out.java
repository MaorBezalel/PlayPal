package com.hit.playpal.utils;

import androidx.annotation.NonNull;

import com.hit.playpal.auth.data.repositories.AuthRepository;

import org.jetbrains.annotations.Contract;

/**
 * <p>A simple class to hold a value that can be modified by a method.</p>
 * <p>It can be used to return additional values from a method (like C# <code>out</code> keyword).</p>
 * @param <T> The type of the value to hold.
 */
public class Out<T> {
    private T mValue;
    public T get() { return mValue; }
    public void set(T iValue) { mValue = iValue; }

    public Out() { mValue = null; }
    public Out(T iValue) { mValue = iValue; }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> Out<T> of(Class<T> iClass) {
        return new Out<>();
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public static <T> Out<T> notRequired() {
        return new Out<>();
    }
}