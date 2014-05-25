package com.ideas.anima.engine.gameplay.interpolators;

public interface Interpolatable<T> {
    public void combine(float ratio1, Interpolatable<T> object2, float ratio2);
}
