package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class AccelerateDecelerateInterpolator extends Interpolator {
    private final AccelerateInterpolator accelerateInterpolator;
    private final DecelerateInterpolator decelerateInterpolator;

    public AccelerateDecelerateInterpolator(float factor) {
        accelerateInterpolator = new AccelerateInterpolator(factor);
        decelerateInterpolator = new DecelerateInterpolator(factor);
    }

    @Override
    public float interpolate(float ratio) {
        float newRatio;

        if (ratio <= 0.5f) {
            newRatio = ratio * 2.0f;
        } else {
            newRatio = 2.0f - ratio * 2.0f;
        }

        if (ratio <= 0.5f) {
            return accelerateInterpolator.interpolate(newRatio) / 2.0f;
        } else {
            return decelerateInterpolator.interpolate(newRatio) / 2.0f + 0.5f;
        }
    }
}
