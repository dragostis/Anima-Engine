package com.ideas.anima.engine;

import java.util.Stack;

public class Pool<T> {
    private final Stack<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;

        freeObjects = new Stack<>();
    }

    public T newObject() {
        if (freeObjects.size() == 0) {
            return factory.createObject();
        } else {
            return freeObjects.pop();
        }
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize) freeObjects.add(object);
    }

    public interface PoolObjectFactory<T> {
        public T createObject();
    }
}