package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

public class Path {
    protected boolean hasArrived;
    private Vector start;
    private Vector finish;
    private Vector curve;
    private float length;

    public Path(Vector start, Vector finish, Vector curve) {
        this.start = start;
        this.finish = finish;
        this.curve = curve;

        length = computeIntegral();
    }

    public Path(Vector start, Vector finish) {
        this.start = start;
        this.finish = finish;

        length = Vector.subtract(finish, start).length();
    }

    public Path() {

    }

    public Vector getPosition(float pathPosition) {
        Vector position;

        pathPosition /= length;

        if (pathPosition > 1.0f) {
            pathPosition = 1.0f;
            hasArrived = true;
        }

        if (curve == null) {
            position = Vector.add(Vector.multiply(start, (1.0f - pathPosition)),
                    Vector.multiply(finish, pathPosition));
        }
        else {
            position = Vector.multiply(start, (1.0f - pathPosition) * (1.0f - pathPosition));
            position.add(Vector.multiply(curve, 2.0f * (1.0f - pathPosition) * pathPosition));
            position = Vector.add(position, Vector.multiply(finish, pathPosition * pathPosition));
        }

        return position;
    }

    public Vector getTangent(float pathPosition) {
        Vector angle;
        Vector result = new Vector();

        pathPosition /= length;

        if (pathPosition > 1.0f) {
            pathPosition = 1.0f;
            hasArrived = true;
        }

        if (curve == null)
            angle = Vector.normalize(Vector.subtract(finish, start));

        else {
            angle = Vector.multiply(Vector.subtract(curve, start), 2.0f * (1.0f - pathPosition));
            angle.add(Vector.multiply(Vector.subtract(finish, curve), 2.0f * pathPosition));
        }

        angle.normalize();

        Vector angleY = angle.duplicate();
        angleY.y = 0.0f;
        angleY.normalize();

        result.z = (float) (Math.acos(angle.dotClipped(angleY)) * 180.0 / Math.PI);

        if (angle.y < 0.0f) result.z = 360.0f - result.z;

        result.y = (float) (Math.acos(angleY.dotClipped(new Vector(1.0f, 0.0f, 0.0f))) * 180.0 / Math.PI);

        if (angle.z > 0.0f) result.y = 360.0f - result.y;

        return result;
    }

    private float computeIntegral() {
        Vector a = new Vector();
        Vector b = new Vector();

        a.x = start.x - 2.0f * curve.x + finish.x;
        a.y = start.y - 2.0f * curve.y + finish.y;
        a.z = start.z - 2.0f * curve.z + finish.z;

        b.x = 2.0f * (curve.x - start.x);
        b.y = 2.0f * (curve.y - start.y);
        b.z = 2.0f * (curve.z - start.z);

        float A = 4.0f * (a.x * a.x + a.y * a.y + a.z * a.z);
        float B = 4.0f * (a.x * b.x + a.y * b.y + a.z * b.z);
        float C = b.x * b.x + b.y * b.y + b.z * b.z;

        float sABC = 2.0f * (float) Math.sqrt(A + B + C);
        float sA = (float) Math.sqrt(A);
        float sA3 = 2 * A * sA;
        float sC = 2.0f * (float) Math.sqrt(C);
        float BA = B / sA;
        float L = (float) Math.log((double) (2.0f * sA + BA + sABC) / (BA + sC));

        return (sA3 * sABC + sA * B * (sABC - sC) + (4.0f * C * A - B * B) * L) / (4.0f * sA3);
    }

    public float getLength() {
        return length;
    }

    public boolean hasArrived() {
        return hasArrived;
    }
}
