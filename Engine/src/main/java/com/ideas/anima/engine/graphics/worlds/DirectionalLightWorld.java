package com.ideas.anima.engine.graphics.worlds;

import android.opengl.Matrix;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.graphics.Vector;
import com.ideas.anima.engine.graphics.World;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.lights.DirectionalLight;

public abstract class DirectionalLightWorld extends World {
    private DirectionalLight directionalLight;
    private float nearShadowMap = 1.0f;
    private float farShadowMap;
    private boolean lightPerspective;
    private float[] shadowMapMatrix = new float[16];
    private float[] shadowMapViewMatrix = new float[16];
    private float[] shadowMapProjectionMatrix = new float[16];
    private static final float MAX_NEAR = 10000.0f;
    private static final float DEPTH_CLAMP = 0.5f;

    protected DirectionalLightWorld(Game game, Camera camera) {
        super(game, camera);

        farShadowMap = getCamera().getFar();
    }

    protected DirectionalLightWorld(Game game, Camera camera, DirectionalLight directionalLight) {
        super(game, camera);

        this.directionalLight = directionalLight;
        farShadowMap = getCamera().getFar();
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public void setNearShadowMap(float nearShadowMap) {
        this.nearShadowMap = nearShadowMap;
    }

    public void setFarShadowMap(float farShadowMap) {
        this.farShadowMap = farShadowMap;
    }

    protected void setLightPerspective(boolean lightPerspective) {
        this.lightPerspective = lightPerspective;
    }

    @Override
    public float[] getViewMatrix() {
        return lightPerspective ? shadowMapViewMatrix : super.getViewMatrix();
    }

    @Override
    public float[] getProjectionMatrix() {
        return lightPerspective ? shadowMapProjectionMatrix : super.getProjectionMatrix();
    }

    public float[] getShadowMapMatrix() {
        return shadowMapMatrix;
    }

    private void computeShadowMapMatrices() {
        float[] matrix = new float[16];

        Vector[] corners = {new Vector(-1.0f, -1.0f, -1.0f),
                            new Vector(-1.0f, -1.0f,  1.0f),
                            new Vector(-1.0f,  1.0f, -1.0f),
                            new Vector(-1.0f,  1.0f,  1.0f),
                            new Vector( 1.0f, -1.0f, -1.0f),
                            new Vector( 1.0f, -1.0f,  1.0f),
                            new Vector( 1.0f,  1.0f, -1.0f),
                            new Vector( 1.0f,  1.0f,  1.0f)};

        Vector up = (new Vector(directionalLight.getDirection()));
        Vector temp = Vector.cross(up, Vector.subtract(getCamera().getLook().getPosition(),
                getCamera().getPosition()));
        Vector look = Vector.cross(temp, up).add(getCamera().getPosition());

        Matrix.invertM(shadowMapProjectionMatrix, 0, getShadowMapProjectionMatrix(), 0);
        Matrix.invertM(shadowMapViewMatrix, 0, super.getViewMatrix(), 0);

        Matrix.multiplyMM(shadowMapProjectionMatrix, 0, shadowMapViewMatrix, 0,
                shadowMapProjectionMatrix, 0);

        Matrix.setLookAtM(
                shadowMapViewMatrix,
                0,
                getCamera().getPosition().x,
                getCamera().getPosition().y,
                getCamera().getPosition().z,
                look.x,
                look.y,
                look.z,
                up.x,
                up.y,
                up.z
        );

        Matrix.multiplyMM(shadowMapProjectionMatrix, 0, shadowMapViewMatrix, 0,
                shadowMapProjectionMatrix, 0);

        float dot = (float) Math.sqrt(Math.abs(Vector.subtract(getCamera().getLook().getPosition(),
                getCamera().getPosition()).normalize().dot(getDirectionalLight().getDirection())));
        float near = MAX_NEAR * dot + nearShadowMap;

        for (Vector corner : corners) corner.transformPointByMatrix(shadowMapProjectionMatrix);

        Vector[] boundaries = getBoundaries(corners);

        float dz = boundaries[1].z - boundaries[0].z;

        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(
                matrix,
                0,
                0.0f,
                0.0f,
                -near - boundaries[1].z
        );

        Matrix.multiplyMM(shadowMapViewMatrix, 0, matrix, 0, shadowMapViewMatrix, 0);
        Matrix.frustumM(shadowMapProjectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, near, near + dz);

        for (Vector corner : corners) {
            corner.transformPointByMatrix(matrix).transformPointByMatrix(shadowMapProjectionMatrix);
        }

        boundaries = getBoundaries(corners);

        float dx = boundaries[1].x - boundaries[0].x;
        float dy = boundaries[1].y - boundaries[0].y;

        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(
                matrix,
                0,
                -boundaries[0].x - dx / 2.0f,
                -boundaries[0].y - dy / 2.0f,
                0.0f
        );

        Matrix.multiplyMM(shadowMapProjectionMatrix, 0, matrix, 0,
                shadowMapProjectionMatrix, 0);

        Matrix.setIdentityM(matrix, 0);
        Matrix.scaleM(
                matrix,
                0,
                2.0f / dx,
                2.0f / dy * DEPTH_CLAMP,
                1.0f
        );

        Matrix.multiplyMM(shadowMapProjectionMatrix, 0, matrix, 0,
                shadowMapProjectionMatrix, 0);

        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(
                matrix,
                0,
                90.0f,
                -1.0f,
                0.0f,
                0.0f
        );

        Matrix.multiplyMM(shadowMapProjectionMatrix, 0, matrix, 0,
                shadowMapProjectionMatrix, 0);

        computeShadowMapMatrix();
    }

    private void computeShadowMapMatrix() {
        float[] matrix = new float[16];

        Matrix.setIdentityM(shadowMapMatrix, 0);

        Matrix.scaleM(shadowMapMatrix, 0, 2.0f, 2.0f, 2.0f);

        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix, 0, -1.0f, -1.0f, -1.0f);

        Matrix.multiplyMM(shadowMapMatrix, 0, matrix, 0, shadowMapMatrix, 0);

        Matrix.multiplyMM(matrix, 0, getProjectionMatrix(), 0, getViewMatrix(), 0);
        Matrix.invertM(matrix, 0, matrix, 0);

        Matrix.multiplyMM(shadowMapMatrix, 0, matrix, 0, shadowMapMatrix, 0);

        Matrix.multiplyMM(matrix, 0, shadowMapProjectionMatrix, 0, shadowMapViewMatrix, 0);
        Matrix.multiplyMM(shadowMapMatrix, 0, matrix, 0, shadowMapMatrix, 0);

        Matrix.setIdentityM(matrix, 0);
        Matrix.translateM(matrix, 0, 1.0f, 1.0f, 1.0f);

        Matrix.multiplyMM(shadowMapMatrix, 0, matrix, 0, shadowMapMatrix, 0);

        Matrix.setIdentityM(matrix, 0);
        Matrix.scaleM(matrix, 0, 0.5f, 0.5f, 0.5f);

        Matrix.multiplyMM(shadowMapMatrix, 0, matrix, 0, shadowMapMatrix, 0);
    }

    private Vector[] getBoundaries(Vector[] corners) {
        Vector[] boundaries = new Vector[2];

        boundaries[0] = new Vector(corners[0]);
        boundaries[1] = new Vector(corners[0]);

        for (Vector corner : corners) {
            if (corner.x < boundaries[0].x) boundaries[0].x = corner.x;
            if (corner.y < boundaries[0].y) boundaries[0].y = corner.y;
            if (corner.z < boundaries[0].z) boundaries[0].z = corner.z;

            if (corner.x > boundaries[1].x) boundaries[1].x = corner.x;
            if (corner.y > boundaries[1].y) boundaries[1].y = corner.y;
            if (corner.z > boundaries[1].z) boundaries[1].z = corner.z;
        }

        return boundaries;
    }

    private float[] getShadowMapProjectionMatrix() {
        float[] projectionMatrix = new float[16];

        float ratio = (float) getGame().glView.getWidth() / getGame().glView.getHeight();

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, getCamera().getNear(),
                farShadowMap);

        return projectionMatrix;
    }

    @Override
    protected void computeMatrices() {
        super.computeMatrices();

        if (directionalLight != null) computeShadowMapMatrices();
    }
}
