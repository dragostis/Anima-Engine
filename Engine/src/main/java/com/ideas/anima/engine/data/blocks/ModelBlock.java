package com.ideas.anima.engine.data.blocks;

import com.ideas.anima.engine.data.Block;

public class ModelBlock extends Block {
    private VerticesBlock verticesBlock;
    private TextureBlock textureBlock;
    private NormalMapBlock normalMapBlock;

    public ModelBlock(int[] indices) {
        super(indices);
    }

    public VerticesBlock getVerticesBlock() {
        return verticesBlock;
    }

    public void setVerticesBlock(VerticesBlock verticesBlock) {
        this.verticesBlock = verticesBlock;
    }

    public TextureBlock getTextureBlock() {
        return textureBlock;
    }

    public void setTextureBlock(TextureBlock textureBlock) {
        this.textureBlock = textureBlock;
    }

    public NormalMapBlock getNormalMapBlock() {
        return normalMapBlock;
    }

    public void setNormalMapBlock(NormalMapBlock normalMapBlock) {
        this.normalMapBlock = normalMapBlock;
    }
}
