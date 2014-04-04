package com.ideas.anima.engine.graphics;

import android.opengl.Matrix;

/**
 * 2D and 3D vector class.
 *
 * @author Dragos Tiselice
 * @version 1.0
 */

public class Vector {
    /**
     * The coordinate on the x axis.
     */
    public float x;
    /**
     * The coordinate on the y axis.
     */
    public float y;
    /**
     * The coordinate on the z axis.
     */
    public float z;

    /**
     * Creates a 3D vector.
     * <p/>
     * Note: The y axis is the height.
     *
     * @param x coordinate on the x axis
     * @param y coordinate on the y axis
     * @param z coordinate on the z axis
     */
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a 2D vector.
     *
     * @param x coordinate on the x axis
     * @param y coordinate on the y axis
     */
    public Vector(float x, float y) {
        this(x, y, 0.0f);
    }

    /**
     * Creates a null vector.
     * <p/>
     * It has the value 0.0f on all coordinates.
     */
    public Vector() {
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     * Creates a 3D vector with the same coordinate on every axis.
     *
     * @param a coordinate
     */
    public Vector(float a) {
        this(a, a, a);
    }

    /**
     * Adds two vectors.
     *
     * @param a vector to be added
     * @param b vector to be added
     * @return sum of the vectors
     */
    public static Vector add(Vector a, Vector b) {
        Vector c = new Vector();

        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;

        return c;
    }

    /**
     * Subtracts two vectors.
     *
     * @param a vector to be subtracted from
     * @param b vector to subtract
     * @return difference of the vectors
     */
    public static Vector subtract(Vector a, Vector b) {
        Vector c = new Vector();

        c.x = a.x - b.x;
        c.y = a.y - b.y;
        c.z = a.z - b.z;

        return c;
    }

    /**
     * Computes the dot product between two vectors.
     *
     * @param a vector for the dot product
     * @param b vector for the dot product
     * @return dot product
     */
    public static float dot(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /**
     * Multiplies the given vector with the given scalar.
     *
     * @param a      vector to be multiplied
     * @param scalar scalar to multiply the vector with
     * @return multiplied vector
     */
    public static Vector multiply(Vector a, float scalar) {
        Vector b = new Vector();

        b.x = a.x * scalar;
        b.y = a.y * scalar;
        b.z = a.z * scalar;

        return b;
    }

    /**
     * Computes the cross product between two given vectors.
     *
     * @param a vector for the cross product
     * @param b vector for the cross product
     * @return cross product of the vectors
     */
    public static Vector cross(Vector a, Vector b) {
        Vector c = new Vector();

        c.x = a.y * b.z - a.z * b.y;
        c.y = a.z * b.x - a.x * b.z;
        c.z = a.x * b.y - a.y * b.x;

        return c;
    }

    /**
     * Normalizes a given vector.
     *
     * @param a vector to be normalized
     * @return normalized vector
     */
    public static Vector normalize(Vector a) {
        Vector b = new Vector();

        float length = a.length();

        if (length != 0.0f) {
            b.x = a.x / length;
            b.y = a.y / length;
            b.z = a.z / length;
        }

        return b;
    }

    /**
     * Transforms a given 3D vector to a 2D one.
     * <p/>
     * It swaps the y and z coordinates and set z to 0.0f.
     *
     * @param a vector to be transformed
     * @return transformed vector
     */
    public static Vector toPlane(Vector a) {
        return new Vector(a.x, a.z);
    }

    /**
     * Transforms a given 2D vector to a 3D one.
     * <p/>
     * It swaps the y and z coordinates and set y to 0.0f.
     *
     * @param a vector to be transformed
     * @return transformed vector
     */
    public static Vector toSpace(Vector a) {
        return new Vector(a.x, 0.0f, a.y);
    }

    /**
     * Creates a new instance of the vector.
     *
     * @return new instance of the vector
     */
    public Vector duplicate() {
        return new Vector(x, y, z);
    }

    /**
     * Adds the values given to a vector.
     *
     * @param x value to be added to the x coordinate
     * @param y value to be added to the y coordinate
     * @param z value to be added to the z coordinate
     * @return instance of the vector
     */
    public Vector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    /**
     * Adds the vector given to a vector.
     *
     * @param a vector to be added
     * @return instance of the vector
     */
    public Vector add(Vector a) {
        x += a.x;
        y += a.y;
        z += a.z;

        return this;
    }

    /**
     * Subtracts the values given from a vector.
     *
     * @param x value to be subtracted from the x coordinate
     * @param y value to be subtracted from the y coordinate
     * @param z value to be subtracted from the z coordinate
     * @return instance of the vector
     */
    public Vector subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    /**
     * Subtracts the vector given from a vector.
     *
     * @param a vector to be subtracted
     * @return instance of the vector
     */
    public Vector subtract(Vector a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;

        return this;
    }

    /**
     * Computes the dot product from a vector and the given one.
     *
     * @param a vector for the dot product
     * @return dot product of the vectors
     */
    public float dot(Vector a) {
        return x * a.x + y * a.y + z * a.z;
    }

    /**
     * Computes the dot product from a vector and the given one and clips them
     * between -1.0f and 1.0f.
     *
     * @param a vector for the dot product
     * @return dot product of the vectors clipped between -1.0f and 1.0f
     */
    public float dotClipped(Vector a) {
        float dot = this.dot(a);

        if (dot > 1.0f) dot = 1.0f;
        if (dot < -1.0f) dot = -1.0f;

        return dot;
    }

    /**
     * Multiplies a vector with the given scalar.
     *
     * @param scalar scalar to be multiplied with a vector
     * @return instance of the vector
     */
    public Vector multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;

        return this;
    }

    /**
     * Computes the length of a vector.
     *
     * @return the length of the vector
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes a vector.
     *
     * @return instance of the vector
     */
    public Vector normalize() {
        float length = this.length();

        if (length != 0.0f) {
            x /= length;
            y /= length;
            z /= length;
        }

        return this;
    }

    /**
     * Computes the distance from a vector to a given one.
     *
     * @param a vector to compute the distance from
     * @return value of the distance
     */
    public float distance(Vector a) {
        return subtract(this, a).length();
    }

    /**
     * Computes the up vector for a look vector.
     * <p/>
     * It finds the vector that is orthogonal to the look vector and that forms
     * the smallest angle with the y axis.
     *
     * @return up vector
     */
    public Vector getUpVector() {
        float[] matrix = new float[16];

        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, 1.0f, 0.0f, 1.0f, 0.0f);

        Vector secondary = this.duplicate().transformPointByMatrix(matrix);

        return Vector.cross(this, secondary).normalize();
    }

    /**
     * Transforms a 3D point by a given matrix.
     *
     * @param matrix matrix to transform a point by
     * @return result of the transformation
     */
    public Vector transformPointByMatrix(float[] matrix) {
        float[] vec = {x, y, z, 1.0f};

        Matrix.multiplyMV(vec, 0, matrix, 0, vec, 0);

        x = vec[0] / vec[3];
        y = vec[1] / vec[3];
        z = vec[2] / vec[3];

        return this;
    }

    /**
     * Transforms a 3D line by a given matrix.
     *
     * @param matrix matrix to transform a line by
     * @return result of the transformation
     */
    public Vector transformDirectionByMatrix(float[] matrix) {
        float[] vec = {x, y, z, 0.0f};

        Matrix.multiplyMV(vec, 0, matrix, 0, vec, 0);

        x = vec[0];
        y = vec[1];
        z = vec[2];

        return this;
    }

    /**
     * Transforms a 3D vector to a 2D one.
     * <p/>
     * It swaps the y and z coordinates and set z to 0.0f.
     *
     * @return transformed vector
     */
    public Vector toPlane() {
        return new Vector(x, z);
    }

    /**
     * Transforms a 2D vector to a 3D one.
     * <p/>
     * It swaps the y and z coordinates and set y to 0.0f.
     *
     * @return transformed vector
     */
    public Vector toSpace() {
        return new Vector(x, 0.0f, y);
    }
}