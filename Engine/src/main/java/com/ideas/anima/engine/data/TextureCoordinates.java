package com.ideas.anima.engine.data;

public class TextureCoordinates extends Block {
    private float[] content;

    public TextureCoordinates(float[] content) {
        super(null);

        this.content = content;
    }

    public float[] getContent() {
        return content;
    }

    public void setContent(float[] content) {
        this.content = content;
    }
}
