package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class AnticipateOvershootInterpolator extends Interpolator {
    private final float tension;

    public AnticipateOvershootInterpolator(float tension) {
        this.tension = tension;
    }

    @Override
    public float interpolate(float ratio) {
        float doubleRatio = 2 * ratio;

        if (ratio < 0.5f) {
            return (float) (0.5f * ((tension + 1) * Math.pow(doubleRatio, 3) - tension * Math.pow(doubleRatio, 2)));
        }

        return (float) (0.5f * ((tension + 1) * Math.pow(doubleRatio - 2, 3)
                + tension * Math.pow(doubleRatio - 2, 2)) + 1);
    }
}
