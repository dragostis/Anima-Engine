package com.ideas.anima.engine.gameplay;

import com.ideas.anima.engine.graphics.Vector;

public class Road extends Path{
	private Path[] paths;
	private float length;
	
	public Road(Path[] paths) {
		this.paths = paths;

        for (Path path : paths) length += path.getLength();
	}
	
	@Override
    public Vector getPosition(float pathPosition) {
		float currentLength = 0.0f;
		
		int index = 0;
		
		for (int i = 0; i < paths.length; i++) {
			currentLength += paths[i].getLength();
			
			if(currentLength > pathPosition) break;
			
			index++;
		}
		
		if (index == paths.length){
			index--;
			
			hasArrived = true;
		}
		
		return paths[index].getPosition(pathPosition - (currentLength - paths[index].getLength()));
	}
	
	@Override
    public Vector getTangent(float pathPosition) {
		float currentLength = 0.0f;
		
		int index = 0;
		
		for (int i = 0; i < paths.length; i++) {
			currentLength += paths[i].getLength();
			
			if(currentLength > pathPosition) break;
			
			index++;
		}
		
		if (index == paths.length){
			index--;
			
			hasArrived = true;
		}
		
		return paths[index].getTangent(pathPosition - (currentLength - paths[index].getLength()));
	}
	
	@Override
    public float getLength() {
		return length;
	}
}
