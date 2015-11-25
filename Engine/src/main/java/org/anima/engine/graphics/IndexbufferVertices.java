package org.anima.engine.graphics;

import android.opengl.GLES30;

import org.anima.engine.data.blocks.VerticesBlock;

public class IndexbufferVertices implements Vertices {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_DATA_SIZE = 3;
    private static final int TEXT_COORD_DATA_SIZE = 2;
    private static final int STRIDE = (POSITION_DATA_SIZE + TEXT_COORD_DATA_SIZE)
            * BYTES_PER_FLOAT;
    private VerticesBlock verticesBlock;
    private int bufferHandle;
    private int numOfVertices;

    public IndexbufferVertices(VerticesBlock verticesBlock) {
        this.verticesBlock = verticesBlock;

        allocateArray(null);
    }

    @Override
    public void allocateArray(float[] array) {
        final int[] buffers = new int[1];
        GLES30.glGenBuffers(1, buffers, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, buffers[0]);
        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER,
                verticesBlock.getFloatBuffer().capacity() * BYTES_PER_FLOAT,
                verticesBlock.getFloatBuffer(),
                GLES30.GL_STATIC_DRAW
        );
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        bufferHandle = buffers[0];

        numOfVertices = verticesBlock.getFloatBuffer().capacity() / 5;

        verticesBlock.getFloatBuffer().limit(0);
        verticesBlock.setFloatBuffer(null);
    }

    @Override
    public void draw(Scene scene) {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
        GLES30.glEnableVertexAttribArray(scene.getPositionHandle());
        GLES30.glVertexAttribPointer(
                scene.getPositionHandle(),
                POSITION_DATA_SIZE,
                GLES30.GL_FLOAT,
                false,
                STRIDE,
                0
        );
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
        GLES30.glEnableVertexAttribArray(scene.getTextCoordHandle());
        GLES30.glVertexAttribPointer(
                scene.getTextCoordHandle(),
                TEXT_COORD_DATA_SIZE,
                GLES30.GL_FLOAT,
                false,
                STRIDE,
                POSITION_DATA_SIZE * BYTES_PER_FLOAT
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, numOfVertices);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }
}
