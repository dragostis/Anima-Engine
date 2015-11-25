package org.anima.engine;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import org.anima.engine.audio.Audio;
import org.anima.engine.input.Input;
import org.anima.engine.io.IO;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Game extends Activity implements GLSurfaceView.Renderer {
    static {
        System.loadLibrary("anima");
    }

    public GLSurfaceView glView;
    public IO io;
    public Input input;
    public Audio audio;
    public Screen screen;
    private long startTime = System.nanoTime();
    private boolean isPaused = false;
    private boolean isResumed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        glView = new GLSurfaceView(this);
        glView.setEGLContextClientVersion(3);
        glView.setRenderer(this);

        this.setContentView(glView);

        io = new IO(this.getAssets());
        input = new Input(this, glView, 1, 1);
        audio = new Audio(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        glView.onPause();

        isPaused = true;
        isResumed = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();

        if (isPaused) isResumed = true;
        isPaused = false;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.screen = getStartScreen();
        screen.resume();
        startTime = System.nanoTime();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    public void onDrawFrame(GL10 gl) {
        float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
        startTime = System.nanoTime();

        if (isPaused) {
            screen.pause();
            isPaused = false;
        }
        if (isResumed) {
            screen.resume();
            isResumed = false;
        }

        screen.update(deltaTime);
        screen.draw();
    }

    public void setScreen(Screen screen) {
        this.screen.pause();
        this.screen.dispose();

        screen.resume();

        this.screen = screen;
    }

    public abstract Screen getStartScreen();
}
