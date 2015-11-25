package org.anima.engine.data.blocks;

import org.anima.engine.data.Block;

import java.io.InputStream;

public class TextureBlock extends Block {
    private InputStream inputStream;

    public TextureBlock(InputStream inputStream) {
        super(null);

        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
