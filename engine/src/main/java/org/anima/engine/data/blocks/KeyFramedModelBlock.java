package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class KeyFramedModelBlock extends Block {
    private ModelBlock[] children;

    public KeyFramedModelBlock(int id) {
        super(id);
    }

    public ModelBlock[] getChildren() {
        return children;
    }

    public void setChildren(ModelBlock[] children) {
        this.children = children;
    }
}
