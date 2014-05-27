package com.ideas.anima.engine.graphics.objects;

import com.ideas.anima.engine.gameplay.GameObject;
import com.ideas.anima.engine.linearmath.Matrix;
import com.ideas.anima.engine.linearmath.Quaternion;
import com.ideas.anima.engine.linearmath.Vector;

public class Camera extends GameObject {
    private GameObject look;
    private Vector up;
    private float near = 1.0f;
    private float far = 20.0f;
    private Matrix viewMatrix = new Matrix();
    private Matrix projectionMatrix = new Matrix();

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

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public void rotateCamera(float pitch, float yaw) {
        Quaternion pitchQuaternion = new Quaternion(look.getPosition().subtract(getPosition())
                .cross(up).normalize(), pitch);
        Quaternion yawQuaternion = new Quaternion(new Vector(0.0f, -1.0f, 0.0f), yaw);

        yawQuaternion.multiply(pitchQuaternion);

        Matrix rotation = new Matrix().rotate(yawQuaternion);

        look.setPosition(rotation.tranformDirection(look.getPosition().subtract(getPosition()))
                .add(getPosition()));
    }

    public Matrix getViewMatrix() {
        Vector eye = look.getPosition().subtract(getPosition()).normalize().multiply(-near)
                .add(getPosition());

        return viewMatrix.view(eye, look.getPosition(), up);
    }

    public Matrix getProjectionMatrix(float ratio) {
        return projectionMatrix.frustum(-ratio, ratio, -1.0f, 1.0f, near, far);
    }
}
