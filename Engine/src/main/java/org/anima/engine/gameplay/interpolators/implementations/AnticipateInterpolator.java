package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class AnticipateInterpolator extends Interpolator {
    private final float tension;

    public AnticipateInterpolator(float tension) {
        this.tension = tension;
    }

    @Override
    public float interpolate(float ratio) {
        // returns [-1; 1]
        return (float) ((tension + 1) * Math.pow(ratio, 3) - tension * Math.pow(ratio, 2));
    }
}
