package org.anima.engine.graphics;

import android.opengl.GLES30;

import org.anima.engine.data.blocks.VerticesBlock;

public class DisposableVertices implements Vertices {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_DATA_SIZE = 3;
    private static final int TEXT_COORD_DATA_SIZE = 2;
    private static final int STRIDE = (POSITION_DATA_SIZE + TEXT_COORD_DATA_SIZE)
            * BYTES_PER_FLOAT;
    private VerticesBlock verticesBlock;

    public DisposableVertices(VerticesBlock verticesBlock) {
        this.verticesBlock = verticesBlock;
    }

    @Override
    public void allocateArray(float[] array) {

    }

    @Override
    public void draw(Scene scene) {
        verticesBlock.getFloatBuffer().position(0);
        GLES30.glEnableVertexAttribArray(scene.getPositionHandle());
        GLES30.glVertexAttribPointer(
                scene.getPositionHandle(),
                POSITION_DATA_SIZE,
                GLES30.GL_FLOAT,
                false, STRIDE,
                verticesBlock.getFloatBuffer()
        );

        verticesBlock.getFloatBuffer().position(POSITION_DATA_SIZE);
        GLES30.glEnableVertexAttribArray(scene.getTextCoordHandle());
        GLES30.glVertexAttribPointer(
                scene.getTextCoordHandle(),
                TEXT_COORD_DATA_SIZE,
                GLES30.GL_FLOAT,
                false,
                STRIDE,
                verticesBlock.getFloatBuffer()
        );

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, verticesBlock.getFloatBuffer().capacity() / 5);
    }
}
