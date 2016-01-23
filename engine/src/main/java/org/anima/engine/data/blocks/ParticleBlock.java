package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class ParticleBlock extends Block {
    private String path;

    public ParticleBlock(int id, String path) {
        super(id);

        this.path = path;
    }
}
