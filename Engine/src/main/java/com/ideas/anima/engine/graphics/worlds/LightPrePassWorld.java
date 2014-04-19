package com.ideas.anima.engine.graphics.worlds;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.gameplay.PostProcessingScene;
import com.ideas.anima.engine.graphics.FramebufferObject;
import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.World;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;

import java.io.IOException;

public class LightPrePassWorld extends World {
    private FramebufferObject depthNormalBuffer;
    private Scene depthNormalScene;
    private Scene toQuadScene;

    public LightPrePassWorld(Game game, Camera camera) {
        super(game, camera);

        depthNormalBuffer = new FramebufferObject(game.glView.getWidth(),
                game.glView.getWidth(), 1);

        try {
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

            toQuadScene = new PostProcessingScene(new Program(
                    game.io.readAsset("shaders/to_quad.vert"),
                    game.io.readAsset("shaders/to_quad.frag")
            ), this, depthNormalBuffer.getDepthTexture()) {
                @Override
                public void getUniformHandles() {

                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        depthNormalBuffer.bind();

        depthNormalScene.use();
        depthNormalScene.draw();

        FramebufferObject.unbind(getGame().glView.getWidth(), getGame().glView.getHeight());

        toQuadScene.use();
        toQuadScene.draw();
    }
}
