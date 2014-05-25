package com.ideas.anima.engine.gameplay.interpolators;

public class ReverseMapping implements Mapping {
    @Override
    public float map(float ratio) {
        return 1.0f - ratio;
    }
}
