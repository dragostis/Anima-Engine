package org.anima.engine.gameplay.physics;

import org.anima.engine.gameplay.GameObject;
import org.anima.engine.linearmath.Vector;

import java.util.ArrayList;
import java.util.List;

public class PhysicsWorld {
    private List<GameObject> objects = new ArrayList<>();

    public PhysicsWorld(Vector gravity) {
        initialize(gravity.getX(), gravity.getY(), gravity.getZ());
    }

    public void addObject(GameObject object) {
        if (object.getBody() == null) throw new NoBodyException();

        objects.add(object);
        loadObject(object.getBody().getPointer());
    }

    public void removeObject(GameObject object) {
        if (objects.contains(object)) {
            objects.remove(object);
            removeObject(object.getBody().getPointer());
        }
    }

    public void update(float deltaTime) {
        updateWorld(deltaTime);

        float[] updates;

        for (GameObject object : objects) {
            updates = updateObject(object.getBody().getPointer());

            object.getPosition().setX(updates[0]);
            object.getPosition().setY(updates[1]);
            object.getPosition().setZ(updates[2]);

            object.getRotation().setX(updates[3]);
            object.getRotation().setY(updates[4]);
            object.getRotation().setZ(updates[5]);
            object.getRotation().setW(updates[6]);
        }
    }

    private native void initialize(float x, float y, float z);
    private native void loadObject(long body);
    private native void removeObject(long body);
    private native void updateWorld(float deltaTime);
    private native float[] updateObject(long body);

    private class NoBodyException extends RuntimeException {
        public NoBodyException() {
            super("GameObject needs to have a Body.");
        }
    }
}