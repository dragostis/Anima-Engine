package com.ideas.anima.engine.linearmath;

public class Matrix {
    private float[] array = new float[16];
    private float[] operation = new float[16];
    private float[] result = new float[16];

    public Matrix() {
        array[0] = array[5] = array[10] = array[15] = 1.0f;
    }

    public Matrix(Matrix matrix) {
        System.arraycopy(matrix.getArray(), 0, array, 0, 16);
    }

    public float[] getArray() {
        return array;
    }

    public Matrix multiply(Matrix matrix) {
        android.opengl.Matrix.multiplyMM(result, 0, matrix.getArray(), 0, array, 0);

        System.arraycopy(result, 0, array, 0, 16);

        return this;
    }

    public Matrix invert() {
        android.opengl.Matrix.invertM(result, 0, array, 0);

        System.arraycopy(result, 0, array, 0, 16);

        return this;
    }

    public Matrix translate(Vector translation) {
        android.opengl.Matrix.setIdentityM(operation, 0);
        android.opengl.Matrix.translateM(operation, 0, translation.getX(), translation.getY(),
                translation.getZ());
        
        android.opengl.Matrix.multiplyMM(result, 0, operation, 0, array, 0);

        System.arraycopy(result, 0, array, 0, 16);
        
        return this;
    }

    public Matrix rotate(Quaternion rotation) {
        setRotationMatrix(rotation);

        android.opengl.Matrix.multiplyMM(result, 0, operation, 0, array, 0);

        System.arraycopy(result, 0, array, 0, 16);

        return this;
    }

    public Matrix scale(Vector scale) {
        android.opengl.Matrix.setIdentityM(operation, 0);
        android.opengl.Matrix.scaleM(operation, 0, scale.getX(), scale.getY(), scale.getZ());

        android.opengl.Matrix.multiplyMM(result, 0, operation, 0, array, 0);

        System.arraycopy(result, 0, array, 0, 16);

        return this;
    }

    public Matrix view(Vector eye, Vector look, Vector up) {
        android.opengl.Matrix.setLookAtM(
                array,
                0,
                eye.getX(),
                eye.getY(),
                eye.getZ(),
                look.getX(),
                look.getY(),
                look.getZ(),
                up.getX(),
                up.getY(),
                up.getZ()
        );

        return this;
    }

    public Matrix frustum(float left, float right, float bottom, float top, float near, float far) {
        android.opengl.Matrix.frustumM(array, 0, left, right, bottom, top, near, far);

        return this;
    }

    public Matrix ortho(float left, float right, float bottom, float top, float near, float far) {
        android.opengl.Matrix.orthoM(array, 0, left, right, bottom, top, near, far);

        return this;
    }

    public Vector tranformPoint(Vector point) {
        Vector result = new Vector();

        android.opengl.Matrix.multiplyMV(result.getArray(), 0, array, 0, point.getArray(), 0);

        return result.normalizeW();
    }

    public Vector tranformDirection(Vector direction) {
        Vector result = new Vector();

        direction.setW(0.0f);

        android.opengl.Matrix.multiplyMV(result.getArray(), 0, array, 0, direction.getArray(), 0);

        return result;
    }

    private void setRotationMatrix(Quaternion rotation) {
        float x = rotation.getX();
        float y = rotation.getY();
        float z = rotation.getZ();
        float w = rotation.getW();

        operation[0]  = 1.0f - 2.0f * (y * y + z * z);
        operation[1]  = 2.0f * (x * y + z * w);
        operation[2]  = 2.0f * (x * z - y * w);
        operation[3]  = 0.0f;
        operation[4]  = 2.0f * (x * y - z * w);
        operation[5]  = 1.0f - 2.0f * (x * x + z * z);
        operation[6]  = 2.0f * (y * z + x * w);
        operation[7]  = 0.0f;
        operation[8]  = 2.0f * (x * z + y * w);
        operation[9]  = 2.0f * (y * z - x * w);
        operation[10] = 1.0f - 2.0f * (x * x + y * y);
        operation[11] = 0.0f;
        operation[12] = 0.0f;
        operation[13] = 0.0f;
        operation[14] = 0.0f;
        operation[15] = 1.0f;
    }
}
