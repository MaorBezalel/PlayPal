package com.hit.playpal.home.adapters.interfaces;

public interface IBindableGame<T> {
    String getTitle(T item);
    float getRating(T item);
    String getBackgroundImage(T item);
    String getId(T item);
}
