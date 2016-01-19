package org.anima.engine.gameplay.interpolators.implementations;


import org.anima.engine.gameplay.interpolators.Interpolator;

public class LinearInterpolator extends Interpolator {
    @Override
    public float interpolate(float ratio) {
        return ratio;
    }
}
