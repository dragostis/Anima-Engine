package com.ideas.anima.engine.graphics;

import android.opengl.Matrix;

import com.ideas.anima.engine.gameplay.GameObject;

public abstract class RenderedObject extends GameObject {
    private float[] modelMatrix = new float[16];

    public RenderedObject() {
        super();

        updateModelMatrix();
    }

    public RenderedObject(Vector position, Vector rotation, Vector scale) {
        super(position, rotation, scale);

        updateModelMatrix();
    }

    protected void updateModelMatrix() {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, getPosition().x, getPosition().y, getPosition().z);

        float max = Math.max(getRotation().x, Math.max(getRotation().y, getRotation().z));
        if (max != 0.0f) Matrix.rotateM(modelMatrix, 0, max, getRotation().x / max,
                getRotation().y / max, getRotation().z / max);

        Matrix.scaleM(modelMatrix, 0, getScale().x, getScale().y, getScale().z);
    }

    public abstract void draw(float deltaTime, int programHandle);
}
