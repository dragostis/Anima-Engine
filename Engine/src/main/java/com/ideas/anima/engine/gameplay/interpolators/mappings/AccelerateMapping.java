package com.ideas.anima.engine.gameplay.interpolators.mappings;

import com.ideas.anima.engine.gameplay.interpolators.Mapping;

public class AccelerateMapping implements Mapping {
    private float acceleration;

    public AccelerateMapping(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public float map(float ratio) {
        return (float) Math.pow(ratio, Math.pow(Math.E, acceleration));
    }
}
