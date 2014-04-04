package com.ideas.anima.engine.data;

public class CompoundModel extends Block {
    private Model[] children;

    public CompoundModel(int[] indices) {
        super(indices);
    }

    public void setChildren(Model[] children) {
        this.children = children;
    }
}
