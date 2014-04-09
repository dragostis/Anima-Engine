package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

import com.ideas.anima.engine.data.Vertices;

public class IndexBufferArray implements ModelArray {
    private static final int bytesPerFloat = 4;
    private static final int positionDataSize = 3;
    private static final int textCoordDataSize = 2;
    private static final int stride = (positionDataSize + textCoordDataSize)
            * bytesPerFloat;
    private Vertices vertices;
    private int bufferHandle;

    public IndexBufferArray(Vertices vertices) {
        this.vertices = vertices;
    }

    @Override
    public void allocateArray(float[] array) {
        final int[] buffers = new int[1];
        GLES30.glGenBuffers(1, buffers, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, buffers[0]);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER,
                vertices.getFloatBuffer().capacity() * bytesPerFloat,
                vertices.getFloatBuffer(),
                GLES30.GL_STATIC_DRAW
        );
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        bufferHandle = buffers[0];

        vertices.getFloatBuffer().limit(0);
        vertices.setFloatBuffer(null);
    }

    @Override
    public void draw(Scene scene) {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
        GLES30.glEnableVertexAttribArray(scene.getPositionHandle());
        GLES30.glVertexAttribPointer(
                scene.getPositionHandle(),
                positionDataSize,
                GLES30.GL_FLOAT,
                false,
                stride,
                0
        );
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
        GLES30.glEnableVertexAttribArray(scene.getTextCoordHandle());
        GLES30.glVertexAttribPointer(
                scene.getTextCoordHandle(),
                textCoordDataSize,
                GLES30.GL_FLOAT,
                false,
                stride,
                positionDataSize * bytesPerFloat
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertices.getFloatBuffer().capacity() / 5);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }
}
