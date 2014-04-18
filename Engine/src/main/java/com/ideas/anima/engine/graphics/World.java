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
    private float[] projectionMatrix = new float[16];
    private float near = 1.0f;
    private float far = 20.0f;

    public World(Game game, Camera camera) {
        this.game = game;
        this.camera = camera;

        renderedObjects = new ArrayList<>();

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
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
        return camera.getViewMatrix();
    }

    public float[] getProjectionMatrix() {
        float ratio = (float) game.glView.getWidth() / game.glView.getHeight();

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, near, far);

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

    public abstract void draw();
}