package org.anima.engine.gameplay.interpolators.implementations;

import org.anima.engine.gameplay.interpolators.Interpolator;

public class BounceInterpolator extends Interpolator {
    @Override
    public float interpolate(float ratio) {
        if (ratio < 0.31489f) {
            return (float) (8 * Math.pow((1.1226f * ratio), 2));
        } else if ( 0.31489f <= ratio && ratio < 0.65990f ) {
            return (float) (8 * Math.pow(1.1226f * ratio - 0.54719f, 2) + 0.7f);
        } else if ( 0.65990f <= ratio && ratio < 0.85908f ) {
            return (float) (8 * Math.pow(1.1226f * ratio - 0.8526f, 2) + 0.9f);
        } else if ( 0.85908f <= ratio) {
            return (float) (8 * Math.pow(1.1226f * ratio - 1.0435f, 2) + 0.95f);
        }

        return 0;
    }
}
