package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.gameplay.physics.Body;
import com.ideas.anima.engine.linearmath.Quaternion;
import com.ideas.anima.engine.linearmath.Vector;

public class GameObject {
    private Vector position;
    private Quaternion rotation;
    private Vector scale;
    private Body body;

    public GameObject() {
        this(new Vector(), new Vector(1.0f), new Quaternion());
    }

    public GameObject(Vector position, Vector scale, Quaternion rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Vector getScale() {
        return scale;
    }

    public void setScale(Vector scale) {
        this.scale = scale;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;

        body.setObject(this);
    }
}
