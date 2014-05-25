package com.ideas.anima.engine.gameplay.interpolators;

public abstract class Interpolator {
    private Interpolatable[] interpolatables;
    private float totalTime;
    private float currentTime;

    public Interpolator(float totalTime, Interpolatable... interpolatables) {
        this.interpolatables = interpolatables;
        this.totalTime = totalTime;
    }

    public void update(float deltaTime) {
        currentTime += deltaTime;

        if (currentTime <= totalTime) interpolate(currentTime / totalTime);
    }

    protected abstract void interpolate(float ratio);
}
