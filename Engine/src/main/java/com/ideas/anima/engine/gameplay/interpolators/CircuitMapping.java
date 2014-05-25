package com.ideas.anima.engine.gameplay.interpolators;

public class CircuitMapping implements Mapping {
    @Override
    public float map(float ratio) {
        if (ratio <= 0.5f) {
            return ratio * 2.0f;
        } else {
            return 2.0f - ratio * 2.0f;
        }
    }
}
