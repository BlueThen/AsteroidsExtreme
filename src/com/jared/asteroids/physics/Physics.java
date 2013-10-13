package com.jared.asteroids.physics;

import java.util.ArrayList;
import java.util.List;

/* Physics: things that move. */
public class Physics {
	long previousTime;
	long currentTime;

	int fixedDeltaTime;
	float fixedDeltaTimeSeconds;
	int leftOverDeltaTime;

	int constraintAccuracy;
	
	public static List<PhysicsObject> objects = new ArrayList<PhysicsObject>();
	
	public Physics() {
		fixedDeltaTime = 20;
		fixedDeltaTimeSeconds = (float)fixedDeltaTime / 1000f;
		leftOverDeltaTime = 0;

		constraintAccuracy = 2;
		
		previousTime = System.currentTimeMillis();
	}
	public void addPO(PhysicsObject object) {
		objects.add(object);
	}
	public void update(List<PointMass> pm) {
		currentTime = System.currentTimeMillis();
		long deltaTimeMS = currentTime - previousTime;
		previousTime = currentTime;
		  
		int timeStepAmt = (int)((float)(deltaTimeMS + leftOverDeltaTime) / (float)fixedDeltaTime);
		timeStepAmt = Math.min(timeStepAmt, 2);
		  
		leftOverDeltaTime += (int)deltaTimeMS - (timeStepAmt * fixedDeltaTime);

		float mouseInfluenceScalar = 1f / timeStepAmt;
		// update physics
		for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
			// solve the constraints multiple times
			// the more it's solved, the more accurate.
			for (int x = 0; x < constraintAccuracy; x++) {
				for (PointMass point : pm)
					point.solveConstraints();
			}
			
			
			// update each particle's position
			for (PointMass point : pm) {
				point.updateInteractions(mouseInfluenceScalar);
				point.integrate(fixedDeltaTimeSeconds);
			}
			for (int i = 0; i < objects.size(); i++) {
				PhysicsObject object = objects.get(i);
				
				object.update(fixedDeltaTimeSeconds);
			}
		}
	}
}
