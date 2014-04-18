package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

public abstract class Scene {
    private Program program;
    private World world;
    private int positionHandle;
    private int textCoordHandle;
    private int mvpMatrixHandle;
    private int textureLocationHandle;

    public Scene(Program program, World world) {
        this.program = program;
        this.world = world;

        getHandles();
    }

    public World getWorld() {
        return world;
    }

    public int getPositionHandle() {
        return positionHandle;
    }

    public int getTextCoordHandle() {
        return textCoordHandle;
    }

    public int getMvpMatrixHandle() {
        return mvpMatrixHandle;
    }

    public int getTextureLocationHandle() {
        return textureLocationHandle;
    }

    public void use() {
        program.use();
    }

    private void getHandles() {
        positionHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_Position");
        textCoordHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_TextCoord");
        mvpMatrixHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_MVPMatrix");
        textureLocationHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_Texture");

        getUniformHandles();
    }

    public abstract void getUniformHandles();

    public abstract void draw();
}
