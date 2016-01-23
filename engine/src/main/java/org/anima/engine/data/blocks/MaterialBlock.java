package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;
import org.anima.engine.linearmath.Vector;

public class MaterialBlock extends Block {
    private int textureId;
    private int normalMapId;

    private Vector ambientColor;
    private Vector diffuseColor;
    private Vector specularColor;

    private float shininess;

    private TextureBlock textureBlock;
    private TextureBlock normalMapBlock;

    public MaterialBlock(int id,
                         Vector ambientColor,
                         Vector diffuseColor,
                         Vector specularColor,
                         float shininess,
                         int textureId,
                         int normalMapId) {
        super(id);
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.shininess = shininess;
        this.textureId = textureId;
        this.normalMapId = normalMapId;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getNormalMapId() {
        return normalMapId;
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

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public TextureBlock getTextureBlock() {
        return textureBlock;
    }

    public void setTextureBlock(TextureBlock textureBlock) {
        this.textureBlock = textureBlock;
    }

    public TextureBlock getNormalMapBlock() {
        return normalMapBlock;
    }

    public void setNormalMapBlock(TextureBlock normalMapBlock) {
        this.normalMapBlock = normalMapBlock;
    }
}
