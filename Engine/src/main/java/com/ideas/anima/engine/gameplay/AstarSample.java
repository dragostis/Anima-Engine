package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

import java.util.ArrayList;
import java.util.List;

public class AstarSample {
    public Vector position;
    public boolean isOccupied;
    public boolean isExpanded;
    public List<AstarSample> neighbours;

    public AstarSample(Vector position) {
        this.position = position;

        neighbours = new ArrayList<AstarSample>();
    }
}
