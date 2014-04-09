package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

public class Scene {
    private Program program;
    private int positionHandle;
    private int textCoordHandle;

    public Scene(Program program) {
        this.program = program;
    }

    public int getPositionHandle() {
        return positionHandle;
    }

    public int getTextCoordHandle() {
        return textCoordHandle;
    }

    public void use() {
        program.use();

        updateHandles();
    }

    private void updateHandles() {
        positionHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_Position");
        textCoordHandle = GLES30.glGetAttribLocation(program.getProgramHandle(), "a_TextCoord");
    }
}
