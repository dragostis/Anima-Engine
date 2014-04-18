package com.ideas.anima.engine.graphics;

import com.ideas.anima.engine.Game;
import com.ideas.anima.engine.graphics.objects.Camera;
import com.ideas.anima.engine.graphics.objects.RenderedObject;

import java.io.IOException;

public class SimpleWorld extends World {
    private Scene simpleScene;

    public SimpleWorld(Game game, Camera camera) {
        super(game, camera);

        try {
            simpleScene = new Scene(new Program(
                    game.io.readAsset("shaders/simple.vert"),
                    game.io.readAsset("shaders/simple.frag")
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        FramebufferObject.unbind(getGame().glView.getWidth(), getGame().glView.getHeight());

        simpleScene.use();

        simpleScene.draw();
    }
}
