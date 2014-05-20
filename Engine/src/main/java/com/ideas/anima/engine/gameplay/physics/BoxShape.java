package com.ideas.anima.engine.gameplay.physics;

import com.ideas.anima.engine.graphics.Vector;

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
        return getBoxShapePointer(size.x / 2.0f, size.y / 2.0f, size.z / 2.0f);
    }

    private native long getBoxShapePointer(float x, float y, float z);
}
