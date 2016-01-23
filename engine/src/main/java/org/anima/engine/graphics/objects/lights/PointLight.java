package org.anima.engine.graphics.objects.lights;

import org.anima.engine.graphics.Vertices;
import org.anima.engine.graphics.objects.RenderedObject;
import org.anima.engine.graphics.Scene;
import org.anima.engine.linearmath.Vector;

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
