package org.anima.engine.input;

import android.content.Context;
import android.view.View;

import java.util.List;

public class Input {
    private AccelerometerHandler accelerometerHandler;
    private Touch touch;

    public Input(Context context, View view, float scaleX, float scaleY) {
        this.accelerometerHandler = new AccelerometerHandler(context);
        this.touch = new Touch(view, scaleX, scaleY);
    }

    public boolean isTouchDown() {
        return touch.isTouchDown();
    }

    public int getTouchX() {
        return touch.getTouchX();
    }

    public int getTouchY() {
        return touch.getTouchY();
    }

    public int getTouchDeltaX() {
        return touch.getDeltaX();
    }

    public int getTouchDeltaY() {
        return touch.getDeltaY();
    }

    public float getAccelX() {
        return accelerometerHandler.getAccelerationX();
    }

    public float getAccelY() {
        return accelerometerHandler.getAccelerationY();
    }

    public float getAccelZ() {
        return accelerometerHandler.getAccelerationZ();
    }

    public List<TouchEvent> getTouchEvents() {
        return touch.getTouchEvents();
    }
}
