package org.anima.engine.gameplay.interpolators;

public abstract class Interpolatable<T> {
    private final Class<T> type;
    protected Class<? extends Interpolatable<T>> t;

    public Interpolatable(Class<T> type) {
        this.type = type;
    }

    public Class<T> getMyType() {
        return this.type;
    }

    public abstract Interpolatable<T> fraction(float ratio);
    public abstract Interpolatable<T> combine(Interpolatable<T> interpolatable, float ratio1, float ratio2);
}
