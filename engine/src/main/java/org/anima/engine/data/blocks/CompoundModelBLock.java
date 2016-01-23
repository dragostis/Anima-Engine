package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class CompoundModelBLock extends Block {
    private ModelBlock[] children;

    public CompoundModelBLock(int[] indices) {
        super(indices);
    }

    public ModelBlock[] getChildren() {
        return children;
    }

    public void setChildren(ModelBlock[] children) {
        this.children = children;
    }
}
