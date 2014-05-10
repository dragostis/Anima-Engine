package com.ideas.anima.engine.graphics.objects;

import android.opengl.Matrix;

import com.ideas.anima.engine.gameplay.GameObject;
import com.ideas.anima.engine.graphics.Quaternion;
import com.ideas.anima.engine.graphics.Vector;

public class Camera extends GameObject {
    private GameObject look;
    private Vector up;
    private float[] viewMatrix = new float[16];

    public Camera(GameObject look, Vector up) {
        this.look = look;
        this.up = up;
    }

    public Camera(Vector position, Vector scale, Quaternion rotation, GameObject look, Vector up) {
        super(position, scale, rotation);

        this.look = look;
        this.up = up;
    }

    public GameObject getLook() {
        return look;
    }

    public void setLook(GameObject look) {
        this.look = look;
    }

    public Vector getUp() {
        return up;
    }

    public void setUp(Vector up) {
        this.up = up;
    }

    public void rotateCamera(float pitch, float yaw) {
        Quaternion pitchQuaternion = new Quaternion(Vector.cross(up, Vector.subtract(
                look.getPosition(), getPosition())).normalize(), pitch);
        Quaternion yawQuaternion = new Quaternion(new Vector(0.0f, -1.0f, 0.0f), yaw);

        yawQuaternion.multiply(pitchQuaternion);

        look.setPosition(Vector.subtract(look.getPosition(), getPosition())
                .transformDirectionByMatrix(yawQuaternion.getRotationMatrix()).add(getPosition()));
    }

    public float[] getViewMatrix() {
        Matrix.setLookAtM(
                viewMatrix,
                0,
                getPosition().x,
                getPosition().y,
                getPosition().z,
                look.getPosition().x,
                look.getPosition().y,
                look.getPosition().z,
                up.x,
                up.y,
                up.z
        );

        return viewMatrix;
    }
}
