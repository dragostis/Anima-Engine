package org.anima.engine.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.anima.engine.Pool;

import java.util.ArrayList;
import java.util.List;

public class Touch implements OnTouchListener {
    private boolean isTouched;
    private int touchX;
    private int touchY;
    private int touchDownX;
    private int touchDownY;
    private int deltaX;
    private int deltaY;
    private Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<>();
    private float scaleX;
    private float scaleY;

    public Touch(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };

        touchEventPool = new Pool<>(factory, 100);

        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent motionEvent) {
        TouchEvent touchEvent = touchEventPool.newObject();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = TouchEvent.Type.TOUCH_DOWN;
                isTouched = true;

                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.Type.TOUCH_DRAGGED;
                isTouched = true;

                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:
                touchEvent.type = TouchEvent.Type.TOUCH_UP;
                isTouched = false;

                break;
        }

        touchEvent.x = touchX = (int) (motionEvent.getX() * scaleX);
        touchEvent.y = touchY = (int) (motionEvent.getY() * scaleY);

        switch (touchEvent.type) {
            case TOUCH_DOWN:
                touchDownX = touchX;
                touchDownY = touchY;

                break;
            case TOUCH_DRAGGED:
                deltaX = touchX - touchDownX;
                deltaY = touchY - touchDownY;

                touchDownX = touchX;
                touchDownY = touchY;

                break;
        }

        touchEventsBuffer.add(touchEvent);

        return true;
    }

    public synchronized boolean isTouchDown() {
        return isTouched;
    }

    public synchronized int getTouchX() {
        return touchX;
    }

    public synchronized int getTouchY() {
        return touchY;
    }

    public synchronized int getDeltaX() {
        int dx = this.deltaX;

        this.deltaX = 0;

        return dx;
    }

    public synchronized int getDeltaY() {
        int dy = this.deltaY;

        this.deltaY = 0;

        return dy;
    }

    public synchronized List<TouchEvent> getTouchEvents() {
        for (TouchEvent touchEvent : touchEvents) touchEventPool.free(touchEvent);

        touchEvents.clear();
        touchEvents.addAll(touchEventsBuffer);
        touchEventsBuffer.clear();

        return touchEvents;
    }
}
