package com.ideas.anima.engine.data;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Data {
    private Block root;

    public Data(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        root = getRoot(dataInputStream);
    }

    private Block getRoot(DataInputStream dataInputStream) throws IOException {
        List<Block> blocks = new ArrayList<Block>();

        while (true) {
            try {
                Type type = Type.values()[dataInputStream.readInt()];

                int numberOfChildren;
                int[] indices;

                int length;
                float[] content;

                String path;

                switch (type) {
                    case MODEL:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new Model(indices));

                        break;
                    case COMPOUND_MODEL:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new CompoundModel(indices));

                        break;
                    case KEY_FRAMED_MODEL:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new KeyFramedModel(indices));

                        break;
                    case PARTICLE:
                        path = dataInputStream.readUTF();

                        blocks.add(new Particle(path));

                        break;
                    case ANIMATED_PARTICLE:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new AnimatedParticle(indices));

                        break;
                    case VERTICES:
                        length = dataInputStream.readInt();
                        content = new float[length];

                        for (int i = 0; i < length; i++) content[i] = dataInputStream.readFloat();

                        blocks.add(new Vertices(content));

                        break;
                    case TEXTURE_COORDINATES:
                        length = dataInputStream.readInt();
                        content = new float[length];

                        for (int i = 0; i < length; i++) content[i] = dataInputStream.readFloat();

                        blocks.add(new Vertices(content));

                        break;
                    case TEXTURE:
                        path = dataInputStream.readUTF();

                        blocks.add(new Texture(path));

                        break;
                    case NORMAL_MAP:
                        path = dataInputStream.readUTF();

                        blocks.add(new NormalMap(path));

                        break;
                }
            } catch (EOFException ignored) {
                break;
            }
        }

        for (Block block : blocks) {
            if (block instanceof Model) {
                Model model = (Model) block;

                model.setVertices((Vertices) blocks.get(block.getIndices()[0]));
                model.setTextureCoordinates((TextureCoordinates) blocks.get(block.getIndices()[1]));
                model.setTexture((Texture) blocks.get(block.getIndices()[2]));
                model.setNormalMap((NormalMap) blocks.get(block.getIndices()[3]));
            }

            if (block instanceof CompoundModel) {
                CompoundModel compoundModel = (CompoundModel) block;
                Model[] children = new Model[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (Model) blocks.get(
                        block.getIndices()[i]);

                compoundModel.setChildren(children);
            }

            if (block instanceof KeyFramedModel) {
                KeyFramedModel keyFramedModel = (KeyFramedModel) block;
                Model[] children = new Model[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (Model) blocks.get(
                        block.getIndices()[i]);

                keyFramedModel.setChildren(children);
            }

            if (block instanceof AnimatedParticle) {
                AnimatedParticle animatedParticle = (AnimatedParticle) block;
                Particle[] children = new Particle[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (Particle) blocks.get(
                        block.getIndices()[i]);

                animatedParticle.setChildren(children);
            }
        }

        return blocks.get(0);
    }

    private enum Type {
        MODEL,
        COMPOUND_MODEL,
        KEY_FRAMED_MODEL,
        PARTICLE,
        ANIMATED_PARTICLE,
        VERTICES,
        TEXTURE_COORDINATES,
        TEXTURE,
        NORMAL_MAP
    }
}
