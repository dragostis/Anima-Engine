package org.anima.engine.gameplay.physics;

import org.anima.engine.gameplay.GameObject;

public class Body {
    private Type type;
    private Shape shape;
    private float mass;
    private long pointer;
    private GameObject object;

    public Body(Type type, Shape shape, float mass) {
        this.type = type;
        this.shape = shape;
        this.mass = mass;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public long getPointer() {
        if (pointer != 0) return pointer;

        float[] values = new float[7];

        values[0] = object.getPosition().getX();
        values[1] = object.getPosition().getY();
        values[2] = object.getPosition().getZ();

        values[3] = object.getRotation().getX();
        values[4] = object.getRotation().getY();
        values[5] = object.getRotation().getZ();
        values[6] = object.getRotation().getW();

        return pointer = getBodyPointer(type.ordinal(), shape.getPointer(), mass, values);
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    private native long getBodyPointer(int type, long shape, float mass, float[] values);

    public static enum Type {
        STATIC,
        KINEMATIC,
        DYNAMIC
    }
}
