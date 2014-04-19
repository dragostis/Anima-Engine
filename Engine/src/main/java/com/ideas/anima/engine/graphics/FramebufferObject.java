package com.ideas.anima.engine.graphics;

import android.opengl.GLES30;

public class FramebufferObject {
    private int width;
    private int height;
    private int framebufferHandle;
    private Texture[] textures;
    private int[] buffers;

    public FramebufferObject(int width, int height, int numOfTextures) {
        this.width = width;
        this.height = height;
        textures = new Texture[numOfTextures];

        if (numOfTextures > 16) {
            throw new RuntimeException("Maximum number of textures in a framebuffer is 16, not: " +
                    numOfTextures);
        }

        int[] framebufferHandleArray = new int[1];

        GLES30.glGenFramebuffers(1, framebufferHandleArray, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebufferHandleArray[0]);

        for (int i = 0; i < numOfTextures; i++) {
            textures[i] = new Texture(width, height, false);

            GLES30.glFramebufferTexture2D(
                    GLES30.GL_FRAMEBUFFER,
                    GLES30.GL_COLOR_ATTACHMENT0 + i,
                    GLES30.GL_TEXTURE_2D,
                    textures[i].getTextureHandle(),
                    0
            );
        }

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        framebufferHandle = framebufferHandleArray[0];

        buffers = new int[textures.length];

        for (int i = 0; i < textures.length; i++) buffers[i] = GLES30.GL_COLOR_ATTACHMENT0 + i;
    }

    public void bind() {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebufferHandle);

        GLES30.glDrawBuffers(textures.length, buffers, 0);

        GLES30.glViewport(0, 0, width, height);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }

    public void dispose() {
        int[] frambufferHandleArray = {framebufferHandle};

        GLES30.glDeleteFramebuffers(1, frambufferHandleArray, 0);

        for (Texture texture : textures) texture.dispose();
    }

    public static void unbind(int width, int height) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        GLES30.glViewport(0, 0, width, height);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }
}
