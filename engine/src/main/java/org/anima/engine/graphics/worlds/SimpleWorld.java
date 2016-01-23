package org.anima.engine.graphics.worlds;

import org.anima.engine.Game;
import org.anima.engine.graphics.FramebufferObject;
import org.anima.engine.graphics.Program;
import org.anima.engine.graphics.Scene;
import org.anima.engine.graphics.World;
import org.anima.engine.graphics.objects.Camera;
import org.anima.engine.graphics.objects.RenderedObject;

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
    public void drawWorld() {
        FramebufferObject.unbind(getGame().glView.getWidth(), getGame().glView.getHeight());

        simpleScene.use();
        simpleScene.drawScene();
    }
}
