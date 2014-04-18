package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.data.blocks.ModelBlock;
import com.ideas.anima.engine.graphics.Vector;

import java.util.ArrayList;
import java.util.List;

public class NavMesh {
    private final float stepRadius;
    private float[] meshArray;
    private float unitSize;
    private List<AstarSample> samples;

    public NavMesh(ModelBlock mesh, float unitSize) {
        this.meshArray = mesh.getVerticesBlock().getContent();
        this.unitSize = unitSize;

        stepRadius = unitSize * (float) Math.sqrt(2.1f);

        generateSamples();
    }

    private void generateSamples() {
        List<AstarSample> temporarySampleList = new ArrayList<>();

        samples = new ArrayList<>();

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

        for (int i = 0; i < meshArray.length; i += 15) {
            for (AstarSample temporarySample : temporarySampleList) {
                Vector triangle = getTriangleCoordinates(temporarySample.position, i);

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

        for (int i = 0; i < meshArray.length; i += 15) {
            Vector triangle = getTriangleCoordinates(position, i);

            height = meshArray[i + 1] * triangle.x + meshArray[i + 5] * triangle.y
                    + meshArray[i + 11] * triangle.z;

            if (!(triangle.x < 0.0f || triangle.y < 0.0f || triangle.z < 0.0f)) break;
        }

        return height;
    }

    private Vector getMin() {
        Vector min = new Vector(meshArray[0], meshArray[2]);

        for (int i = 5; i < meshArray.length; i += 5) {
            if (min.x > meshArray[i]) min.x = meshArray[i];

            if (min.y > meshArray[i + 2]) min.y = meshArray[i + 2];
        }

        return min;
    }

    private Vector getMax() {
        Vector max = new Vector(meshArray[0], meshArray[2]);

        for (int i = 5; i < meshArray.length; i += 5) {
            if (max.x < meshArray[i]) max.x = meshArray[i];

            if (max.y < meshArray[i + 2]) max.y = meshArray[i + 2];
        }

        return max;
    }

    private Vector getTriangleCoordinates(Vector position, int i) {
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
        x1 = meshArray[i];
        x2 = meshArray[i + 5];
        x3 = meshArray[i + 10];

        y = position.y;
        y1 = meshArray[i + 2];
        y2 = meshArray[i + 7];
        y3 = meshArray[i + 12];

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
