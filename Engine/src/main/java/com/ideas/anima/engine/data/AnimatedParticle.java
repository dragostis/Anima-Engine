package com.ideas.anima.engine.data;

public class AnimatedParticle extends Block {
    private Particle[] children;

    public AnimatedParticle(int[] indices) {
        super(indices);
    }

    public void setChildren(Particle[] children) {
        this.children = children;
    }
}
