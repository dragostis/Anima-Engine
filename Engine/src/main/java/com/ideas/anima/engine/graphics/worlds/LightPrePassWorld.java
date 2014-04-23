package com.ideas.anima.engine.graphics.worlds;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.data.Data;
import com.ideas.anima.engine.data.blocks.VerticesBlock;
import com.ideas.anima.engine.graphics.IndexbufferVertices;
import com.ideas.anima.engine.graphics.PostProcessingScene;
import com.ideas.anima.engine.graphics.FramebufferObject;
import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Vertices;
import com.ideas.anima.engine.graphics.World;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;
import com.ideas.anima.engine.graphics.objects.lights.PointLight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LightPrePassWorld extends World {
    private List<PointLight> pointLights = new ArrayList<>();
    private FramebufferObject depthNormalBuffer;
    private FramebufferObject lightingBuffer;
    private Scene depthNormalScene;
    private PointLightScene pointLightScene;
    private Scene ambientLightScene;
    private Vertices sphere;

    public LightPrePassWorld(Game game, Camera camera) {
        super(game, camera);

        depthNormalBuffer = new FramebufferObject(game.glView.getWidth(),
                game.glView.getHeight(), 1);
        lightingBuffer = new FramebufferObject(game.glView.getWidth(),
                game.glView.getHeight(), 2);

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
                    game.io.readAsset("shaders/point_light.vert"),
                    game.io.readAsset("shaders/point_light.frag")
            ), this);

            ambientLightScene = new AmbientLightScene(new Program(
                    game.io.readAsset("shaders/ambient_light.vert"),
                    game.io.readAsset("shaders/ambient_light.frag")
            ), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    @Override
    public void draw() {
        depthNormalBuffer.bind();

        depthNormalScene.use();
        depthNormalScene.draw();

        lightingBuffer.bind();

        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
        GLES30.glDisable(GLES30.GL_CULL_FACE);
        GLES30.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);

        pointLightScene.use();
        pointLightScene.draw();

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        FramebufferObject.unbind(getGame().glView.getWidth(), getGame().glView.getHeight());

        ambientLightScene.use();
        ambientLightScene.draw();
    }

    private class PointLightScene extends Scene {
        private int lightPositionHandle;
        private int lightColorHandle;
        private int lightRadiusHandle;
        private int eyePositionHandle;
        private int screenSizeHandle;
        private int depthTextureLocationHandle;
        private int normalTextureLocationHandle;
        private float[] inverseVPMatrix = new float[16];

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
            eyePositionHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_EyePosition");
            screenSizeHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_ScreenSize");
            inverseVPMatrixHandle = GLES30.glGetUniformLocation(program.getProgramHandle(),
                    "u_IVPMatrix");
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

                GLES30.glUniform3f(
                        lightPositionHandle,
                        pointLight.getPosition().x,
                        pointLight.getPosition().y,
                        pointLight.getPosition().z
                );

                GLES30.glUniform3f(
                        lightColorHandle,
                        pointLight.getColor().x,
                        pointLight.getColor().y,
                        pointLight.getColor().z
                );

                GLES30.glUniform3f(
                        eyePositionHandle,
                        getCamera().getPosition().x,
                        getCamera().getPosition().y,
                        getCamera().getPosition().z
                );

                GLES30.glUniform1f(lightRadiusHandle, pointLight.getRadius());

                GLES30.glUniform2f(
                        screenSizeHandle,
                        getGame().glView.getWidth(),
                        getGame().glView.getHeight()
                );

                Matrix.multiplyMM(inverseVPMatrix, 0, getProjectionMatrix(), 0, getViewMatrix(), 0);
                Matrix.invertM(inverseVPMatrix, 0, inverseVPMatrix, 0);

                GLES30.glUniformMatrix4fv(getInverseVPMatrixHandle(), 1, false, inverseVPMatrix, 0);

                pointLight.setSphere(sphere);
                pointLight.draw(this);
            }
        }
    }

    private class AmbientLightScene extends Scene {
        private int screenSizeHandle;
        private int diffuseTextureLocationHandle;
        private int specularTextureLocationHandle;

        public AmbientLightScene(Program program, World world) {
            super(program, world);
        }

        @Override
        public void getUniformHandles() {
            screenSizeHandle = GLES30.glGetUniformLocation(
                    program.getProgramHandle(), "u_ScreenSize");
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

                GLES30.glUniform2f(
                        screenSizeHandle,
                        getGame().glView.getWidth(),
                        getGame().glView.getHeight()
                );

                object.draw(this);
            }
        }
    }
}
