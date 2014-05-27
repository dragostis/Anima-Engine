package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;
import com.ideas.anima.engine.linearmath.Matrix;

import java.util.ArrayList;
import java.util.List;

public abstract class World {
    private Game game;
    private Camera camera;
    private List<RenderedObject> renderedObjects;
    private Matrix viewMatrix;
    private Matrix projectionMatrix;

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

    public Matrix getViewMatrix() {
        return viewMatrix;
    }

    public Matrix getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getWidth() {
        return getGame().glView.getWidth();
    }

    public int getHeight() {
        return getGame().glView.getHeight();
    }

    public float getNear() {
        return camera.getNear();
    }

    public float getFar() {
        return camera.getFar();
    }

    protected void computeMatrices() {
        viewMatrix = camera.getViewMatrix();
        projectionMatrix = camera.getProjectionMatrix((float) game.glView.getWidth()
                / game.glView.getHeight());
    }

    public void draw() {
        computeMatrices();

        drawWorld();
    }

    protected abstract void drawWorld();
}
