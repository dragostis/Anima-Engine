package org.anima.engine.gameplay.physics;

import org.anima.engine.linearmath.Vector;

import java.util.ArrayList;
import java.util.List;

public class CompoundShape implements Shape {
    private List<PrimitiveShape> shapes = new ArrayList<>();
    private List<Vector> positions = new ArrayList<>();

    public void addShape(PrimitiveShape shape, Vector position) {
        shapes.add(shape);
        positions.add(position);
    }

    @Override
    public long getPointer() {
        long[] shapes = new long[this.shapes.size()];
        float[] positions = new float[this.positions.size() * 3];

        for (int i = 0; i < shapes.length; i++) shapes[i] = this.shapes.get(i).getPointer();

        for (int i = 0; i < this.positions.size(); i++) {
            positions[i * 3] = this.positions.get(i).getX();
            positions[i * 3 + 1] = this.positions.get(i).getY();
            positions[i * 3 + 2] = this.positions.get(i).getZ();
        }

        return getCompoundShapePointer(shapes, positions);
    }

    private native long getCompoundShapePointer(long[] shapes, float[] positions);
}
