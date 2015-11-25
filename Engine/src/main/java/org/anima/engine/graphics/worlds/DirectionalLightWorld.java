package org.anima.engine.graphics.worlds;

import org.anima.engine.Game;
import org.anima.engine.graphics.World;
import org.anima.engine.graphics.objects.Camera;
import org.anima.engine.graphics.objects.lights.DirectionalLight;
import org.anima.engine.linearmath.Matrix;
import org.anima.engine.linearmath.Quaternion;
import org.anima.engine.linearmath.Vector;

public abstract class DirectionalLightWorld extends World {
    private DirectionalLight directionalLight;
    private float nearShadowMap = 1.0f;
    private float farShadowMap;
    private boolean lightPerspective;
    private Matrix shadowMapMatrix = new Matrix();
    private Matrix shadowMapViewMatrix = new Matrix();
    private Matrix shadowMapProjectionMatrix = new Matrix();
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
    public Matrix getViewMatrix() {
        return lightPerspective ? shadowMapViewMatrix : super.getViewMatrix();
    }

    @Override
    public Matrix getProjectionMatrix() {
        return lightPerspective ? shadowMapProjectionMatrix : super.getProjectionMatrix();
    }

    public Matrix getShadowMapMatrix() {
        return shadowMapMatrix;
    }

    private void computeShadowMapMatrices() {
        Vector[] corners = {
                new Vector(-1.0f, -1.0f, -1.0f),
                new Vector(-1.0f, -1.0f,  1.0f),
                new Vector(-1.0f,  1.0f, -1.0f),
                new Vector(-1.0f,  1.0f,  1.0f),
                new Vector( 1.0f, -1.0f, -1.0f),
                new Vector( 1.0f, -1.0f,  1.0f),
                new Vector( 1.0f,  1.0f, -1.0f),
                new Vector( 1.0f,  1.0f,  1.0f)
        };

        Vector up = new Vector(directionalLight.getDirection());
        Vector temp = up.cross(getCamera().getLook().getPosition().subtract(getCamera().getPosition()));
        Vector look = temp.cross(up).add(getCamera().getPosition());

        shadowMapProjectionMatrix = new Matrix(getShadowMapProjectionMatrix()).invert();
        shadowMapViewMatrix = new Matrix(super.getViewMatrix()).invert();

        shadowMapProjectionMatrix.multiply(shadowMapViewMatrix);

        shadowMapViewMatrix.view(getCamera().getPosition(), look, up);

        shadowMapProjectionMatrix.multiply(shadowMapViewMatrix);

        float dot = (float) Math.sqrt(Math.abs(getCamera().getLook().getPosition().subtract(
                getCamera().getPosition()).normalize().dot(getDirectionalLight().getDirection())));
        float near = MAX_NEAR * dot + nearShadowMap;

        for (int i = 0; i < corners.length; i++) {
            corners[i] = shadowMapProjectionMatrix.tranformPoint(corners[i]);
        }

        Vector[] boundaries = getBoundaries(corners);

        float dz = boundaries[1].getZ() - boundaries[0].getZ();

        Matrix translation = new Matrix().translate(new Vector(0.0f, 0.0f,
                -near - boundaries[1].getZ()));

        shadowMapViewMatrix.multiply(translation);
        shadowMapProjectionMatrix.frustum(-1.0f, 1.0f, -1.0f, 1.0f, near, near + dz);

        for (int i = 0; i < corners.length; i++) {
            corners[i] = translation.tranformPoint(corners[i]);
            corners[i] = shadowMapProjectionMatrix.tranformPoint(corners[i]);
        }

        boundaries = getBoundaries(corners);

        float dx = boundaries[1].getX() - boundaries[0].getX();
        float dy = boundaries[1].getY() - boundaries[0].getY();
        
        shadowMapProjectionMatrix.translate(new Vector(
                -boundaries[0].getX() - dx / 2.0f,
                -boundaries[0].getY() - dy / 2.0f,
                0.0f
        ));

        shadowMapProjectionMatrix.scale(new Vector(
                2.0f / dx,
                2.0f / dy * DEPTH_CLAMP,
                1.0f
        ));

        shadowMapProjectionMatrix.rotate(new Quaternion(new Vector(-1.0f, 0.0f, 0.0f), 90.0f));

        computeShadowMapMatrix();
    }

    private void computeShadowMapMatrix() {
        shadowMapMatrix = new Matrix().scale(new Vector(2.0f)).translate(new Vector(-1.0f));
        
        shadowMapMatrix.multiply(new Matrix(getViewMatrix()).multiply(getProjectionMatrix())
                .invert());
        shadowMapMatrix.multiply(new Matrix(shadowMapViewMatrix).multiply(
                shadowMapProjectionMatrix));
        
        shadowMapMatrix.translate(new Vector(1.0f)).scale(new Vector(0.5f));
    }

    private Vector[] getBoundaries(Vector[] corners) {
        Vector[] boundaries = new Vector[2];

        boundaries[0] = new Vector(corners[0]);
        boundaries[1] = new Vector(corners[0]);

        for (Vector corner : corners) {
            if (corner.getX() < boundaries[0].getX()) boundaries[0].setX(corner.getX());
            if (corner.getY() < boundaries[0].getY()) boundaries[0].setY(corner.getY());
            if (corner.getZ() < boundaries[0].getZ()) boundaries[0].setZ(corner.getZ());

            if (corner.getX() > boundaries[1].getX()) boundaries[1].setX(corner.getX());
            if (corner.getY() > boundaries[1].getY()) boundaries[1].setY(corner.getY());
            if (corner.getZ() > boundaries[1].getZ()) boundaries[1].setZ(corner.getZ());
        }

        return boundaries;
    }

    private Matrix getShadowMapProjectionMatrix() {
        float ratio = (float) getWidth() / getHeight();

        return new Matrix().frustum(-ratio, ratio, -1.0f, 1.0f, getCamera().getNear(),
                farShadowMap);
    }

    @Override
    protected void computeMatrices() {
        super.computeMatrices();

        if (directionalLight != null) computeShadowMapMatrices();
    }
}
