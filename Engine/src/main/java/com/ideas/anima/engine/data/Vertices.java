package com.ideas.anima.engine.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Vertices extends Block {
    private static final int bytesPerFloat = 4;
    private FloatBuffer floatBuffer;

    public Vertices(float[] content) {
        super(null);

        putInBuffer(content);
    }

    public float[] getContent() {
        return floatBuffer.array();
    }

    public void setContent(float[] content) {
        putInBuffer(content);
    }

    public FloatBuffer getFloatBuffer() {
        return floatBuffer;
    }

    public void setFloatBuffer(FloatBuffer floatBuffer) {
        this.floatBuffer = floatBuffer;
    }

    private void putInBuffer(float[] content) {
        floatBuffer = ByteBuffer.allocateDirect(content.length * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(content).position(0);
    }
}
