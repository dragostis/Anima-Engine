package com.ideas.anima.engine.data;

public class Block {
    private int[] indices;

    protected Block(int[] indices) {
        this.indices = indices;
    }

    protected int[] getIndices() {
        return indices;
    }
}
