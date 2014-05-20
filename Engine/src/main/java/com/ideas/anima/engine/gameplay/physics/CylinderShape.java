package com.ideas.anima.engine.gameplay.physics;

import com.ideas.anima.engine.graphics.Vector;

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
        return getCylinderShapePointer(size.x / 2.0f, size.y / 2.0f, size.z / 2.0f);
    }

    private native long getCylinderShapePointer(float v, float v1, float v2);
}
