package com.ideas.anima.engine.io;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IO {
    AssetManager assetManager;
    String externalStoragePath;

    public IO(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    public InputStream readAsset(String fileName) throws IOException {
        return assetManager.open(fileName);
    }

    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
}
