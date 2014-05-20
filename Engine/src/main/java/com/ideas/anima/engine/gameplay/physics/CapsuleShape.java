package com.ideas.anima.engine.gameplay.physics;

public class CapsuleShape implements PrimitiveShape {
    private float radius;
    private float height;

    public CapsuleShape(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public long getPointer() {
        return getCapsuleShapePointer(radius, height);
    }

    private native long getCapsuleShapePointer(float radius, float height);
}
