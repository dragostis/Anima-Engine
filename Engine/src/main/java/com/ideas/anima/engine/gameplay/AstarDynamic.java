package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

public class AstarDynamic extends Astar {
    private CollisionDetector collisionDetector;

    public AstarDynamic(NavMesh navMesh, CollisionDetector collisionDetector) {
        super(navMesh);

        this.collisionDetector = collisionDetector;
    }

    @Override
    public void findPath(Vector start, Vector finish) {
        collisionDetector.detect(navMesh);

        super.findPath(start, finish);
    }
}
