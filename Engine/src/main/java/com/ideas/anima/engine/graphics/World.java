package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;

import java.util.ArrayList;
import java.util.List;

public abstract class World {
    private Game game;
    private Camera camera;
    private List<RenderedObject> renderedObjects;
    private float[] viewMatrix;
    private float[] projectionMatrix = new float[16];
    private float near = 1.0f;
    private float far = 20.0f;

    public World(Game game, Camera camera) {
        this.game = game;
        this.camera = camera;

        renderedObjects = new ArrayList<>();

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glEnable(GLES30.GL_CULL_FACE);

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    public Game getGame() {
        return game;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<RenderedObject> getRenderedObjects() {
        return renderedObjects;
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public int getWidth() {
        return getGame().glView.getWidth();
    }

    public int getHeight() {
        return getGame().glView.getHeight();
    }

    protected void computeMatrices() {
        viewMatrix = camera.getViewMatrix();

        float ratio = (float) game.glView.getWidth() / game.glView.getHeight();

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, near, far);
    }

    public void draw() {
        computeMatrices();

        drawWorld();
    }

    protected abstract void drawWorld();
}
