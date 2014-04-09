package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

import com.ideas.anima.engine.data.Vertices;

public class DisposableArray implements ModelArray {
    private static final int bytesPerFloat = 4;
    private static final int positionDataSize = 3;
    private static final int textCoordDataSize = 2;
    private static final int stride = (positionDataSize + textCoordDataSize)
            * bytesPerFloat;
    private Vertices vertices;

    public DisposableArray(Vertices vertices) {
        this.vertices = vertices;
    }

    @Override
    public void allocateArray(float[] array) {

    }

    @Override
    public void draw(Scene scene) {
        vertices.getFloatBuffer().position(0);
        GLES30.glEnableVertexAttribArray(scene.getPositionHandle());
        GLES30.glVertexAttribPointer(
                scene.getPositionHandle(),
                positionDataSize,
                GLES30.GL_FLOAT,
                false, stride,
                vertices.getFloatBuffer()
        );

        vertices.getFloatBuffer().position(positionDataSize);
        GLES30.glEnableVertexAttribArray(scene.getTextCoordHandle());
        GLES30.glVertexAttribPointer(
                scene.getTextCoordHandle(),
                textCoordDataSize,
                GLES30.GL_FLOAT,
                false,
                stride,
                vertices.getFloatBuffer()
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertices.getFloatBuffer().capacity() / 5);
    }
}
