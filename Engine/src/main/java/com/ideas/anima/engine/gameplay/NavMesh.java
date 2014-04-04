package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.data.Model;
import com.ideas.anima.engine.graphics.Vector;

import java.util.ArrayList;
import java.util.List;

public class NavMesh {
    private final float stepRadius;
    private Model mesh;
    private float unitSize;
    private List<AstarSample> samples;

    public NavMesh(Model mesh, float unitSize) {
        this.mesh = mesh;
        this.unitSize = unitSize;

        stepRadius = unitSize * (float) Math.sqrt(2.1f);

        generateSamples();
    }

    private void generateSamples() {
        List<AstarSample> temporarySampleList = new ArrayList<AstarSample>();

        samples = new ArrayList<AstarSample>();

        Vector min = getMin();
        Vector max = getMax();

        int width = (int) ((max.x - min.x) / unitSize);
        int height = (int) ((max.y - min.y) / unitSize);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                temporarySampleList.add(new AstarSample(new Vector(min.x + i * unitSize,
                        min.y + j * unitSize)));
            }
        }

        for (int i = 0; i < mesh.getArray().length; i += 24) {
            for (AstarSample temporarySample : temporarySampleList) {
                Vector triangle = getTriangleCoordinates(temporarySample.position, mesh, i);

                if (triangle.x >= 0.0f && triangle.y >= 0.0f && triangle.z >= 0.0f)
                    if (!samples.contains(temporarySample))
                        samples.add(temporarySample);
            }
        }

        for (int i = 0; i < samples.size(); i++) {
            for (int j = i + 1; j < samples.size(); j++) {
                if (samples.get(i).neighbours.size() == 8) break;

                if (samples.get(i).position.distance(samples.get(j).position) < stepRadius) {
                    samples.get(i).neighbours.add(samples.get(j));
                    samples.get(j).neighbours.add(samples.get(i));
                }
            }
        }
    }

    public float getHeight(Vector position) {
        float height = 0.0f;

        for (int i = 0; i < mesh.getArray().length; i += 24) {
            Vector triangle = getTriangleCoordinates(position, mesh, i);

            height = mesh.getArray()[i + 1] * triangle.x + mesh.getArray()[i + 9] * triangle.y
                    + mesh.getArray()[i + 17] * triangle.z;

            if (!(triangle.x < 0.0f || triangle.y < 0.0f || triangle.z < 0.0f)) break;
        }

        return height;
    }

    private Vector getMin() {
        Vector min = new Vector(mesh.getArray()[0], mesh.getArray()[1]);

        for (int i = 3; i < mesh.getArray().length; i++) {
            if (min.x > mesh.getArray()[i] && i % 3 == 0) min.x = mesh.getArray()[i];

            if (min.y > mesh.getArray()[i] && i % 3 == 2) min.y = mesh.getArray()[i];
        }

        return min;
    }

    private Vector getMax() {
        Vector max = new Vector(mesh.getArray()[0], mesh.getArray()[1]);

        for (int i = 3; i < mesh.getArray().length; i++) {
            if (max.x < mesh.getArray()[i] && i % 3 == 0) max.x = mesh.getArray()[i];

            if (max.y < mesh.getArray()[i] && i % 3 == 2) max.y = mesh.getArray()[i];
        }

        return max;
    }

    private Vector getTriangleCoordinates(Vector position, Model mesh, int i) {
        Vector triangle = new Vector();

        float x;
        float x1;
        float x2;
        float x3;

        float y;
        float y1;
        float y2;
        float y3;

        x = position.x;
        x1 = mesh.getArray()[i];
        x2 = mesh.getArray()[i + 8];
        x3 = mesh.getArray()[i + 16];

        y = position.y;
        y1 = mesh.getArray()[i + 2];
        y2 = mesh.getArray()[i + 10];
        y3 = mesh.getArray()[i + 18];

        if (x < x1 && x < x2 && x < x3 || x > x1 && x > x2 && x > x3) return new Vector(-1.0f);

        if (y < y1 && y < y2 && y < y3 || y > y1 && y > y2 && y > y3) return new Vector(-1.0f);

        triangle.x = (y2 - y3) * (x - x3) + (x3 - x2) * (y - y3);
        triangle.x /= (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        triangle.y = (y3 - y1) * (x - x3) + (x1 - x3) * (y - y3);
        triangle.y /= (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        triangle.z = 1.0f - triangle.x - triangle.y;

        return triangle;
    }

    public List<AstarSample> getSamples() {
        return samples;
    }

    public float getStepRadius() {
        return stepRadius;
    }
}
