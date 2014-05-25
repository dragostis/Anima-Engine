package com.ideas.anima.engine.gameplay.interpolators;

public interface Interpolatable<T> {
    public void multiply(float scalar);

    public void add(T object);
}