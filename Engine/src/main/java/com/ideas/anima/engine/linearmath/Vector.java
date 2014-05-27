package com.ideas.anima.engine.linearmath;

public class Vector {
    private float[] array = new float[4];

    public Vector(float x, float y, float z) {
        array[0] = x;
        array[1] = y;
        array[2] = z;
        array[3] = 1.0f;
    }

    public Vector(Vector vector) {
        array[0] = vector.getX();
        array[1] = vector.getY();
        array[2] = vector.getZ();
        array[3] = vector.getW();
    }

    public Vector(float x, float y) {
        this(x, y, 0.0f);
    }

    public Vector() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector(float a) {
        this(a, a, a);
    }

    public float getX() {
        return array[0];
    }

    public float getY() {
        return array[1];
    }

    public float getZ() {
        return array[2];
    }

    public float getW() {
        return array[3];
    }

    public void setX(float x) {
        array[0] = x;
    }

    public void setY(float y) {
        array[1] = y;
    }

    public void setZ(float z) {
        array[2] = z;
    }

    public void setW(float w) {
        array[3] = w;
    }

    public float[] getArray() {
        return array;
    }

    public Vector add(Vector a) {
        return new Vector(array[0] + a.getX(), array[1] + a.getY(), array[2] + a.getZ());
    }

    public Vector subtract(Vector a) {
        return new Vector(array[0] - a.getX(), array[1] - a.getY(), array[2] - a.getZ());
    }

    public float dot(Vector a) {
        return array[0] * a.getX() + array[1] * a.getY() + array[2] * a.getZ();
    }

    public Vector multiply(float scalar) {
        return new Vector(array[0] * scalar, array[1] * scalar, array[2] * scalar);
    }

    public Vector cross(Vector a) {
        return new Vector(
            array[1] * a.getZ() - array[2] * a.getY(),
            array[2] * a.getX() - array[0] * a.getZ(),
            array[0] * a.getY() - array[1] * a.getX()
        );
    }

    public float length() {
        return (float) Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2]);
    }

    public Vector normalize() {
        float length = length();

        return new Vector(array[0] / length, array[1] / length, array[2] / length);
    }

    public float distance(Vector a) {
        return subtract(a).length();
    }

    public Vector toPlane() {
        return new Vector(array[0], array[2]);
    }

    public Vector toSpace() {
        return new Vector(array[0], 0.0f, array[1]);
    }
}