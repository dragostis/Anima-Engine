package com.ideas.anima.engine.graphics.scenes;

import android.opengl.GLES30;

import com.ideas.anima.engine.data.blocks.VerticesBlock;
import com.ideas.anima.engine.graphics.DisposableVertices;
import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.Vector;
import com.ideas.anima.engine.graphics.World;

public abstract class PostProcessingScene extends Scene {
    protected DisposableVertices quad;
    private Texture texture;
    private Vector position = new Vector(-1.0f, -1.0f);
    private float width = 2.0f;
    private float height = 2.0f;

    public PostProcessingScene(Program program, World world, Texture texture) {
        super(program, world);

        this.texture = texture;

        quad = getQuad();
    }

    protected PostProcessingScene(Program program, World world, Texture texture, Vector position,
                                  float width, float height) {
        super(program, world);

        this.texture = texture;
        this.position = position;
        this.width = width;
        this.height = height;

        quad = getQuad();
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;

        quad = getQuad();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;

        quad = getQuad();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;

        quad = getQuad();
    }

    private DisposableVertices getQuad() {
        float[] quadArray = {position.x        , position.y + height,  0.0f,  0.0f,  1.0f,
                             position.x        , position.y         ,  0.0f,  0.0f,  0.0f,
                             position.x + width, position.y         ,  0.0f,  1.0f,  0.0f,
                             position.x        , position.y + height,  0.0f,  0.0f,  1.0f,
                             position.x + width, position.y         ,  0.0f,  1.0f,  0.0f,
                             position.x + width, position.y + height,  0.0f,  1.0f,  1.0f};

        VerticesBlock verticesBlock = new VerticesBlock(quadArray);

        return new DisposableVertices(verticesBlock);
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
