package com.ideas.anima.engine;

import java.util.ArrayList;
import java.util.List;

public class Pool<Type> {
    private final List<Type> freeObjects;
    private final PoolObjectFactory<Type> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<Type> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        freeObjects = new ArrayList<Type>(maxSize);
    }

    public Type newObject() {
        Type object;

        if (freeObjects.size() == 0) object = factory.createObject();
        else object = freeObjects.remove(freeObjects.size() - 1);

        return object;
    }

    public void free(Type object) {
        if (freeObjects.size() < maxSize) freeObjects.add(object);
    }

    public interface PoolObjectFactory<Type> {
        public Type createObject();
    }
}