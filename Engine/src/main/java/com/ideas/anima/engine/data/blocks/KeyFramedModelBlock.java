package com.ideas.anima.engine.data.blocks;

import com.ideas.anima.engine.data.Block;

public class KeyFramedModelBlock extends Block {
    private ModelBlock[] children;

    public KeyFramedModelBlock(int[] indices) {
        super(indices);
    }

    public ModelBlock[] getChildren() {
        return children;
    }

    public void setChildren(ModelBlock[] children) {
        this.children = children;
    }
}
