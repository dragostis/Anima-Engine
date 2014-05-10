package com.ideas.anima.engine.graphics.worlds;

import android.opengl.GLES30;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.data.Data;
import com.ideas.anima.engine.data.blocks.VerticesBlock;
import com.ideas.anima.engine.graphics.scenes.FXAAScene;
import com.ideas.anima.engine.graphics.IndexbufferVertices;
import com.ideas.anima.engine.graphics.scenes.PostProcessingScene;
import com.ideas.anima.engine.graphics.FramebufferObject;
import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.Vector;
import com.ideas.anima.engine.graphics.Vertices;
import com.ideas.anima.engine.graphics.World;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;
import com.ideas.anima.engine.graphics.objects.lights.PointLight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LightPrePassWorld extends DirectionalLightWorld {
    private List<PointLight> pointLights = new ArrayList<>();
    private FramebufferObject depthNormalBuffer;
    private FramebufferObject lightingBuffer;
    private FramebufferObject shadowMapBuffer;
    private FramebufferObject finalBuffer;
    private Scene depthNormalScene;
    private Scene pointLightScene;
    private Scene directionalLightScene;
    private Scene casterDepthScene;
    private Scene ambientLightScene;
    private Scene fxaaScene;
    private Vertices sphere;

    public LightPrePassWorld(Game game, Camera camera) {
        super(game, camera);

        depthNormalBuffer = new FramebufferObject(getWidth(), getHeight(), 1);
        lightingBuffer = new FramebufferObject(getWidth(), getHeight(), 2);
        shadowMapBuffer = new FramebufferObject(1024, 1024, 0);
        finalBuffer = new FramebufferObject(getWidth(), getHeight(), 1);

        try {
            sphere = new IndexbufferVertices((VerticesBlock)
                    (new Data(game.io.readAsset("vertices/sphere.anv"), game.io)).getRoot());

            depthNormalScene = new Scene(new Program(
                    game.io.readAsset("shaders/depth_normal.vert"),
                    game.io.readAsset("shaders/depth_normal.frag")
            ), this) {
                @Override
                public void getUniformHandles() {

                }

                @Override
                public void draw() {
                    for (RenderedObject object : getRenderedObjects()) {
                        object.draw(this);
                    }
                }
            };

            pointLightScene = new PointLightScene(new Program(
                    game.io.readAsset("shaders/light_pre_pass/point_light.vert"),
                    game.io.readAsset("shaders/light_pre_pass/point_light.frag")
            ), this);

            directionalLightScene = new DirectionalLightScene(new Program(
                    game.io.readAsset("shaders/light_pre_pass/directional_light.vert"),
                    game.io.readAsset("shaders/light_pre_pass/directional_light.frag")
            ), this, depthNormalBuffer.getDepthTexture());

            casterDepthScene = new Scene(new Program(
                    game.io.readAsset("shaders/depth.vert"),
                    game.io.readAsset("shaders/depth.frag")
            ), this) {

                @Override
                public void getUniformHandles() {

                }

                @Override
                public void draw() {
                    setLightPerspective(true);

                    for (RenderedObject object : getRenderedObjects()) {
                        if (object.isShadowCaster()) object.draw(this);
                    }

                    setLightPerspective(false);
                }
            };

            ambientLightScene = new AmbientLightScene(new Program(
                    game.io.readAsset("shaders/light_pre_pass/ambient_light.vert"),
                    game.io.readAsset("shaders/light_pre_pass/ambient_light.frag")
            ), this);

            fxaaScene = new FXAAScene(new Program(
                    game.io.readAsset("shaders/fxaa.vert"),
                    game.io.readAsset("shaders/fxaa.frag")
            ), this, finalBuffer.getTextures()[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    @Override
    public void drawWorld() {
        depthNormalBuffer.bind();

        depthNormalScene.use();
        depthNormalScene.drawScene();

        if (getDirectionalLight() != null) {
            shadowMapBuffer.bind();

            casterDepthScene.use();
            casterDepthScene.drawScene();
        }

        lightingBuffer.bind();

        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE);
        GLES30.glCullFace(GLES30.GL_FRONT);

        pointLightScene.use();
        pointLightScene.drawScene();

        GLES30.glCullFace(GLES30.GL_BACK);

        if (getDirectionalLight() != null) {
            directionalLightScene.use();
            directionalLightScene.drawScene();
        }

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        finalBuffer.bind();

        ambientLightScene.use();
        ambientLightScene.drawScene();

        FramebufferObject.unbind(getGame().glView.getWidth(), getGame().glView.getHeight());

        fxaaScene.use();
        fxaaScene.drawScene();
    }

    private class PointLightScene extends Scene {
        private int lightPositionHandle;
        private int lightColorHandle;
        private int lightRadiusHandle;
        private int depthTextureLocationHandle;
        private int normalTextureLocationHandle;

        public PointLightScene(Program program, World world) {
            super(program, world);
        }

        @Override
        public void getUniformHandles() {
            lightPositionHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_LightPosition");
            lightColorHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_LightColor");
            lightRadiusHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_LightRadius");
            depthTextureLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_DepthTexture");
            normalTextureLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_NormalTexture");
        }

        @Override
        public void draw() {
            for (PointLight pointLight : pointLights) {
                depthNormalBuffer.getDepthTexture().bind(0, depthTextureLocationHandle);
                depthNormalBuffer.getTextures()[0].bind(1, normalTextureLocationHandle);

                Vector lightPosition = new Vector(pointLight.getPosition())
                        .transformPointByMatrix(getViewMatrix());

                GLES30.glUniform3f(
                        lightPositionHandle,
                        lightPosition.x,
                        lightPosition.y,
                        lightPosition.z
                );

                GLES30.glUniform3f(
                        lightColorHandle,
                        pointLight.getColor().x,
                        pointLight.getColor().y,
                        pointLight.getColor().z
                );

                GLES30.glUniform1f(lightRadiusHandle, pointLight.getRadius());

                pointLight.setSphere(sphere);
                pointLight.draw(this);
            }
        }
    }

    private class DirectionalLightScene extends PostProcessingScene {
        private int lightDirectionHandle;
        private int lightColorHandle;
        private int smMatrixHandle;
        private int normalTextureLocationHandle;
        private int shadowMapLocationHandle;

        public DirectionalLightScene(Program program, World world, Texture texture) {
            super(program, world, texture);
        }

        @Override
        public void getUniformHandles() {
            lightDirectionHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_LightDirection");
            lightColorHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_LightColor");
            smMatrixHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_SMMatrix");
            normalTextureLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_NormalTexture");
            shadowMapLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_ShadowMap");
        }

        @Override
        public void draw() {
            depthNormalBuffer.getTextures()[0].bind(1, normalTextureLocationHandle);
            shadowMapBuffer.getDepthTexture().bind(2, shadowMapLocationHandle);

            Vector lightDirection = new Vector(getDirectionalLight().getDirection())
                    .transformDirectionByMatrix(getViewMatrix());

            GLES30.glUniform3f(
                    lightDirectionHandle,
                    lightDirection.x,
                    lightDirection.y,
                    lightDirection.z
            );

            GLES30.glUniform3f(
                    lightColorHandle,
                    getDirectionalLight().getColor().x,
                    getDirectionalLight().getColor().y,
                    getDirectionalLight().getColor().z
            );

            GLES30.glUniformMatrix4fv(smMatrixHandle, 1, false, getShadowMapMatrix(), 0);

            super.draw();
        }
    }

    private class AmbientLightScene extends Scene {
        private int diffuseTextureLocationHandle;
        private int specularTextureLocationHandle;

        public AmbientLightScene(Program program, World world) {
            super(program, world);
        }

        @Override
        public void getUniformHandles() {
            diffuseTextureLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_DiffuseTexture");
            specularTextureLocationHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_SpecularTexture");
        }

        @Override
        public void draw() {
            for (RenderedObject object : getRenderedObjects()) {
                lightingBuffer.getTextures()[0].bind(1, diffuseTextureLocationHandle);
                lightingBuffer.getTextures()[1].bind(2, specularTextureLocationHandle);

                object.draw(this);
            }
        }
    }
}
