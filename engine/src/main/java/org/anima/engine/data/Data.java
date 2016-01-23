package org.anima.engine.data;

import org.anima.engine.data.blocks.AnimatedParticleBlock;
import org.anima.engine.data.blocks.CompoundModelBLock;
import org.anima.engine.data.blocks.KeyFramedModelBlock;
import org.anima.engine.data.blocks.ModelBlock;
import org.anima.engine.data.blocks.NormalMapBlock;
import org.anima.engine.data.blocks.ParticleBlock;
import org.anima.engine.data.blocks.TextureBlock;
import org.anima.engine.data.blocks.VerticesBlock;
import org.anima.engine.io.IO;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Data {
    private Block root;
    private IO io;

    public Data(InputStream inputStream, IO io) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        this.io = io;

        root = getRoot(dataInputStream);
    }

    public Block getRoot() {
        return root;
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

                        blocks.add(new ModelBlock(indices));

                        break;
                    case COMPOUND_MODEL:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new CompoundModelBLock(indices));

                        break;
                    case KEY_FRAMED_MODEL:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new KeyFramedModelBlock(indices));

                        break;
                    case PARTICLE:
                        path = dataInputStream.readUTF();

                        blocks.add(new ParticleBlock(path));

                        break;
                    case ANIMATED_PARTICLE:
                        numberOfChildren = dataInputStream.readInt();
                        indices = new int[numberOfChildren];

                        for (int i = 0; i < numberOfChildren; i++) indices[i] = dataInputStream.readInt();

                        blocks.add(new AnimatedParticleBlock(indices));

                        break;
                    case VERTICES:
                        length = dataInputStream.readInt();
                        content = new float[length];

                        for (int i = 0; i < length; i++) content[i] = dataInputStream.readFloat();

                        blocks.add(new VerticesBlock(content));

                        break;
                    case TEXTURE:
                        path = dataInputStream.readUTF();

                        blocks.add(new TextureBlock(io.readAsset(path)));

                        break;
                    case NORMAL_MAP:
                        path = dataInputStream.readUTF();

                        blocks.add(new NormalMapBlock(io.readAsset(path)));

                        break;
                }
            } catch (EOFException ignored) {
                break;
            }
        }

        for (Block block : blocks) {
            if (block instanceof ModelBlock) {
                ModelBlock modelBlock = (ModelBlock) block;

                modelBlock.setVerticesBlock((VerticesBlock) blocks.get(block.getIndices()[0]));
                modelBlock.setTextureBlock((TextureBlock) blocks.get(block.getIndices()[1]));
                modelBlock.setNormalMapBlock((NormalMapBlock) blocks.get(block.getIndices()[2]));
            }

            if (block instanceof CompoundModelBLock) {
                CompoundModelBLock compoundModelBLock = (CompoundModelBLock) block;
                ModelBlock[] children = new ModelBlock[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (ModelBlock) blocks.get(
                        block.getIndices()[i]);

                compoundModelBLock.setChildren(children);
            }

            if (block instanceof KeyFramedModelBlock) {
                KeyFramedModelBlock keyFramedModelBlock = (KeyFramedModelBlock) block;
                ModelBlock[] children = new ModelBlock[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (ModelBlock) blocks.get(
                        block.getIndices()[i]);

                keyFramedModelBlock.setChildren(children);
            }

            if (block instanceof AnimatedParticleBlock) {
                AnimatedParticleBlock animatedParticleBlock = (AnimatedParticleBlock) block;
                ParticleBlock[] children = new ParticleBlock[block.getIndices().length];

                for (int i = 0; i < children.length; i++) children[i] = (ParticleBlock) blocks.get(
                        block.getIndices()[i]);

                animatedParticleBlock.setChildren(children);
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
        TEXTURE,
        NORMAL_MAP
    }
}
