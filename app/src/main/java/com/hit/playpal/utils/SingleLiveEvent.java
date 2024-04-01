package com.hit.playpal.utils;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <p>A lifecycle-aware observable that sends only new updates after subscription, used for events like navigation and Snackbar messages.</p>
 * <p>This avoids common issue where an event is still active but the observer is recreated, leading to an event being emitted second time.</p>
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(LifecycleOwner iOwner, Observer<? super T> iObserver) {
        super.observe(iOwner, t -> {
            if (mPending.compareAndSet(true, false)) {
                iObserver.onChanged(t);
            }
        });
    }

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