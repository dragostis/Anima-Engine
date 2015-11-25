package org.anima.engine.scripting;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class AssetsContext implements Context {
    private AssetManager assetMananager;

    public AssetsContext(AssetManager assetManager) {
        this.assetMananager = assetMananager;
    }

    @Override
    public InputStream get(String fileName) throws IOException {
        return assetMananager.open(fileName);
    }
}
