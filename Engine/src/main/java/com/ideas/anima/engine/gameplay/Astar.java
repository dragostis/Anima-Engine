package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Astar {
    protected NavMesh navMesh;
    private RoadWithHeight roadWithHeight;
    private boolean foundPath;
    private float closestSampleDistance;
    private List<Vector> path;
    private List<Candidate> candidates;
    private List<Node> nodes;

    public Astar(NavMesh navMesh) {
        this.navMesh = navMesh;

        path = new ArrayList<Vector>();
        candidates = new ArrayList<Candidate>();
        nodes = new ArrayList<Node>();
    }

    public void findPath(Vector start, Vector finish) {
        nodes.add(new Node(start, 0.0f, null));
        findNeighbours(nodes.get(0));

        findClosestSampleDistance(finish);

        foundPath = false;

        while (!foundPath) {
            findCandidates(finish);
            step(finish);
        }

        generateRoad();

        clear();
    }

    private void findCandidates(Vector finish) {
        for (Node node : nodes)
            for (int j = 0; j < node.neighbours.size(); j++)
                if (!node.neighbours.get(j).isExpanded && !node.neighbours.get(j).isOccupied) {
                    Candidate candidate = new Candidate(node.neighbours.get(j).position);

                    candidate.score = node.distance + candidate.position.distance(node.position) + candidate.position.distance(finish);
                    candidate.parent = node;
                    candidate.neighbours = node.neighbours.get(j).neighbours;
                    candidate.original = node.neighbours.get(j);

                    candidates.add(candidate);
                }

        for (Node node : nodes) {
            float stepSize = node.position.distance(finish);

            if (stepSize < navMesh.getStepRadius()) {
                Candidate candidate = new Candidate(finish);

                candidate.position = finish;
                candidate.score = node.distance + stepSize;
                candidate.parent = node;
                candidate.original = candidate.parent;

                candidates.add(candidate);
            }
        }
    }

    private void step(Vector finish) {
        Candidate chosenCandidate;

        chosenCandidate = candidates.get(0);

        for (int i = 1; i < candidates.size(); i++)
            if (chosenCandidate.score > candidates.get(i).score) {
                chosenCandidate = candidates.get(i);
            }

        candidates.clear();

        nodes.add(new Node(chosenCandidate.position, chosenCandidate.parent.distance
                + chosenCandidate.position.distance(chosenCandidate.parent.position),
                chosenCandidate.parent));
        nodes.get(nodes.size() - 1).neighbours = chosenCandidate.neighbours;
        chosenCandidate.original.isExpanded = true;

        if (chosenCandidate.position == finish || chosenCandidate.position.distance(finish)
                <= closestSampleDistance) {
            foundPath = true;
        }
    }

    private void generateRoad() {
        float maxCurveError = 1.0f / 3.0f;

        List<Vector> lines = new ArrayList<Vector>();
        Node node = nodes.get(nodes.size() - 1);

        while (node != null) {
            path.add(node.position);

            node = node.parent;
        }

        Collections.reverse(path);

        int previousIndex = 1;
        float sum;
        float distance;

        lines.add(path.get(0));

        for (int i = 0; i < path.size() - 1; i++) {
            sum = 0.0f;

            if (lines.size() != 1) {
                for (int j = previousIndex; j < i; j++) {
                    sum += distanceFromLine(lines.get(lines.size() - 1), path.get(i), path.get(j));
                }

                distance = lines.get(lines.size() - 1).distance(path.get(i));
            } else {
                for (int j = previousIndex; j < i; j++) {
                    sum += distanceFromLine(path.get(1), path.get(i), path.get(j));
                }

                distance = path.get(1).distance(path.get(i));
            }

            if (sum / distance >= maxCurveError) {
                lines.add(path.get(i - 1));

                previousIndex = i - 1;
            }
        }

        lines.add(path.get(path.size() - 1));

        path.clear();

        for (Vector line : lines) path.add(line.toSpace());

        Path[] paths = new Path[path.size()];
        paths[0] = new Path(path.get(0), Vector.add(path.get(0), path.get(1)).multiply(0.5f));

        for (int i = 1; i < path.size() - 1; i++) {
            paths[i] = new Path(Vector.add(path.get(i - 1), path.get(i)).multiply(0.5f),
                    Vector.add(path.get(i), path.get(i + 1)).multiply(0.5f), path.get(i));
        }

        paths[path.size() - 1] = new Path(Vector.add(path.get(path.size() - 2),
                path.get(path.size() - 1)).multiply(0.5f), path.get(path.size() - 1));

        roadWithHeight = new RoadWithHeight(paths, navMesh);
    }

    private void findNeighbours(AstarSample sample) {
        for (int i = 0; i < navMesh.getSamples().size(); i++) {
            if (sample.position.distance(navMesh.getSamples().get(i).position)
                    < navMesh.getStepRadius()) {
                sample.neighbours.add(navMesh.getSamples().get(i));
            }
        }
    }

    private void findClosestSampleDistance(Vector finish) {
        float min;
        float distance;

        min = navMesh.getSamples().get(0).position.distance(finish);

        for (int i = 0; i < navMesh.getSamples().size(); i++) {
            distance = navMesh.getSamples().get(i).position.distance(finish);

            if (min > distance && !navMesh.getSamples().get(i).isOccupied) min = distance;
        }

        closestSampleDistance = min < navMesh.getStepRadius() ? 0.0f : min;
    }

    private float distanceFromLine(Vector line1, Vector line2, Vector point) {
        Vector difference = Vector.subtract(line1, line2);
        Vector line = new Vector(1.0f / difference.x, -1.0f / difference.y,
                line1.y / difference.y - line1.x / difference.x);

        return Math.abs(line.x * point.x + line.y * point.y + line.z)
                / (float) Math.sqrt(line.x * line.x + line.y * line.y);
    }

    private void clear() {
        for (int i = 0; i < navMesh.getSamples().size(); i++) {
            navMesh.getSamples().get(i).isExpanded = false;
        }

        path.clear();
        candidates.clear();
        nodes.clear();
    }

    public RoadWithHeight getRoadWithHeight() {
        return roadWithHeight;
    }

    private class Candidate extends AstarSample {
        public float score;
        public Node parent;
        public AstarSample original;

        public Candidate(Vector position) {
            super(position);
        }
    }

    private class Node extends AstarSample {
        public float distance;
        public Node parent;

        public Node(Vector position, float distance, Node parent) {
            super(position);
            this.distance = distance;
            this.parent = parent;
        }
    }
}