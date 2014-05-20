package com.ideas.anima.engine.gameplay.physics;

public class SphereShape implements PrimitiveShape {
    private float radius;

    public SphereShape(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public long getPointer() {
        return getSphereShapePointer(radius);
    }

    private native long getSphereShapePointer(float radius);
}
