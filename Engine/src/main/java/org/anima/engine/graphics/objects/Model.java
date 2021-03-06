package org.anima.engine.graphics.objects;

import android.opengl.GLES30;

import org.anima.engine.data.blocks.ModelBlock;
import org.anima.engine.graphics.IndexbufferVertices;
import org.anima.engine.linearmath.Quaternion;
import org.anima.engine.graphics.Scene;
import org.anima.engine.graphics.Texture;
import org.anima.engine.linearmath.Vector;
import org.anima.engine.graphics.Vertices;

public class Model extends RenderedObject {
    private Vertices vertices;
    private Texture texture;
    private Texture normalMap;
    private Vector ambientColor = new Vector(0.01f);
    private Vector diffuseColor = new Vector(0.7f);
    private Vector specularColor = new Vector(0.0f);

    public Model(ModelBlock modelBlock) {
        super();

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

    public Model(Model model) {
        super();

        this.vertices = model.vertices;
        this.texture = model.texture;
        this.normalMap = model.normalMap;
        this.ambientColor = model.ambientColor;
        this.diffuseColor = model.diffuseColor;
        this.specularColor = model.specularColor;
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
                    ambientColor.getX(),
                    ambientColor.getY(),
                    ambientColor.getZ()
            );
        }

        if (scene.getDiffuseHandle() != -1) {
            GLES30.glUniform3f(
                    scene.getDiffuseHandle(),
                    diffuseColor.getX(),
                    diffuseColor.getY(),
                    diffuseColor.getZ()
            );
        }

        if (scene.getSpecularHandle() != -1) {
            GLES30.glUniform3f(
                    scene.getSpecularHandle(),
                    specularColor.getX(),
                    specularColor.getY(),
                    specularColor.getZ()
            );
        }

        vertices.draw(scene);
    }
}
