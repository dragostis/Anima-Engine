package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

// TODO: reimplement this
public class AnimatedParticleBlock extends Block {
    private ParticleBlock[] children;

    public AnimatedParticleBlock() {
    }

    public void setChildren(ParticleBlock[] children) {
        this.children = children;
    }
}
