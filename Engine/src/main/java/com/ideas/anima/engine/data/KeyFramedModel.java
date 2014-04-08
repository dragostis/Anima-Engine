package com.ideas.anima.engine.data;

public class KeyFramedModel extends Block {
    private Model[] children;

    public KeyFramedModel(int[] indices) {
        super(indices);
    }

    public Model[] getChildren() {
        return children;
    }

    public void setChildren(Model[] children) {
        this.children = children;
    }
}
