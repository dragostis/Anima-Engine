package org.anima.engine.gameplay.interpolators.implementations;

import org.anima.engine.gameplay.interpolators.Interpolator;

public class AccelerateInterpolator extends Interpolator {
    private final float factor;

    public AccelerateInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float interpolate(float ratio) {
        return (float) Math.pow(ratio, Math.pow(Math.E, factor));
    }
}
