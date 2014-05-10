package com.ideas.anima.engine.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.ideas.anima.engine.Pool;

import java.util.ArrayList;
import java.util.List;

public class Touch implements OnTouchListener {
    private boolean isTouched;
    private int touchX;
    private int touchY;
    private Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    private float scaleX;
    private float scaleY;

    public Touch(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };

        touchEventPool = new Pool<TouchEvent>(factory, 100);

        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this) {
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

            touchEventsBuffer.add(touchEvent);

            return true;
        }
    }

    public boolean isTouchDown() {
        synchronized (this) {
            return isTouched;
        }
    }

    public int getTouchX() {
        synchronized (this) {
            return touchX;
        }
    }

    public int getTouchY() {
        synchronized (this) {
            return touchY;
        }
    }

    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            for (TouchEvent touchEvent : touchEvents) touchEventPool.free(touchEvent);

            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();

            return touchEvents;
        }
    }
}
