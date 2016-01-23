package org.anima.engine.io;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.IOException;

public class Assets {
    private static AssetManager assetManager;

    public static void setAssetManager(AssetManager assetManager) {
        Assets.assetManager = assetManager;
    }

    public static AssetFileDescriptor getFileDescriptor(String path) throws IOException {
        return assetManager.openFd(path);
    }
}
