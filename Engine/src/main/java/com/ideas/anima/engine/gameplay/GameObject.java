package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

public class GameObject {
    private Vector position;
    private Vector rotation;
    private Vector scale;

    public GameObject() {
        this(new Vector(), new Vector(), new Vector(1.0f));
    }

    public GameObject(Vector position, Vector rotation, Vector scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getRotation() {
        return rotation;
    }

    public void setRotation(Vector rotation) {
        this.rotation = rotation;
    }

    public Vector getScale() {
        return scale;
    }

    public void setScale(Vector scale) {
        this.scale = scale;
    }
}
