package com.ideas.anima.engine.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import com.ideas.anima.engine.data.blocks.TextureBlock;

import java.io.InputStream;

public class Texture {
    private int textureHandle;

    public Texture(int width, int height, boolean depth) {
        int[] textureHandleArray = new int[1];

        GLES30.glGenTextures(1, textureHandleArray, 0);

        if (textureHandleArray[0] != 0) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandleArray[0]);

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
                    GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
                    GLES30.GL_NEAREST);

            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
                    GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
                    GLES30.GL_CLAMP_TO_EDGE);

            GLES30.glTexImage2D(
                    GLES30.GL_TEXTURE_2D,
                    0,
                    depth ? GLES30.GL_DEPTH_COMPONENT16 : GLES30.GL_RGB8,
                    width,
                    height,
                    0,
                    depth ? GLES30.GL_DEPTH_COMPONENT : GLES30.GL_RGB,
                    depth ? GLES30.GL_UNSIGNED_SHORT : GLES30.GL_UNSIGNED_BYTE,
                    null
            );

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

            textureHandle = textureHandleArray[0];
        } else {
            throw new RuntimeException("Error loading texture.");
        }
    }

    public Texture(TextureBlock textureBlock) {
        this(textureBlock.getInputStream());
    }

    public Texture(InputStream inputStream) {
        final int[] textureHandleArray = new int[1];

        GLES30.glGenTextures(1, textureHandleArray, 0);

        if (textureHandleArray[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandleArray[0]);

            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
                    GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
                    GLES30.GL_LINEAR);

            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
                    GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
                    GLES30.GL_CLAMP_TO_EDGE);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

            bitmap.recycle();
        }

        if(textureHandleArray[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        textureHandle = textureHandleArray[0];
    }

    public int getTextureHandle() {
        return textureHandle;
    }

    public void setRepeat(boolean repeat) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle);

        if (repeat) {
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
                    GLES30.GL_REPEAT);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
                    GLES30.GL_REPEAT);
        } else {
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
                    GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
                    GLES30.GL_CLAMP_TO_EDGE);
        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    public void bind(int position, int textureLocationHandle) {
        if (position > 16) {
            throw new RuntimeException("Maximum number of textures in a scene is 16, not: " +
                    position);
        }

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + position);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle);

        GLES30.glUniform1i(textureLocationHandle, position);
    }

    public void dispose() {
        int[] textureHandleArray = {textureHandle};

        GLES30.glDeleteTextures(1, textureHandleArray, 0);
    }

    public static void unbind() {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }
}
