package org.anima.engine.graphics.objects;

import android.opengl.GLES30;

import org.anima.engine.gameplay.GameObject;
import org.anima.engine.linearmath.Quaternion;
import org.anima.engine.graphics.Scene;
import org.anima.engine.linearmath.Matrix;
import org.anima.engine.linearmath.Vector;

public abstract class RenderedObject extends GameObject {
    private Matrix modelMatrix;
    private boolean shadowCaster;

    protected RenderedObject() {
        super();

        updateModelMatrix();
    }

    protected RenderedObject(Vector position, Vector scale, Quaternion rotation) {
        super(position, scale, rotation);

        updateModelMatrix();
    }

    public boolean isShadowCaster() {
        return shadowCaster;
    }

    public void setShadowCaster(boolean shadowCaster) {
        this.shadowCaster = shadowCaster;
    }

    protected void updateModelMatrix() {
        modelMatrix = new Matrix();

        modelMatrix.scale(getScale());
        modelMatrix.rotate(getRotation());
        modelMatrix.translate(getPosition());
    }

    public void draw(Scene scene) {
        updateModelMatrix();

        if (scene.getMvpMatrixHandle() != -1) {
            modelMatrix.multiply(scene.getViewMatrix());

            if (scene.getMvMatrixHandle() != -1) {
                GLES30.glUniformMatrix4fv(scene.getMvMatrixHandle(), 1, false,
                        modelMatrix.getArray(), 0);
            }

            modelMatrix.multiply(scene.getProjectionMatrix());

            GLES30.glUniformMatrix4fv(scene.getMvpMatrixHandle(), 1, false,
                    modelMatrix.getArray(), 0);
        }

        drawObject(scene);
    }

    protected abstract void drawObject(Scene scene);
}
