package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

public class ModelBlock extends Block {
    private int vertexId;
    private int materialId;

    private VerticesBlock verticesBlock;
    private MaterialBlock materialBlock;


    public ModelBlock(int vertexId,
                      int materialId) {
        this.vertexId = vertexId;
        this.materialId = materialId;
    }

    public int getVertexId() {
        return this.vertexId;
    }

    public int getMaterialId() {
        return this.materialId;
    }

    public VerticesBlock getVerticesBlock() {
        return verticesBlock;
    }

    public void setVerticesBlock(VerticesBlock verticesBlock) {
        this.verticesBlock = verticesBlock;
    }

    public MaterialBlock getMaterialBlock() {
        return materialBlock;
    }

    public void setMaterialBlock(MaterialBlock materialBlock) {
        this.materialBlock = materialBlock;
    }
}
