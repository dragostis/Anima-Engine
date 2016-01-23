package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class AnimatedParticleBlock extends Block {
    private ParticleBlock[] children;

    public AnimatedParticleBlock(int id) {
        super(id);
    }

    public void setChildren(ParticleBlock[] children) {
        this.children = children;
    }
}
