package com.ideas.anima.engine.audio;

import android.media.SoundPool;

public class Sound {
    private SoundPool soundPool;
    private int soundID;

    public Sound(SoundPool soundPool, int soundID) {
        this.soundPool = soundPool;
        this.soundID = soundID;
    }

    public void play(float volume) {
        soundPool.play(soundID, volume, volume, 0, 0, 1);
    }

    public void dispose() {
        soundPool.unload(soundID);
    }
}
