package com.hit.playpal.utils;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A lifecycle-aware observable that sends only new updates after subscription.
 * This is used for events like navigation and Snackbar messages.
 * This avoids a common issue where an event is still active but the observer is recreated,
 * leading to an event being emitted a second time.
 *
 * @param <T> The type of data held by this instance
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    /**
     * Atomic boolean to handle pending state.
     */
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    /**
     * Observe the LiveData, LifecycleOwner aware.
     *
     * @param iOwner The LifecycleOwner which controls the observer
     * @param iObserver The observer that will receive the events
     */
    @MainThread
    public void observe(LifecycleOwner iOwner, Observer<? super T> iObserver) {
        super.observe(iOwner, t -> {
            if (mPending.compareAndSet(true, false)) {
                iObserver.onChanged(t);
            }
        });
    }

    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     *
     * @param iValue The new value
     */
    @MainThread
    public void setValue(@Nullable T iValue) {
        mPending.set(true);
        super.setValue(iValue);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}