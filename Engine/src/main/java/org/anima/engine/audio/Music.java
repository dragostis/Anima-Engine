package org.anima.engine.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class Music implements MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    public Music(AssetFileDescriptor assetFileDescriptor) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

        isPrepared = true;

        mediaPlayer.setOnCompletionListener(this);
    }

    public void dispose() {
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();

        mediaPlayer.release();
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped() {
        return !isPrepared;
    }

    public void play() throws IllegalStateException, IOException {
        if (mediaPlayer.isPlaying()) return;

        synchronized (this) {
            if (!isPrepared) mediaPlayer.prepare();

            mediaPlayer.start();
        }
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    public void stop() {
        mediaPlayer.stop();

        synchronized (this) {
            isPrepared = false;
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}