package com.jared.asteroids;

import java.util.ArrayList;
import java.util.List;

import com.jared.asteroids.math.JVector;
import com.jared.asteroids.physics.Physics;
import com.jared.asteroids.physics.PhysicsObject;
import com.jared.asteroids.physics.PointMass;

class Ragdoll implements PhysicsObject {
	/*
	     O
	    /|\
	   / | \
	    / \
	   |   |
	 */
	// each pointmass will be a joint to the body.
	PointMass head;
	PointMass shoulder;
	PointMass elbowLeft;
	PointMass elbowRight;
	PointMass handLeft;
	PointMass handRight;
	PointMass pelvis;
	PointMass kneeLeft;
	PointMass kneeRight;
	PointMass footLeft;
	PointMass footRight;
	Circle headCircle;
	List <PointMass> pm;

	boolean dead;
	float dieTime;

	float headLength;
	float totalTime;
	Ragdoll (float x, float y, float bodyHeight) {
		headLength = bodyHeight / 7.5f;
		pm = new ArrayList<PointMass>(); // arraylist of all our pointmasses so we don't have to both with a ton of copy-pasting

		// PointMasses
		// Here, they're initialized with random positions. 
		head = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		shoulder = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		elbowLeft = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		elbowRight = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		handLeft = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		handRight = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		pelvis = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		kneeLeft = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		kneeRight = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		footLeft = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());
		footRight = new PointMass(x + -5 + 10 * (float)Math.random(),y + -5 + 10 * (float)Math.random());

		pm.add(head);
		pm.add(shoulder);
		pm.add(elbowLeft);
		pm.add(elbowRight);
		pm.add(handLeft);
		pm.add(handRight);
		pm.add(pelvis);
		pm.add(kneeLeft);
		pm.add(kneeRight);
		pm.add(footLeft);
		pm.add(footRight);


		// Masses
		// Uses data from http://www.humanics-es.com/ADA304353.pdf
		head.m = 4;
		shoulder.m = 26; // shoulder to torso
		elbowLeft.m = 2; // upper arm mass
		elbowRight.m = 2; 
		handLeft.m = 2;
		handRight.m = 2;
		pelvis.m = 15; // pelvis to lower torso
		kneeLeft.m = 10;
		kneeRight.m = 10;
		footLeft.m = 5; // calf + foot
		footRight.m = 5;

		// Limbs
		// PointMasses are attached to each other here.
		// Proportions are mainly used from http://www.idrawdigital.com/2009/01/tutorial-anatomy-and-proportion/
		head.attachTo(shoulder, 5/4 * headLength, 1, true);
		elbowLeft.attachTo(shoulder, headLength*3/2, 1, true);
		elbowRight.attachTo(shoulder, headLength*3/2, 1, true);
		handLeft.attachTo(elbowLeft, headLength*2, 1, true);
		handRight.attachTo(elbowRight, headLength*2, 1, true);
		pelvis.attachTo(shoulder,headLength*3.5f, 0.8f, true);
		kneeLeft.attachTo(pelvis, headLength*2, 1, true);
		kneeRight.attachTo(pelvis, headLength*2, 1, true);
		footLeft.attachTo(kneeLeft, headLength*2, 1, true);
		footRight.attachTo(kneeRight, headLength*2, 1, true);

		// Head
		headCircle = new Circle(head, headLength*0.75f);
		//	    headCircle = new Circle(head.position, headLength*0.75);
		//	    headCircle.attachToPointMass(head);

		// Invisible Constraints. These add resistance to some limbs from pointing in odd directions.
		// this keeps the head from tilting in extremely uncomfortable positions
		pelvis.attachTo(head, headLength*4.75f, 0.02f, false);
		// these constraints resist flexing the legs too far up towards the body
		footLeft.attachTo(shoulder, headLength*7.5f, 0.001f, false);
		footRight.attachTo(shoulder, headLength*7.5f, 0.001f, false);

		// The PointMasses (and circle!) is added to the world
		//	    world.addCircle(headCircle);
		for (PointMass point : pm)
			Asteroids.addPM(point);
		Physics.objects.add(this);
		Asteroids.ragdolls.add(this);
	}
	public void die() {
		dead = true;
		dieTime = totalTime + 1.2f;
	}
	public void update(float fixedDeltaTimeSeconds) {
		totalTime += fixedDeltaTimeSeconds;
		if (dead && totalTime > dieTime) {
			removeFromWorld();
		}
		for (PointMass point : pm) {
			point.pos.x -= Asteroids.worldSpeed * fixedDeltaTimeSeconds;
			point.lastPos.x -= Asteroids.worldSpeed * fixedDeltaTimeSeconds;
			
			// squirming
			float angle = 2f * (float)Math.PI * (float)Math.random();
			float amplitude = 0.5f * (float)Math.random();
			JVector v = new JVector(amplitude * (float)Math.cos(angle), amplitude * (float)Math.sin(angle));
			point.pos.add(v);
		}

		if (head.pos.x < -10) {
			for (PointMass point : pm) {
				point.pos.x += Renderer.width + 60;
				point.lastPos.x = point.pos.x;
			}
		}
		
	}
	// This must be used if the body is ever deleted
	void removeFromWorld () {
		for (int i = 0; i < pm.size(); i++) {
			PointMass point = pm.get(i);
			Asteroids.pm.remove(point);
			Asteroids.ragdolls.remove(this);
			Physics.objects.remove(this);
			Asteroids.circles.remove(headCircle);
		}
		//	    world.removeCircle(headCircle);
		//	    world.removePointMass(head);
		//	    world.removePointMass(shoulder);
		//	    world.removePointMass(pelvis);
		//	    world.removePointMass(elbowLeft);
		//	    world.removePointMass(elbowRight);
		//	    world.removePointMass(handLeft);
		//	    world.removePointMass(handRight);
		//	    world.removePointMass(kneeLeft);
		//	    world.removePointMass(kneeRight);
		//	    world.removePointMass(footLeft);
		//	    world.removePointMass(footRight);
	}
	void scatter() { // as in shoot this body in a random direction
		float angle = 2.0f * (float)Math.PI * (float)Math.random();
		float power = 20f + 10f * (float)Math.random();
		JVector force = new JVector(power * (float) Math.cos(angle), power * (float) Math.sin(angle));
		for (PointMass point : pm)
			point.lastPos.sub(force);
		
		
	}
	public JVector getAvgPos() {
		JVector total = new JVector(0,0);
		for (PointMass point : pm) {
			total.add(point.pos);
		}
		total.mult(1f/pm.size());
		return total;
	}
}