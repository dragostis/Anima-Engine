package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class CompoundModelBlock extends Block {
    private int[] childrenIds;

    private ModelBlock[] children;

    public CompoundModelBlock(int[] childrenIds) {
        this.childrenIds = childrenIds;
    }

    public int[] getChildrenIds() { return childrenIds; }

    public ModelBlock[] getChildren() {
        return children;
    }

    public void setChildren(ModelBlock[] children) {
        this.children = children;
    }
}
