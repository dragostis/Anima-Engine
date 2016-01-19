package org.anima.engine.gameplay.interpolators;

public class CombinedInterpolator extends Interpolator {
    private final Interpolator interpolator1;
    private final Interpolator interpolator2;

    public CombinedInterpolator(Interpolator interpolator1, Interpolator interpolator2) {
        this.interpolator1 = interpolator1;
        this.interpolator2 = interpolator2;
    }

    @Override
    public float interpolate(float ratio) {
        return interpolator2.interpolate(interpolator1.interpolate(ratio));
    }
}
