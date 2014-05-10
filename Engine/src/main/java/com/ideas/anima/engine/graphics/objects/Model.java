package com.ideas.anima.engine.graphics.objects;

import android.opengl.GLES30;

import com.ideas.anima.engine.data.blocks.ModelBlock;
import com.ideas.anima.engine.graphics.IndexbufferVertices;
import com.ideas.anima.engine.graphics.Quaternion;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Texture;
import com.ideas.anima.engine.graphics.Vector;
import com.ideas.anima.engine.graphics.Vertices;

public class Model extends RenderedObject {
    private Vertices vertices;
    private Texture texture;
    private Texture normalMap;
    private Vector ambientColor = new Vector(0.05f);
    private Vector diffuseColor = new Vector(0.7f);
    private Vector specularColor = new Vector(0.0f);

    public Model(ModelBlock modelBlock) {
        vertices = new IndexbufferVertices(modelBlock.getVerticesBlock());
        texture = new Texture(modelBlock.getTextureBlock());
        normalMap = new Texture(modelBlock.getNormalMapBlock());
    }

    public Model(Vector position, Vector scale, Quaternion rotation, ModelBlock modelBlock) {
        super(position, scale, rotation);

        vertices = new IndexbufferVertices(modelBlock.getVerticesBlock());
        texture = new Texture(modelBlock.getTextureBlock());
        normalMap = new Texture(modelBlock.getNormalMapBlock());
    }

    public Vector getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector specularColor) {
        this.specularColor = specularColor;
    }

    @Override
    protected void drawObject(Scene scene) {
        texture.bind(0, scene.getTextureLocationHandle());

        if (scene.getNormalMapLocationHandle() != -1) {
            normalMap.bind(1, scene.getNormalMapLocationHandle());
        }

        if (scene.getAmbientHandle() != -1) {
            GLES30.glUniform3f(
                    scene.getAmbientHandle(),
                    ambientColor.x,
                    ambientColor.y,
                    ambientColor.z
            );
        }

        if (scene.getDiffuseHandle() != -1) {
            GLES30.glUniform3f(
                    scene.getDiffuseHandle(),
                    diffuseColor.x,
                    diffuseColor.y,
                    diffuseColor.z
            );
        }

        if (scene.getSpecularHandle() != -1) {
            GLES30.glUniform3f(
                    scene.getSpecularHandle(),
                    specularColor.x,
                    specularColor.y,
                    specularColor.z
            );
        }

        vertices.draw(scene);
    }
}
