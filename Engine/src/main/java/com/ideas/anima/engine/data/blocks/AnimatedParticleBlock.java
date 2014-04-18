package com.ideas.anima.engine.data.blocks;

import com.ideas.anima.engine.data.Block;

public class AnimatedParticleBlock extends Block {
    private ParticleBlock[] children;

    public AnimatedParticleBlock(int[] indices) {
        super(indices);
    }

    public void setChildren(ParticleBlock[] children) {
        this.children = children;
    }
}
