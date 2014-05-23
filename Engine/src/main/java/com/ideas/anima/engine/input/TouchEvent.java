package com.ideas.anima.engine.input;

public class TouchEvent {
    public Type type;
    public int x;
    public int y;

    public enum Type {
        TOUCH_DOWN,
        TOUCH_UP,
        TOUCH_DRAGGED
    }
}