package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    private List<Circle> circles;

    public CollisionDetector() {
        circles = new ArrayList<Circle>();
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public void detect(NavMesh navMesh) {
        for (int i = 0; i < navMesh.getSamples().size(); i++) {
            navMesh.getSamples().get(i).isOccupied = false;

            for (Circle circle : circles) {
                if (navMesh.getSamples().get(i).position.distance(circle.position)
                        <= circle.radius) {
                    navMesh.getSamples().get(i).isOccupied = true;
                }
            }
        }
    }

    public static class Circle {
        public Vector position;
        public float radius;

        public Circle(Vector position, float radius) {
            this.position = position;
            this.radius = radius;
        }
    }
}
