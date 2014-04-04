package com.ideas.anima.engine.data;

public class Model extends Block {
    private Vertices vertices;
    private TextureCoordinates textureCoordinates;
    private Texture texture;
    private NormalMap normalMap;

    public Model(int[] indices) {
        super(indices);
    }

    public void setVertices(Vertices vertices) {
        this.vertices = vertices;
    }

    public void setTextureCoordinates(TextureCoordinates textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setNormalMap(NormalMap normalMap) {
        this.normalMap = normalMap;
    }

    public float[] getArray() {
        return vertices.getContent();
    }
}
