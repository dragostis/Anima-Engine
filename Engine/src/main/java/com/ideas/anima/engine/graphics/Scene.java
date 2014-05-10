package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.World;

public abstract class Scene {
    protected Program program;
    private World world;
    protected int positionHandle;
    protected int textCoordHandle;
    protected int mvMatrixHandle;
    protected int mvpMatrixHandle;
    protected int projectionVectorHandle;
    protected int clipVectorHandle;
    protected int screenRatioHandle;
    protected int ambientHandle;
    protected int diffuseHandle;
    protected int specularHandle;
    protected int textureLocationHandle;
    protected int normalMapLocationHandle;

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

    public int getMvMatrixHandle() {
        return mvMatrixHandle;
    }

    public int getMvpMatrixHandle() {
        return mvpMatrixHandle;
    }

    public int getProjectionVectorHandle() {
        return projectionVectorHandle;
    }

    public int getClipVectorHandle() {
        return clipVectorHandle;
    }

    public int getScreenRatioHandle() {
        return screenRatioHandle;
    }

    public int getAmbientHandle() {
        return ambientHandle;
    }

    public int getDiffuseHandle() {
        return diffuseHandle;
    }

    public int getSpecularHandle() {
        return specularHandle;
    }

    public int getTextureLocationHandle() {
        return textureLocationHandle;
    }

    public int getNormalMapLocationHandle() {
        return normalMapLocationHandle;
    }

    public void use() {
        program.use();
    }

    protected void getHandles() {
        positionHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_Position");
        textCoordHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_TextCoord");
        mvMatrixHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_MVMatrix");
        mvpMatrixHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_MVPMatrix");
        projectionVectorHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                "u_ProjectionVector");
        clipVectorHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_ClipVector");
        screenRatioHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                "u_ScreenRatio");
        ambientHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_AmbientColor");
        diffuseHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_DiffuseColor");
        specularHandle = GLES30.glGetUniformLocation(program.getProgramHandle(), "u_SpecularColor");
        textureLocationHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                "u_Texture");
        normalMapLocationHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                "u_NormalMap");

        getUniformHandles();
    }

    public abstract void getUniformHandles();

    public void drawScene() {
        if (screenRatioHandle != -1) {
            GLES30.glUniform2f(
                    screenRatioHandle,
                    1.0f / getWorld().getWidth(),
                    1.0f / getWorld().getHeight()
            );
        }

        if (projectionVectorHandle != -1) {
            float[] projectionMatrix = getWorld().getProjectionMatrix();

            GLES30.glUniform4f(
                    projectionVectorHandle,
                    -2.0f / projectionMatrix[0],
                    -2.0f / projectionMatrix[5],
                    (1.0f - projectionMatrix[8]) / projectionMatrix[0],
                    (1.0f + projectionMatrix[9]) / projectionMatrix[5]
            );

            GLES30.glUniform3f(
                    clipVectorHandle,
                    -getWorld().getNear() * getWorld().getFar(),
                    getWorld().getNear() - getWorld().getFar(),
                    getWorld().getFar()
            );
        }

        draw();
    }

    protected abstract void draw();
}
