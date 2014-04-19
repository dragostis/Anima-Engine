package com.ideas.anima.engine.gameplay;

import android.opengl.GLES30;

import com.ideas.anima.engine.data.blocks.VerticesBlock;
import com.ideas.anima.engine.graphics.IndexbufferVertices;
import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.Vertices;
import com.ideas.anima.engine.graphics.World;

public abstract class PostProcessingScene extends Scene {
    private Vertices quad;
    private Texture texture;

    public PostProcessingScene(Program program, World world, Texture texture) {
        super(program, world);

        this.texture = texture;

        float[] quadArray = {-1.0f,  1.0f,  0.0f,  0.0f,  1.0f,
                             -1.0f, -1.0f,  0.0f,  0.0f,  0.0f,
                              1.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                             -1.0f,  1.0f,  0.0f,  0.0f,  1.0f,
                              1.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                              1.0f,  1.0f,  0.0f,  1.0f,  1.0f};

        VerticesBlock verticesBlock = new VerticesBlock(quadArray);
        quad = new IndexbufferVertices(verticesBlock);
    }

    @Override
    protected void getHandles() {
        positionHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_Position");
        textCoordHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_TextCoord");
        textureLocationHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                "u_Texture");

        getUniformHandles();
    }

    @Override
    public void draw() {
        texture.bind(0, textureLocationHandle);

        quad.draw(this);
    }
}
