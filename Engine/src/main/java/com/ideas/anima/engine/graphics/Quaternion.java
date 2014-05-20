package com.ideas.anima.engine.graphics;

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

        x = -direction.x * sin;
        y =  direction.y * sin;
        z =  direction.z * sin;
        w =  (float) Math.cos(Math.toRadians(angle / 2.0f));
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
        x = quaternion.w * x + quaternion.x * w + quaternion.y * z - quaternion.z * y;
        y = quaternion.w * y + quaternion.x * z + quaternion.y * w - quaternion.z * x;
        z = quaternion.w * z + quaternion.x * y + quaternion.y * x - quaternion.z * w;
        w = quaternion.w * w + quaternion.x * x + quaternion.y * y - quaternion.z * z;
    }

    public float[] getRotationMatrix() {
        float[] matrix = new float[16];

        matrix[0]  = 1.0f - 2.0f * (y * y + z * z);
        matrix[1]  = 2.0f * (x * y + z * w);
        matrix[2]  = 2.0f * (x * z - y * w);
        matrix[3]  = 0.0f;
        matrix[4]  = 2.0f * (x * y - z * w);
        matrix[5]  = 1.0f - 2.0f * (x * x + z * z);
        matrix[6]  = 2.0f * (y * z + x * w);
        matrix[7]  = 0.0f;
        matrix[8]  = 2.0f * (x * z + y * w);
        matrix[9]  = 2.0f * (y * z - x * w);
        matrix[10] = 1.0f - 2.0f * (x * x + y * y);
        matrix[11] = 0.0f;
        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;

        return matrix;
    }
}
