package com.ideas.anima.engine.graphics.objects.lights;

import com.ideas.anima.engine.graphics.Vertices;
import com.ideas.anima.engine.graphics.objects.RenderedObject;
import com.ideas.anima.engine.graphics.Scene;
import com.ideas.anima.engine.graphics.Vector;

public class PointLight extends RenderedObject {
    private Vector color;
    private float radius;
    private static final float ERROR = 1.04f;
    private Vertices sphere;

    public PointLight(Vector color, float radius) {
        this.color = color;
        this.radius = radius;

        setScale(new Vector(radius * ERROR));
    }

    public PointLight(Vector position, Vector rotation, Vector scale, Vector color, float radius) {
        super(position, rotation, scale.multiply(radius));

        this.color = color;
        this.radius = radius;
    }

    public Vector getColor() {
        return color;
    }

    public void setColor(Vector color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;

        setScale(new Vector(radius * ERROR));
    }

    public void setSphere(Vertices sphere) {
        if (this.sphere == null) this.sphere = sphere;
    }

    @Override
    protected void drawObject(Scene scene) {
        sphere.draw(scene);
    }
}