package com.hit.playpal.home.adapters;

public interface IBindableGame<T> {
    String getTitle(T item);
    float getRating(T item);
    String getBackgroundImage(T item);
    String getId(T item);
}
