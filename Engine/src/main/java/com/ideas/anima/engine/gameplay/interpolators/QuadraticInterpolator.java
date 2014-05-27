package com.ideas.anima.engine.gameplay.interpolators;

import com.ideas.anima.engine.gameplay.Interpolator;

public class QuadraticInterpolator<T> extends Interpolator {
    private Interpolatable<T> object1;
    private Interpolatable<T> object2;
    private Interpolatable<T> object3;
    private Interpolatable<T> result;

    public QuadraticInterpolator(float totalTime, Interpolatable<T> object1,
            Interpolatable<T> object2, Interpolatable<T> object3, Interpolatable<T> result) {
        super(totalTime);

        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
        this.result = result;
    }

    @Override
    protected void interpolate(float ratio) {
        result.combine(0.0f, object1, 1.0f);

        result.combine((1.0f - ratio) * (1.0f - ratio), object2, 2.0f * (1.0f - ratio) * ratio);
        result.combine(1.0f, object3, ratio * ratio);
    }
}