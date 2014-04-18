package com.ideas.anima.engine.graphics.objects;

import com.ideas.anima.engine.data.blocks.ModelBlock;
import com.ideas.anima.engine.graphics.IndexbufferVertices;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.Vector;
import com.ideas.anima.engine.graphics.Vertices;

public class Model extends RenderedObject {
    private Vertices vertices;
    private Texture texture;
    private Texture normalMap;

    public Model(ModelBlock modelBlock) {
        vertices = new IndexbufferVertices(modelBlock.getVerticesBlock());
        texture = new Texture(modelBlock.getTextureBlock());
        normalMap = new Texture(modelBlock.getNormalMapBlock());
    }

    public Model(Vector position, Vector rotation, Vector scale, ModelBlock modelBlock) {
        super(position, rotation, scale);

        vertices = new IndexbufferVertices(modelBlock.getVerticesBlock());
        texture = new Texture(modelBlock.getTextureBlock());
        normalMap = new Texture(modelBlock.getNormalMapBlock());
    }

    @Override
    protected void drawObject(Scene scene) {
        texture.bind(0, scene.getTextureLocationHandle());
//        normalMap.bind(1, scene.getNormalMapLocationHandle());

        vertices.draw(scene);
    }
}
