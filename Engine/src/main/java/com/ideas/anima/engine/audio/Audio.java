package com.ideas.anima.engine.audio;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

public class Audio {
    private AssetManager assetManager;
    private SoundPool soundPool;

    public Audio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        assetManager = activity.getAssets();

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }

    public Music newMusic(String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);

        return new Music(assetFileDescriptor);
    }

    public Sound newSound(String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);
        int soundID = soundPool.load(assetFileDescriptor, 0);

        return new Sound(soundPool, soundID);
    }
}
