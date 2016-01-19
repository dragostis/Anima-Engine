package org.anima.engine.gameplay.interpolators;

public abstract class Interpolator {
    public abstract float interpolate(float ratio);

    public <T> Interpolatable<T> fraction(Interpolatable<T> interpolatable, float ratio) {
        return interpolatable.fraction(ratio);
    }

    public <T> Interpolatable combine(Interpolatable<T> interpolatable1, Interpolatable<T> interpolatable2,
                                  float ratio1, float ratio2) {
        return interpolatable1.combine(interpolatable2, ratio1, ratio2);
    }

    public CombinedInterpolator compose(Interpolator interpolator) {
        return new CombinedInterpolator(this, interpolator);
    }
}
