package org.anima.engine.data;

import org.anima.engine.data.blocks.CompoundModelBlock;
import org.anima.engine.data.blocks.MaterialBlock;
import org.anima.engine.data.blocks.ModelBlock;
import org.anima.engine.data.blocks.TextureBlock;
import org.anima.engine.data.blocks.VerticesBlock;
import org.anima.engine.io.IO;
import org.anima.engine.linearmath.Vector;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Integer, Block> blocksMap = new HashMap<>();

        while (true) {
            try {
                Type type = Type.values()[dataInputStream.readInt()];

                // TODO: remove it from models themselves
                // it's currently used to associate between blocks, but we should do that in this
                // method, and initialize objects without ids, both direct and referential
                int id = dataInputStream.readInt();

                switch (type) {
                    case MODEL:
                        int vertexId = dataInputStream.readInt();
                        int materialId = dataInputStream.readInt();

                        blocksMap.put(id, new ModelBlock(vertexId, materialId));

                        break;
                    case COMPOUND_MODEL:
                        int childrenNumber = dataInputStream.readInt();
                        int[] childrenIds = new int[childrenNumber];

                        for (int i = 0; i < childrenNumber; i++) childrenIds[i] = dataInputStream.readInt();

                        blocksMap.put(id, new CompoundModelBlock(childrenIds));

                        break;
                    case MATERIAL:
                        // Use Vector or float[]?
                        Vector ambient = new Vector(dataInputStream.readFloat(),
                                                    dataInputStream.readFloat(),
                                                    dataInputStream.readFloat());
                        Vector diffuse = new Vector(dataInputStream.readFloat(),
                                                    dataInputStream.readFloat(),
                                                    dataInputStream.readFloat());
                        Vector specular = new Vector(dataInputStream.readFloat(),
                                                     dataInputStream.readFloat(),
                                                     dataInputStream.readFloat());
                        float shininess = dataInputStream.readFloat();
                        int textureId = dataInputStream.readInt();
                        int normalMapId = dataInputStream.readInt();

                        blocksMap.put(id, new MaterialBlock(ambient, diffuse, specular, shininess,
                                                            textureId, normalMapId));

                        break;
                    case VERTICES:
                        int length = dataInputStream.readInt();
                        float[] content = new float[length];

                        for (int i = 0; i < length; i++) content[i] = dataInputStream.readFloat();

                        blocksMap.put(id, new VerticesBlock(content));

                        break;
                    case TEXTURE:
                        length = dataInputStream.readInt();
                        byte[] path = new byte[length];
                        dataInputStream.read(path, 0, length);

                        blocksMap.put(id, new TextureBlock(io.readAsset(new String(path, "UTF-8"))));

                        break;
                }
            } catch (EOFException ignored) {
                break;
            }
        }

        for (Block block : blocksMap.values()) {
            if (block instanceof ModelBlock) {
                ModelBlock modelBlock = (ModelBlock) block;

                modelBlock.setVerticesBlock((VerticesBlock) blocksMap.get(modelBlock.getVertexId()));
                modelBlock.setMaterialBlock((MaterialBlock) blocksMap.get(modelBlock.getMaterialId()));
            }

            if (block instanceof CompoundModelBlock) {
                CompoundModelBlock compoundModelBlock = (CompoundModelBlock) block;
                ModelBlock[] children = new ModelBlock[compoundModelBlock.getChildrenIds().length];

                for (int i = 0; i < children.length; i++) children[i] = (ModelBlock) blocksMap.get(i);

                compoundModelBlock.setChildren(children);
            }
        }

        return blocksMap.get(0);
    }

    private enum Type {
        MODEL,
        COMPOUND_MODEL,
        KEY_FRAMED_MODEL,
        PARTICLE,
        ANIMATED_PARTICLE,
        VERTICES,
        MATERIAL,
        TEXTURE,
    }
}
