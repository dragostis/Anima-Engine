package com.ideas.anima.engine.gameplay.interpolators;

import com.ideas.anima.engine.gameplay.Interpolator;

public class LinearInterpolator<T> extends Interpolator {
    private Interpolatable<T> object1;
    private Interpolatable<T> object2;
    private Interpolatable<T> result;

    public LinearInterpolator(float totalTime, Interpolatable<T> object1, Interpolatable<T> object2,
            Interpolatable<T> result) {
        super(totalTime);

        this.object1 = object1;
        this.object2 = object2;
        this.result = result;
    }

    @Override
    protected void interpolate(float ratio) {
        result.combine(0.0f, object1, 1.0f);

        result.combine(1.0f - ratio, object2, ratio);
    }
}
