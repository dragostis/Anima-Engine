package com.ideas.anima.engine.data;

public class Model extends Block {
    private Vertices vertices;
    private Texture texture;
    private NormalMap normalMap;

    public Model(int[] indices) {
        super(indices);
    }

    public Vertices getVertices() {
        return vertices;
    }

    public void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public NormalMap getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(NormalMap normalMap) {
        this.normalMap = normalMap;
    }
}
