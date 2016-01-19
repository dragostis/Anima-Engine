package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class DecelerateInterpolator extends Interpolator {
    private final float factor;

    public DecelerateInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float interpolate(float ratio) {
        return (float) Math.pow(1 - (1 - ratio), Math.E * factor);
    }
}
