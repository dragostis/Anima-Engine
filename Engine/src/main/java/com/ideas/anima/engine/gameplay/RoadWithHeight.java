package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

public class RoadWithHeight extends Road {
	private NavMesh navMesh;

	public RoadWithHeight(Path[] paths, NavMesh navMesh) {
		super(paths);
		
		this.navMesh = navMesh;
	}
	
	public Vector getPosition(float pathPosition) {
		Vector position = super.getPosition(pathPosition);
		
		position.y = navMesh.getHeight(Vector.toPlane(position));
		
		return position;
	}
}
