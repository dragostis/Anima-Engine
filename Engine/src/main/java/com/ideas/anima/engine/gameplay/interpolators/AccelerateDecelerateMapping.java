package com.ideas.anima.engine.gameplay.interpolators;

public class AccelerateDecelerateMapping implements Mapping {
    private float acceleration;

    public AccelerateDecelerateMapping(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public float map(float ratio) {
        float newRatio = new CircuitMapping().map(ratio);

        if (ratio <= 0.5f) {
            return new AccelerateMapping(acceleration).map(newRatio) / 2.0f;
        } else {
            return new AccelerateMapping(-acceleration).map(newRatio) / 2.0f + 0.5f;
        }
    }
}
