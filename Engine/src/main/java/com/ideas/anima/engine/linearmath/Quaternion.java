package com.ideas.anima.engine.linearmath;

public class Quaternion {
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion() {
        this(new Vector(1.0f, 0.0f, 0.0f), 0.0f);
    }

    public Quaternion(Vector direction, float angle) {
        direction.normalize();

        float sin = (float) Math.sin(Math.toRadians(angle / 2.0f));

        x = direction.getX() * sin;
        y = direction.getY() * sin;
        z = direction.getZ() * sin;
        w = (float) Math.cos(Math.toRadians(angle / 2.0f));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void multiply(Quaternion quaternion) {
        float newX = quaternion.w * x + quaternion.x * w + quaternion.y * z - quaternion.z * y;
        float newY = quaternion.w * y - quaternion.x * z + quaternion.y * w + quaternion.z * x;
        float newZ = quaternion.w * z + quaternion.x * y - quaternion.y * x + quaternion.z * w;
        float newW = quaternion.w * w - quaternion.x * x - quaternion.y * y - quaternion.z * z;

        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;

    }
}
