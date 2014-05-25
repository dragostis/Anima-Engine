package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.gameplay.interpolators.Interpolatable;
import com.ideas.anima.engine.gameplay.interpolators.Mapping;

import java.util.ArrayList;
import java.util.List;

public abstract class Interpolator {
    private Interpolatable[] interpolatables;
    private List<Mapping> mappings = new ArrayList<>();
    private float totalTime;
    private float currentTime;

    public Interpolator(float totalTime, Interpolatable... interpolatables) {
        this.interpolatables = interpolatables;
        this.totalTime = totalTime;
    }

    public Interpolator map(Mapping mapping) {
        mappings.add(mapping);

        return this;
    }

    public void update(float deltaTime) {
        currentTime += deltaTime;

        if (currentTime <= totalTime) {
            float ratio = currentTime / totalTime;

            for (Mapping mapping : mappings) ratio = mapping.map(ratio);

            interpolate(ratio);
        }
    }

    protected abstract void interpolate(float ratio);
}
