package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

// TODO: reimplement this
public class KeyFramedModelBlock extends Block {
    private ModelBlock[] children;

    public KeyFramedModelBlock() {
    }

    public ModelBlock[] getChildren() {
        return children;
    }

    public void setChildren(ModelBlock[] children) {
        this.children = children;
    }
}
