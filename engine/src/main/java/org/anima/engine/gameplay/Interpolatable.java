package org.anima.engine.gameplay;

public interface Interpolatable<T> {
    public void multiply(float scalar);

    public void add(T object);
}
