package com.ideas.anima.engine.graphics.objects;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.ideas.anima.engine.gameplay.GameObject;
import com.ideas.anima.engine.graphics.Quaternion;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Vector;

public abstract class RenderedObject extends GameObject {
    private float[] modelMatrix = new float[16];
    private boolean shadowCaster;

    protected RenderedObject() {
        super();

        updateModelMatrix();
    }

    protected RenderedObject(Vector position, Vector scale, Quaternion rotation) {
        super(position, scale, rotation);

        updateModelMatrix();
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    public boolean isShadowCaster() {
        return shadowCaster;
    }

    public void setShadowCaster(boolean shadowCaster) {
        this.shadowCaster = shadowCaster;
    }

    protected void updateModelMatrix() {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.translateM(modelMatrix, 0, getPosition().x, getPosition().y, getPosition().z);

        float max = Math.max(getRotation().x, Math.max(getRotation().y, getRotation().z));
        if (max != 0.0f) Matrix.rotateM(modelMatrix, 0, max, getRotation().x / max,
                getRotation().y / max, getRotation().z / max);

        Matrix.scaleM(modelMatrix, 0, getScale().x, getScale().y, getScale().z);
    }

    public void draw(Scene scene) {
        updateModelMatrix();

        if (scene.getMMatrixHandle() != -1) {
            GLES30.glUniformMatrix4fv(scene.getMMatrixHandle(), 1, false, modelMatrix, 0);
        }

        if (scene.getMvpMatrixHandle() != -1) {
            Matrix.multiplyMM(modelMatrix, 0, scene.getWorld().getViewMatrix(), 0, modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, scene.getWorld().getProjectionMatrix(), 0,
                    modelMatrix, 0);

            GLES30.glUniformMatrix4fv(scene.getMvpMatrixHandle(), 1, false, modelMatrix, 0);
        }

        drawObject(scene);
    }

    protected abstract void drawObject(Scene scene);
}
