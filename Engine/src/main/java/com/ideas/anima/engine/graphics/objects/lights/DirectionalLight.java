package com.ideas.anima.engine.graphics.objects.lights;

import com.ideas.anima.engine.graphics.Vector;

public class DirectionalLight {
    private Vector direction;
    private Vector color;

    public DirectionalLight(Vector direction, Vector color) {
        this.direction = direction.normalize().multiply(-1.0f);
        this.color = color;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction.normalize().multiply(-1.0f);
    }

    public Vector getColor() {
        return color;
    }

    public void setColor(Vector color) {
        this.color = color;
    }
}
