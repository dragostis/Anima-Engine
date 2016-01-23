package org.anima.engine.gameplay.physics;

import org.anima.engine.linearmath.Vector;

public class CylinderShape implements PrimitiveShape {
    private Vector size;

    public CylinderShape(Vector size) {
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
        return getCylinderShapePointer(size.getX() / 2.0f, size.getY() / 2.0f, size.getZ() / 2.0f);
    }

    private native long getCylinderShapePointer(float v, float v1, float v2);
}
