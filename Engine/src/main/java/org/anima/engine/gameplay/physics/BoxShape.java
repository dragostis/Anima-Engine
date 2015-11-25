package org.anima.engine.gameplay.physics;

import org.anima.engine.linearmath.Vector;

public class BoxShape implements PrimitiveShape {
    private Vector size;

    public BoxShape(Vector size) {
        this.size = size;
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    @Override
    public long getPointer() {
        return getBoxShapePointer(size.getX() / 2.0f, size.getY() / 2.0f, size.getZ() / 2.0f);
    }

    private native long getBoxShapePointer(float x, float y, float z);
}
