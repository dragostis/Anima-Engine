package com.ideas.anima.engine.gameplay.physics;

public class ConeShape implements PrimitiveShape {
    private float radius;
    private float height;

    public ConeShape(float radius, float height) {
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
        return getConeShapePointer(radius, height);
    }

    private native long getConeShapePointer(float radius, float height);
}
