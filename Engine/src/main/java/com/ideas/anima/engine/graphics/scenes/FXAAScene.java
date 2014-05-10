package com.ideas.anima.engine.graphics.scenes;

import android.opengl.GLES30;

import com.ideas.anima.engine.graphics.Program;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.World;

public class FXAAScene extends PostProcessingScene {
    private int screenRatioHandle;

    public FXAAScene(Program program, World world, Texture texture) {
        super(program, world, texture);
    }

    @Override
    public void getUniformHandles() {
        screenRatioHandle = GLES30.glGetUniformLocation(
                program.getProgramHandle(), "u_ScreenRatio");
    }

    @Override
    public void draw() {
        GLES30.glUniform2f(
                screenRatioHandle,
                1.0f / getWorld().getGame().glView.getWidth(),
                1.0f / getWorld().getGame().glView.getHeight()
        );

        super.draw();
    }
}
