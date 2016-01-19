package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class OvershootInterpolator extends Interpolator {
    private final float tension;

    public OvershootInterpolator(float tension) {
        this.tension = tension;
    }

    @Override
    public float interpolate(float ratio) {
        return (float) ((tension + 1) * Math.pow(ratio - 1, 3) + tension * Math.pow(ratio - 1, 2) + 1);
    }
}
